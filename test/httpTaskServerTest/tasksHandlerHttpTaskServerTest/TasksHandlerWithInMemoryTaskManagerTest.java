package httpTaskServerTest.tasksHandlerHttpTaskServerTest;

import http.HttpTaskServer;
import managers.InMemoryTaskManager;
import managers.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.IOException;

public class TasksHandlerWithInMemoryTaskManagerTest extends TasksHandlerTest<InMemoryTaskManager> {

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



