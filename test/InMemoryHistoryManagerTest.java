import managers.InMemoryTaskManager;
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

    @Test // добавим тест проверяющий что размер не будет больше 10
    public void shouldReturn10Tasks() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task = inMemoryTaskManager.addTask(new Task("name", "descriptions"));
        for (int i = 1; i <= 11; i++){
            inMemoryTaskManager.getTask(task.getId());
        }
        int sizeTasksHistorylist = inMemoryTaskManager.getHistory().size();
        Assertions.assertEquals(10, sizeTasksHistorylist, "Список просмотренных задач больше 10");
    }
}
