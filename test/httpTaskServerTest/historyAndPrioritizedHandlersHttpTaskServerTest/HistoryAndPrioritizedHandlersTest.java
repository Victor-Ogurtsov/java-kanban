package httpTaskServerTest.historyAndPrioritizedHandlersHttpTaskServerTest;

import com.google.gson.Gson;
import http.HttpTaskServer;
import managers.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class HistoryAndPrioritizedHandlersTest<T extends TaskManager> {
    protected T taskManager;

    @Test
    void ShouldReturnCode200AndHistoryListWhenHistoryRequest() throws IOException, InterruptedException {
        HttpTaskServer.startHttpTaskServer(taskManager);
        Gson gson = HttpTaskServer.getGson();
        URI uri = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        Epic epic = taskManager.addEpic(new Epic("name1", "descriptions1"));
        taskManager.getEpic(epic.getId());
        SubTask subTask = taskManager.addSubTask(epic.getId(), new SubTask("name2", "descriptions2", LocalDateTime.now(), Duration.ofMinutes(10)));
        taskManager.addSubTask(epic.getId(), new SubTask("name3", "descriptions3", LocalDateTime.now().plusMinutes(30), Duration.ofMinutes(10)));
        taskManager.getSubtask(subTask.getId());
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(200, response.statusCode(), "Значение кода не соответствует успеху");
        Assertions.assertEquals(gson.toJson(taskManager.getHistory()), response.body(), "Список истории просмотра ответа" +
                "не соответствует списку истории просмотра в taskManager");
        HttpTaskServer.stopHttpTaskServer();
    }

    @Test
    void ShouldReturnCode200AndPrioritizedListWhenPrioritizedRequest() throws IOException, InterruptedException {
        HttpTaskServer.startHttpTaskServer(taskManager);
        Gson gson = HttpTaskServer.getGson();
        URI uri = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        Epic epic = taskManager.addEpic(new Epic("name1", "descriptions1"));
        taskManager.addSubTask(epic.getId(), new SubTask("name2", "descriptions2", LocalDateTime.now(), Duration.ofMinutes(10)));
        taskManager.addSubTask(epic.getId(), new SubTask("name3", "descriptions3", LocalDateTime.now().plusMinutes(30), Duration.ofMinutes(10)));
        taskManager.addTask(new Task("name4", "descriptions4", LocalDateTime.now().plusMinutes(60), Duration.ofMinutes(10)));
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(200, response.statusCode(), "Значение кода не соответствует успеху");
        Assertions.assertEquals(gson.toJson(taskManager.getPrioritizedTasks()), response.body(), "Список истории просмотра ответа" +
                "не соответствует списку истории просмотра в taskManager");
        HttpTaskServer.stopHttpTaskServer();
    }
}
