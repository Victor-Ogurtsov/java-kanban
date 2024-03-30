package historyManagerTest;

import managers.FileBackedTaskManager;
import org.junit.jupiter.api.BeforeEach;
import java.io.File;
import java.io.IOException;

public class FileBackedHistoryManagerTest extends HistoryManagerTest<FileBackedTaskManager>{

    File file = File.createTempFile("temp", "temp");

    public FileBackedHistoryManagerTest() throws IOException {
    }

    @BeforeEach
    public void setTaskManager(){
        taskManager = new FileBackedTaskManager(file);
    }
}
