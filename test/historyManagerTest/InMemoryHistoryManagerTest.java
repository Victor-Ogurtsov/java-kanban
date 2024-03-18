package historyManagerTest;

import managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryHistoryManagerTest extends HistoryManagerTest<InMemoryTaskManager>{

    @BeforeEach
    public void setTaskManager(){
        taskManager = new InMemoryTaskManager();
    }
}
