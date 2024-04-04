package httpTaskServerTest.subTasksHandlerHttpTaskServerTest;

import com.google.gson.Gson;
import managers.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class SubTasksHandlerTest<T extends TaskManager> {

    protected T taskManager;
    Gson gson;

    @Test
    void ShouldReturnCode200AndSubTasksListWhenGETRequest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks");
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
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(200, response.statusCode(), "Значение кода не соответствует успеху");
        Assertions.assertEquals(gson.toJson(taskManager.getAllSubTasks()), response.body(), "Список подзадач ответа" +
                "не соответствует списку подзадач в taskManager");
    }

    @Test
    void ShouldReturnCode200AndSubTaskWhenGETRequestAndId() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        Epic epic = taskManager.addEpic(new Epic("name1", "descriptions1"));
        SubTask subTask1 = taskManager.addSubTask(epic.getId(), new SubTask("name2", "descriptions2", LocalDateTime.now(), Duration.ofMinutes(10)));
        taskManager.addSubTask(epic.getId(), new SubTask("name3", "descriptions3", LocalDateTime.now().plusMinutes(30), Duration.ofMinutes(10)));
        HttpResponse<String> response = client.send(request, handler);

        Assertions.assertEquals(200, response.statusCode(), "Значение кода не соответствует успеху");
        Assertions.assertEquals(gson.toJson(taskManager.getSubtask(subTask1.getId())), response.body(), "Ответ не содержит" +
                " запрошенной subTask в taskManager");
    }

    @Test
    void ShouldReturnCode404WhenGETRequestAndSubTaskNotCreate() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtask/1");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(404, response.statusCode(), "Значение кода ответа неверно");
    }

    @Test
    void ShouldReturnCode406WhenPOSTRequestAndTasksHaveIntersectionDuration() throws IOException, InterruptedException {
        Epic epic = taskManager.addEpic(new Epic("name1", "descriptions1"));
        taskManager.addSubTask(epic.getId(), new SubTask("name2", "descriptions2", LocalDateTime.now(), Duration.ofMinutes(10)));
        SubTask subTask2 = new SubTask("name3", "descriptions3", LocalDateTime.now(), Duration.ofMinutes(10));
        subTask2.setEpicId(epic.getId());
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTask2)))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(406, response.statusCode(), "Значение кода ответа неверно");
    }

    @Test
    void shouldReturnCode201AndCreateSubTaskWhenPOSTRequestAndSubTaskNotHaveId() throws IOException, InterruptedException {
        Epic epic = taskManager.addEpic(new Epic("name1", "descriptions1"));
        SubTask subTask = new SubTask("name2", "descriptions2", LocalDateTime.now(), Duration.ofMinutes(10));
        subTask.setEpicId(epic.getId());
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTask)))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        subTask.setId(2);
        Assertions.assertEquals(201, response.statusCode(), "Значение кода не соответствует успеху");
        Assertions.assertEquals(gson.toJson(subTask), response.body(), "subTask из тела запроса" +
                " не записана в менеджер задач");
    }

    @Test
    void shouldReturnCode201AndUpdateSubTaskWhenPOSTRequestAndSubTaskHaveId() throws IOException, InterruptedException {
        Epic epic = taskManager.addEpic(new Epic("name1", "descriptions1"));
        SubTask subTask1 = taskManager.addSubTask(epic.getId(), new SubTask("name2", "descriptions2", LocalDateTime.now(), Duration.ofMinutes(10)));
        SubTask subTaskForUpdate = new SubTask("NEW name2", "descriptions2", LocalDateTime.now(), Duration.ofMinutes(10));
        subTaskForUpdate.setEpicId(1);
        subTaskForUpdate.setId(2);
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTaskForUpdate)))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(201, response.statusCode(), "Значение кода не соответствует успеху");
        Assertions.assertEquals(subTaskForUpdate, taskManager.getSubtask(subTask1.getId()), "subTask из тела запроса" +
                " не обновила subTask в менеджере задач");
    }

    @Test
    void shouldReturnCode200WhenDELETERequest() throws IOException, InterruptedException {
        Epic epic = taskManager.addEpic(new Epic("name1", "descriptions1"));
        SubTask subTask1 = taskManager.addSubTask(epic.getId(), new SubTask("name2", "descriptions2", LocalDateTime.now(), Duration.ofMinutes(10)));
        URI uri = URI.create("http://localhost:8080/subtasks?id=2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(200, response.statusCode(), "Значение кода не соответствует успеху");
        Assertions.assertNull(taskManager.getSubtask(subTask1.getId()), "Подзадача из запроса осталась taskManager");
    }
}
