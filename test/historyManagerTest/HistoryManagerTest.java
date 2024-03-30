package historyManagerTest;
import managers.InMemoryTaskManager;
import managers.IntersectDurationTaskException;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Task;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public abstract class HistoryManagerTest<T extends TaskManager> {

    protected T taskManager;

    @Test // убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
    public void shouldReturnEqualsTask() throws IntersectDurationTaskException {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task = inMemoryTaskManager.addTask(new Task("name1", "descriptions1",
                LocalDateTime.now(), Duration.ofMinutes(10)));

        Task newTask = inMemoryTaskManager.getTask(task.getId());
        List<Task> tasksHistory = inMemoryTaskManager.getHistory();

        Assertions.assertTrue(tasksHistory.contains(task), "Задача полученная методом getTask(Task task) не" +
                " сохранена в InMemoryHistoryManager");
        newTask.setDescription("descriptions2");
        inMemoryTaskManager.updateTask(newTask);
        Assertions.assertFalse(tasksHistory.contains(newTask), "После изменения задачи в inMemoryTaskManager" +
                " задача изменилась в InMemoryHistoryManager");
    }

    @Test
    public void shouldReturnListWithOneInstanceTaskAndTheLastPlace() throws IntersectDurationTaskException {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        Task task1 = inMemoryTaskManager.addTask(new Task("name1", "descriptions1", LocalDateTime.now(),
                Duration.ofMinutes(10)));
        Task task2 = inMemoryTaskManager.addTask(new Task("name2", "descriptions2", LocalDateTime.now()
                .plusHours(1), Duration.ofMinutes(10)));

        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getTask(task2.getId());
        inMemoryTaskManager.getTask(task1.getId());

        Assertions.assertEquals(2, inMemoryTaskManager.getHistory().size(), "Список истории просмотров" +
                " содержит не правильное количество элементов");
        Assertions.assertEquals(task1, inMemoryTaskManager.getHistory().get(1), "Задача не отображается последней в списке");
    }

    @Test
    public void shouldEqualZeroWhenRemoveTask() throws IntersectDurationTaskException {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        Task task1 = inMemoryTaskManager.addTask(new Task("name1", "descriptions1", LocalDateTime.now(),
                Duration.ofMinutes(10)));

        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.removeTask(task1.getId());

        Assertions.assertEquals(0, inMemoryTaskManager.getHistory().size(), "Список с историей задач не пуст");
    }
}
