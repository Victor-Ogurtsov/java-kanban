import managers.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManagerTest {

    @Test // убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
    public void shouldReturnEqualsTask() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task = inMemoryTaskManager.addTask(new Task("name1", "descriptions1"));
        Task newTask = inMemoryTaskManager.getTask(task.getId());
        ArrayList<Task> tasksHistory = inMemoryTaskManager.getHistory();
        Assertions.assertTrue(tasksHistory.contains(task), "Задача полученная методом getTask(Task task) не сохранена в InMemoryHistoryManager");
        newTask.setDescription("descriptions2");
        inMemoryTaskManager.updateTask(newTask);
        Assertions.assertFalse(tasksHistory.contains(newTask), "После изменеиня задачи в inMemoryTaskManager задача изменилась в InMemoryHistoryManager");
    }
}
