package httpTaskServerTest.tasksHandlerHttpTaskServerTest;

import com.google.gson.Gson;
import http.HttpTaskServer;
import managers.TaskManager;
import org.junit.jupiter.api.*;
import tasks.Task;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class TasksHandlerTest<T extends TaskManager> {

    protected T taskManager;

    @Test
    void ShouldReturnCode200AndTasksListWhenGETRequest() throws IOException, InterruptedException {
        HttpTaskServer.startHttpTaskServer(taskManager);
        Gson gson = HttpTaskServer.getGson();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        taskManager.addTask(new Task("name1", "descriptions1", LocalDateTime.now(), Duration.ofMinutes(10)));
        taskManager.addTask(new Task("name2", "descriptions2", LocalDateTime.now().plusMinutes(30), Duration.ofMinutes(10)));
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(200, response.statusCode(), "Значение кода не соответствует успеху");
        Assertions.assertEquals(gson.toJson(taskManager.getAllTasks()), response.body(), "Список задач ответа" +
                "не соответствует списку задач в taskManager");
        HttpTaskServer.stopHttpTaskServer();
    }

    @Test
    void ShouldReturnCode200AndTaskWhenGETRequestAndId() throws IOException, InterruptedException {
        HttpTaskServer.startHttpTaskServer(taskManager);
        Gson gson = HttpTaskServer.getGson();
        URI uri = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        Task newTask1 = taskManager.addTask(new Task("name1", "descriptions1", LocalDateTime.now(), Duration.ofMinutes(10)));
        taskManager.addTask(new Task("name2", "descriptions2", LocalDateTime.now().plusMinutes(30), Duration.ofMinutes(10)));
        HttpResponse<String> response = client.send(request, handler);

        Assertions.assertEquals(200, response.statusCode(), "Значение кода не соответствует успеху");
        Assertions.assertEquals(gson.toJson(taskManager.getTask(newTask1.getId())), response.body(), "Ответ не содержит" +
                " запрошенной Task в taskManager");
        HttpTaskServer.stopHttpTaskServer();
    }

    @Test
    void ShouldReturnCode404WhenGETRequestAndTaskNotCreate() throws IOException, InterruptedException {
        HttpTaskServer.startHttpTaskServer(taskManager);
        URI uri = URI.create("http://localhost:8080/tasks/1");
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
    void ShouldReturnCode406WhenPOSTRequestAndTasksHaveIntersectionDuration() throws IOException, InterruptedException {
        HttpTaskServer.startHttpTaskServer(taskManager);
        Gson gson = HttpTaskServer.getGson();
        taskManager.addTask(new Task("name1", "descriptions1", LocalDateTime.now(), Duration.ofMinutes(10)));
        Task task2 = new Task("name2", "descriptions2", LocalDateTime.now(), Duration.ofMinutes(10));
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task2)))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(406, response.statusCode(), "Значение кода ответа неверно");
        HttpTaskServer.stopHttpTaskServer();
    }

    @Test
    void shouldReturnCode201AndCreateTaskWhenPOSTRequestAndTaskNotHaveId() throws IOException, InterruptedException {
        HttpTaskServer.startHttpTaskServer(taskManager);
        Gson gson = HttpTaskServer.getGson();
        Task task = new Task("name1", "descriptions1", LocalDateTime.now(), Duration.ofMinutes(10));
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        task.setId(1);
        Assertions.assertEquals(201, response.statusCode(), "Значение кода не соответствует успеху");
        Assertions.assertEquals(gson.toJson(task), response.body(), "Task из тела запроса" +
                " не записана в менеджер задач");
        HttpTaskServer.stopHttpTaskServer();
    }

    @Test
    void shouldReturnCode201AndUpdateTaskWhenPOSTRequestAndTaskHaveId() throws IOException, InterruptedException {
        HttpTaskServer.startHttpTaskServer(taskManager);
        Gson gson = HttpTaskServer.getGson();
        Task task = taskManager.addTask(new Task("name1", "descriptions1", LocalDateTime.now(), Duration.ofMinutes(10)));
        Task taskForUpdate = new Task("NEW name1", "descriptions1", LocalDateTime.now(), Duration.ofMinutes(10));
        taskForUpdate.setId(1);
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(taskForUpdate)))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(201, response.statusCode(), "Значение кода не соответствует успеху");
        Assertions.assertEquals(taskForUpdate, taskManager.getTask(task.getId()), "Task из тела запроса" +
                " не стала не обновила Task в менеджере задач");
        HttpTaskServer.stopHttpTaskServer();
    }

    @Test
    void shouldReturnCode200WhenDELETERequest() throws IOException, InterruptedException {
        HttpTaskServer.startHttpTaskServer(taskManager);
        Task task = taskManager.addTask(new Task("name1", "descriptions1", LocalDateTime.now(), Duration.ofMinutes(10)));
        URI uri = URI.create("http://localhost:8080/tasks?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(200, response.statusCode(), "Значение кода не соответствует успеху");
        Assertions.assertNull(taskManager.getTask(task.getId()), "Задача из запроса осталась taskManager");
        HttpTaskServer.stopHttpTaskServer();
    }
}