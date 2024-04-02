package httpTaskServerTest.tasksHandlerHttpTaskServerTest;

import managers.FileBackedTaskManager;
import managers.Managers;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.IOException;

public class TasksHandlerWithFileBackedTaskManagerTest extends TasksHandlerTest<FileBackedTaskManager> {
    @BeforeEach
    public void setTaskManager() throws IOException {
        File file = File.createTempFile("temp", "temp");
        taskManager = Managers.loadFromFile(file);
    }
}
