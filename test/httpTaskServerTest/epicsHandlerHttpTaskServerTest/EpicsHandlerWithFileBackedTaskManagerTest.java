package httpTaskServerTest.epicsHandlerHttpTaskServerTest;

        import http.HttpTaskServer;
        import managers.FileBackedTaskManager;
        import managers.Managers;
        import org.junit.jupiter.api.AfterEach;
        import org.junit.jupiter.api.BeforeEach;
        import java.io.File;
        import java.io.IOException;

public class EpicsHandlerWithFileBackedTaskManagerTest extends EpicsHandlerTest<FileBackedTaskManager> {
    @BeforeEach
    public void startHttpServerAndSetTaskManagerAndGson() throws IOException {
        File file = File.createTempFile("temp", "temp");
        taskManager = Managers.loadFromFile(file);
        HttpTaskServer.startHttpTaskServer(taskManager);
        gson = HttpTaskServer.getGson();
    }

    @AfterEach
    public void stopHttpServer(){
        HttpTaskServer.stopHttpTaskServer();
    }
}