package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HistoryHandler implements HttpHandler {

    TaskManager taskManager;
    Gson gson;

    public HistoryHandler(TaskManager taskManager, Gson gson){
        this.taskManager = taskManager;
        this.gson = gson;
    }
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = gson.toJson(taskManager.getHistory());

        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }
}
