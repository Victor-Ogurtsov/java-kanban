import managers.FileBackedTaskManager;
import managers.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.*;

public class FileBackedTaskManagerTest {

    @Test
    public void shouldLoadFromFile() throws IOException {
        File file = File.createTempFile("temp", "temp");
        FileBackedTaskManager fileBackedTaskManager = Managers.loadFromFile(file);

        Task task = fileBackedTaskManager.addTask(new Task("name", "descriptions"));
        Epic epic = fileBackedTaskManager.addEpic(new Epic("name2", "descriptions2"));
        SubTask subTask = fileBackedTaskManager.addSubTask(epic.getId(), new SubTask("name3", "descriptions3"));
        fileBackedTaskManager.getTask(task.getId());
        fileBackedTaskManager.getEpic(epic.getId());
        fileBackedTaskManager.getSubtask(subTask.getId());
        fileBackedTaskManager.removeSubTask(subTask.getId());

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
