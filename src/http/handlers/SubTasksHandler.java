package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.IntersectDurationTaskException;
import managers.ManagerSaveException;
import managers.TaskManager;
import tasks.SubTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class SubTasksHandler extends BasicHandler implements HttpHandler {

    public SubTasksHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
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
                    if (path.contains("/subtasks/") && path.split("/").length == 3) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        if (taskManager.getSubtask(id) == null) {
                            responseCode = 404;
                            response = "SubTask с id = " + id + " не найдена";
                        } else {
                            responseCode = 200;
                            response = gson.toJson(taskManager.getSubtask(id));
                        }
                    } else if (path.equals("/subtasks")) {
                        responseCode = 200;
                        response = gson.toJson(taskManager.getAllSubTasks());
                    } else {
                        response = "Проверьте правильность пути запроса";
                        responseCode = 404;
                    }
                    break;
                } catch (NumberFormatException exception) {
                    responseCode = 404;
                    response = "id должно быть числом";
                }
            case "POST":
                try {
                    SubTask subTaskFromJson = gson.fromJson(bodyString, SubTask.class);
                    if (subTaskFromJson.getId() == 0) {
                        int epicId = subTaskFromJson.getEpicId();
                        responseCode = 201;
                        response = gson.toJson(taskManager.addSubTask(epicId, subTaskFromJson));
                    } else {
                        taskManager.updateSubTask(subTaskFromJson);
                        responseCode = 201;
                        response = gson.toJson(taskManager.getSubtask(subTaskFromJson.getId()));
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
                taskManager.removeSubTask(id);
                responseCode = 200;
                response = "SubTask c id = " + id + " удален";
                break;
        }
        httpExchange.sendResponseHeaders(responseCode, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }
}
