package httpTaskServerTest.epicsHandlerHttpTaskServerTest;

import managers.InMemoryTaskManager;
import managers.Managers;
import org.junit.jupiter.api.BeforeEach;

public class EpicsHandlerWithInMemoryTaskManagerTest extends EpicsHandlerTest<InMemoryTaskManager> {
    @BeforeEach
    public void setTaskManager(){
        taskManager = Managers.getDefault();
    }
}

