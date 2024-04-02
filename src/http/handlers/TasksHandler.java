package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.IntersectDurationTaskException;
import managers.ManagerSaveException;
import managers.TaskManager;
import tasks.Task;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class TasksHandler implements HttpHandler {
    TaskManager taskManager;
    Gson gson;

    public TasksHandler (TaskManager taskManager, Gson gson){
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        String query = httpExchange.getRequestURI().getQuery();
        InputStream inputStream = httpExchange.getRequestBody();
        String bodyString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        String info = "Запрос: метод - " + method + "; путь -  "
         + path + "; параметры строки запроса - " + query + "; тело - " + bodyString;
        System.out.println(info);
        int responseCode = 0;

        String response = "";
        switch (method) {
            case "GET":
                try {
                    if (path.contains("/tasks/") && path.split("/").length == 3) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        if (taskManager.getTask(id) == null) {
                            responseCode = 404;
                            response = "Задача с id = " + id + " не найдена";
                        } else {
                            responseCode = 200;
                            response = gson.toJson(taskManager.getTask(id));
                        }
                    } else if (path.equals("/tasks")) {
                        responseCode = 200;
                        response = gson.toJson(taskManager.getAllTasks());
                    } else {
                        responseCode = 404;
                        response = "Проверьте правильность пути запроса";
                    }
                    break;
                } catch (NumberFormatException exception) {
                    responseCode = 404;
                    response = "id должно быть числом";
                }
            case "POST":
                try {
                    Task taskFromJson = gson.fromJson(bodyString, Task.class);
                    if (taskFromJson.getId() == 0) {
                        responseCode = 201;
                        response = gson.toJson(taskManager.addTask(taskFromJson));
                    } else {
                        taskManager.updateTask(taskFromJson);
                        responseCode = 201;
                        response = gson.toJson(taskManager.getTask(taskFromJson.getId()));
                    }
                    break;
                } catch (IntersectDurationTaskException exception) {
                    responseCode = 406;
                    response = "Задача пересекается по времени выполнения";
                    break;
                } catch (ManagerSaveException exception) {
                    responseCode = 500;
                    response = "Ошибка сохранения данных менеджера в файл";
                    break;
                }
            case "DELETE":
                int id = Integer.parseInt(query.substring(3));
                taskManager.removeTask(id);
                responseCode = 200;
                response = "Task c id = " + id + " удален";
                break;
        }
        httpExchange.sendResponseHeaders(responseCode, 0);

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
        System.out.println("Ответ: Код - " + responseCode + "; тело ответа - " + response);
    }
}
