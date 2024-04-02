package httpTaskServerTest.subTasksHandlerHttpTaskServerTest;

import managers.InMemoryTaskManager;
import managers.Managers;
import org.junit.jupiter.api.BeforeEach;

public class SubTasksHandlerWithInMemoryTaskManagerTest extends SubTasksHandlerTest<InMemoryTaskManager> {
    @BeforeEach
    public void setTaskManager(){
        taskManager = Managers.getDefault();
    }
}
