package httpTaskServerTest.tasksHandlerHttpTaskServerTest;

import managers.InMemoryTaskManager;
import managers.Managers;
import org.junit.jupiter.api.BeforeEach;

public class TasksHandlerWithInMemoryTaskManagerTest extends TasksHandlerTest<InMemoryTaskManager> {
    @BeforeEach
    public void setTaskManager(){
        taskManager = Managers.getDefault();
    }
}



