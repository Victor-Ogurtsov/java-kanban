import managers.FileBackedTaskManager;
import managers.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    File file = File.createTempFile("temp", "temp");

    public FileBackedTaskManagerTest() throws IOException {
    }

    @BeforeEach
    public void setOTaskManager() throws IOException {
        taskManager = new FileBackedTaskManager(file);
    }

    @Test
    public void shouldLoadFromFile() throws IOException {

        Task task = taskManager.addTask(new Task("name", "descriptions",
                LocalDateTime.now(), Duration.ofMinutes(10)));
        Epic epic = taskManager.addEpic(new Epic("name2", "descriptions2"));
        SubTask subTask = taskManager.addSubTask(epic.getId(), new SubTask("name3", "descriptions3",
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(10)));
        taskManager.getTask(task.getId());
        taskManager.getEpic(epic.getId());
        taskManager.getSubtask(subTask.getId());
        taskManager.removeSubTask(subTask.getId());

        FileBackedTaskManager fileBackedTaskManager2 = Managers.loadFromFile(file);
        Assertions.assertNotNull(fileBackedTaskManager2.getTask(task.getId()), "task не загрузилась из файла");
        Assertions.assertNotNull(fileBackedTaskManager2.getEpic(epic.getId()), "epic не загрузилась из файла");
        Assertions.assertTrue(fileBackedTaskManager2.getHistory().contains(task), "история просмотра task" +
                " не загрузилась из файла в список");
        Assertions.assertEquals(task, fileBackedTaskManager2.getHistory().get(0), "запись в списке с историей просмотра" +
                " task не на своем месте");
        Assertions.assertTrue(fileBackedTaskManager2.getHistory().contains(epic), "история просмотра epic" +
                " не загрузилась из файла в список");
        Assertions.assertEquals(epic, fileBackedTaskManager2.getHistory().get(1), "запись в списке с историей просмотра" +
                " epic не на своем месте");
        Assertions.assertNull(fileBackedTaskManager2.getSubtask(subTask.getId()), "удаленная subTask загружена" +
                " из файла в историю просмотров");
        Assertions.assertFalse(fileBackedTaskManager2.getHistory().contains(subTask), "история просмотра" +
                " удаленной subTask загружена из файла ");
    }
}
