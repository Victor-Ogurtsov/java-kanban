package httpTaskServerTest.subTasksHandlerHttpTaskServerTest;

import http.HttpTaskServer;
import managers.InMemoryTaskManager;
import managers.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

public class SubTasksHandlerWithInMemoryTaskManagerTest extends SubTasksHandlerTest<InMemoryTaskManager> {
    @BeforeEach
    public void startHttpServerAndSetTaskManagerAndGson() throws IOException {
        taskManager = Managers.getDefault();
        HttpTaskServer.startHttpTaskServer(taskManager);
        gson = HttpTaskServer.getGson();
    }

    @AfterEach
    public void stopHttpServer(){
        HttpTaskServer.stopHttpTaskServer();
    }
}
