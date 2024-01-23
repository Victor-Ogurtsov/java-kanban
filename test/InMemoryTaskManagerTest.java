import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.*;

public class InMemoryTaskManagerTest {


    //проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
    @Test
    public void shouldAddTaskAndReturnsTaskById(){
        TaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task = inMemoryTaskManager.addTask(new Task("name", "descriptions"));
        Assertions.assertNotNull(task, "Задача не добавлена");
        Assertions.assertNotNull(inMemoryTaskManager.getTask(task.getId()), "Задача не возвращается по id");
    }

    @Test
    public void shouldAddEpicAndReturnsEpicById(){
        TaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Epic epic = inMemoryTaskManager.addEpic(new Epic("name", "descriptions"));
        Assertions.assertNotNull(epic, "Epic не добавлен в InMemoryTaskManager");
        Assertions.assertNotNull(inMemoryTaskManager.getEpic(epic.getId()), "Epic не возвращается по id");
    }

    @Test
    public void shouldAddSubTaskAndReturnsSubTaskById(){
        TaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Epic epic = inMemoryTaskManager.addEpic(new Epic("name", "descriptions"));
        SubTask subTask = inMemoryTaskManager.addSubTask(epic.getId(),new SubTask("name", "descriptions"));
        Assertions.assertNotNull(subTask, "SubTask не добавлен");
        Assertions.assertNotNull(inMemoryTaskManager.getSubtask(subTask.getId()), "SubTask не возвращается по id");
    }
}
