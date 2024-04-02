package httpTaskServerTest.epicsHandlerHttpTaskServerTest;

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

public abstract class EpicsHandlerTest<T extends TaskManager> {

    protected T taskManager;
    @Test
    void ShouldReturnCode200AndEpicListWhenGETRequest() throws IOException, InterruptedException {
        HttpTaskServer.startHttpTaskServer(taskManager);
        Gson gson = HttpTaskServer.getGson();
        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        taskManager.addEpic(new Epic("name1", "descriptions1"));
        taskManager.addEpic(new Epic("name2", "descriptions2"));
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(200, response.statusCode(), "Значение кода не соответствует успеху");
        Assertions.assertEquals(gson.toJson(taskManager.getAllEpics()), response.body(), "Список epic ответа" +
                "не соответствует списку epic в taskManager");
        HttpTaskServer.stopHttpTaskServer();
    }

    @Test
    void ShouldReturnCode200AndEpicWhenGETRequestAndId() throws IOException, InterruptedException {
        HttpTaskServer.startHttpTaskServer(taskManager);
        Gson gson = HttpTaskServer.getGson();
        URI uri = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        Task epic1 = taskManager.addEpic(new Epic("name1", "descriptions1"));
        taskManager.addEpic(new Epic("name2", "descriptions2"));
        HttpResponse<String> response = client.send(request, handler);

        Assertions.assertEquals(200, response.statusCode(), "Значение кода не соответствует успеху");
        Assertions.assertEquals(gson.toJson(taskManager.getEpic(epic1.getId())), response.body(), "Ответ не содержит" +
                " запрошенного epic в taskManager");
        HttpTaskServer.stopHttpTaskServer();
    }

    @Test
    void ShouldReturnCode404WhenGETRequestAndEpicNotCreate() throws IOException, InterruptedException {
        HttpTaskServer.startHttpTaskServer(taskManager);
        URI uri = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        Assertions.assertEquals(404, response.statusCode(), "Значение кода ответа неверно");
        HttpTaskServer.stopHttpTaskServer();
    }

    @Test
    void ShouldReturnCode404WhenGETRequestForGetSubTasksFromEpicAndEpicNotCreate() throws IOException, InterruptedException {
        HttpTaskServer.startHttpTaskServer(taskManager);
        URI uri = URI.create("http://localhost:8080/epics/1/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        Assertions.assertEquals(404, response.statusCode(), "Значение кода ответа неверно");
        HttpTaskServer.stopHttpTaskServer();
    }

    @Test
    void ShouldReturnCode200AndSubTaskListWhenGETRequestForGetSubTasksFromEpic() throws IOException, InterruptedException {
        HttpTaskServer.startHttpTaskServer(taskManager);
        Gson gson = HttpTaskServer.getGson();
        Task epic1 = taskManager.addEpic(new Epic("name1", "descriptions1"));
        taskManager.addSubTask(epic1.getId(), new SubTask("name2","descriptions2", LocalDateTime.now(), Duration.ofMinutes(10)));
        taskManager.addSubTask(epic1.getId(), new SubTask("name3","descriptions3", LocalDateTime.now().plusMinutes(30), Duration.ofMinutes(10)));
        URI uri = URI.create("http://localhost:8080/epics/1/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);

        Assertions.assertEquals(200, response.statusCode(), "Значение кода ответа неверно");
        Assertions.assertEquals(gson.toJson(taskManager.getAllSubTaskOfEpic(epic1.getId())), response.body(),
                "список подзадач эпика в ответе на запрос не соответствует списку из taskManager");
        HttpTaskServer.stopHttpTaskServer();
    }

    @Test
    void shouldReturnCode201AndCreateEpicWhenPOSTRequestAndEpicNotHaveId() throws IOException, InterruptedException {
        HttpTaskServer.startHttpTaskServer(taskManager);
        Gson gson = HttpTaskServer.getGson();
        Epic epic = new Epic("name1", "descriptions1");
        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        epic.setId(1);
        Assertions.assertEquals(201, response.statusCode(), "Значение кода не соответствует успеху");
        Assertions.assertEquals(gson.toJson(epic), (response.body()), "Epic из тела запроса" +
                " не записан в менеджер задач");
        HttpTaskServer.stopHttpTaskServer();
    }

    @Test
    void shouldReturnCode201AndUpdateEpicWhenPOSTRequestAndEpicHaveId() throws IOException, InterruptedException {
        HttpTaskServer.startHttpTaskServer(taskManager);
        Gson gson = HttpTaskServer.getGson();
        Epic epic = taskManager.addEpic(new Epic("name1", "descriptions1"));
        Epic epicForUpdate = new Epic("NEW name1", "descriptions1");
        epicForUpdate.setId(1);
        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epicForUpdate)))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(201, response.statusCode(), "Значение кода не соответствует успеху");
        Assertions.assertEquals(epicForUpdate, taskManager.getEpic(epic.getId()), "Epic из тела запроса" +
                " не обновила Epic в менеджере задач");
        HttpTaskServer.stopHttpTaskServer();
    }

    @Test
    void shouldReturnCode200WhenDELETERequest() throws IOException, InterruptedException {
        HttpTaskServer.startHttpTaskServer(taskManager);
        Epic epic = taskManager.addEpic(new Epic("name1", "descriptions1"));
        URI uri = URI.create("http://localhost:8080/epics?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(200, response.statusCode(), "Значение кода не соответствует успеху");
        Assertions.assertNull(taskManager.getTask(epic.getId()), "Задача из запроса осталась taskManager");
        HttpTaskServer.stopHttpTaskServer();
    }
}
