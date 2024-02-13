import managers.HistoryManager;
import managers.InMemoryTaskManager;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Task;
import java.util.List;


public class InMemoryHistoryManagerTest {

    @Test // убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
    public void shouldReturnEqualsTask() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task = inMemoryTaskManager.addTask(new Task("name1", "descriptions1"));

        Task newTask = inMemoryTaskManager.getTask(task.getId());
        List<Task> tasksHistory = inMemoryTaskManager.getHistory();

        Assertions.assertTrue(tasksHistory.contains(task), "Задача полученная методом getTask(Task task) не сохранена в InMemoryHistoryManager");
        newTask.setDescription("descriptions2");
        inMemoryTaskManager.updateTask(newTask);
        Assertions.assertFalse(tasksHistory.contains(newTask), "После изменеиня задачи в inMemoryTaskManager задача изменилась в InMemoryHistoryManager");
    }

    @Test
    public void shouldReturnListWithOneInstanceTaskAndTheLastPlace() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        Task task1 = inMemoryTaskManager.addTask(new Task("name1", "descriptions1"));
        Task task2 = inMemoryTaskManager.addTask(new Task("name2", "descriptions2"));

        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getTask(task2.getId());
        inMemoryTaskManager.getTask(task1.getId());

        Assertions.assertEquals(2, inMemoryTaskManager.getHistory().size(), "Список истории просмотров" +
                " содержит не правильное количество элементов");
        Assertions.assertEquals(task1, inMemoryTaskManager.getHistory().get(1), "Задача не отображается последней в списке");
    }

    @Test
    public void shouldEqualZeroWhenRemoveTask(){
        TaskManager inMemoryTaskManager = Managers.getDefault();
        Task task1 = inMemoryTaskManager.addTask(new Task("name1", "descriptions1"));

        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.removeTask(task1.getId());

        Assertions.assertEquals(0, inMemoryTaskManager.getHistory().size(), "Список с историей задач не пуст");
    }
}
