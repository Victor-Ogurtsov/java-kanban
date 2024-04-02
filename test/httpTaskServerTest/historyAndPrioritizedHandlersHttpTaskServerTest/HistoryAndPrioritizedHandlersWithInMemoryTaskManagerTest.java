package httpTaskServerTest.historyAndPrioritizedHandlersHttpTaskServerTest;

import managers.InMemoryTaskManager;
import managers.Managers;
import org.junit.jupiter.api.BeforeEach;

public class HistoryAndPrioritizedHandlersWithInMemoryTaskManagerTest extends HistoryAndPrioritizedHandlersTest<InMemoryTaskManager>{
    @BeforeEach
    public void setTaskManager(){
        taskManager = Managers.getDefault();
    }
}
