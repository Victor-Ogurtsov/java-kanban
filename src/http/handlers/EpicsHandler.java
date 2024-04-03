package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.IntersectDurationTaskException;
import managers.ManagerSaveException;
import managers.TaskManager;
import tasks.Epic;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class EpicsHandler extends BasicHandler implements HttpHandler {

    public EpicsHandler(TaskManager taskManager, Gson gson) {
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
                    if (path.contains("/epics/") && path.split("/").length == 3) {
                        if (checkIdFromPathInManager(path)) {
                            responseCode = 404;
                            response = "Epic с id = " + getIdFromPath(path) + " не найден";
                        } else {
                            responseCode = 200;
                            response = gson.toJson(taskManager.getEpic(getIdFromPath(path)));
                        }
                    } else if (path.contains("/epics/") && path.split("/")[3].equals("subtasks")) {
                        if (checkIdFromPathInManager(path)) {
                            responseCode = 404;
                            response = "Epic с id = " + getIdFromPath(path) + " не найден";
                        } else {
                            int id = Integer.parseInt(path.split("/")[2]);
                            responseCode = 200;
                            response = gson.toJson(taskManager.getAllSubTaskOfEpic(id));
                        }
                    } else if (path.equals("/epics")) {
                        responseCode = 200;
                        response = gson.toJson(taskManager.getAllEpics());
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
                    Epic epicFromJson = gson.fromJson(bodyString, Epic.class);
                    if (epicFromJson.getId() == 0) {
                        responseCode = 201;
                        response = gson.toJson(taskManager.addEpic(epicFromJson));
                    } else {
                        taskManager.updateEpic(epicFromJson);
                        responseCode = 201;
                        response = gson.toJson(taskManager.getEpic(epicFromJson.getId()));
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
                taskManager.removeEpic(id);
                responseCode = 200;
                response = "Epic c id = " + id + " удален";
                break;
        }
        httpExchange.sendResponseHeaders(responseCode, 0);

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
        System.out.println("Ответ: Код - " + responseCode + "; тело ответа - " + response);
    }

    boolean checkIdFromPathInManager(String path) {
        int epicId = getIdFromPath(path);
        return (taskManager.getEpic(epicId) == null);
    }

    int getIdFromPath(String path) {
        return Integer.parseInt(path.split("/")[2]);
    }

}
