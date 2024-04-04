
import managers.IntersectDurationTaskException;
import managers.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    @Test
    public void shouldAddTaskAndReturnsTaskById(){
        Task task = taskManager.addTask(new Task("name", "descriptions", LocalDateTime.now(), Duration.ofMinutes(10)));
        Assertions.assertNotNull(task, "Задача не добавлена");
        Assertions.assertNotNull(taskManager.getTask(task.getId()), "Задача не возвращается по id");
    }

    @Test
    public void shouldAddEpicAndReturnsEpicById(){
        Epic epic = taskManager.addEpic(new Epic("name", "descriptions"));
        Assertions.assertNotNull(epic, "Epic не добавлен в InMemoryTaskManager");
        Assertions.assertNotNull(taskManager.getEpic(epic.getId()), "Epic не возвращается по id");
    }

    @Test
    public void shouldAddSubTaskAndReturnsSubTaskById(){
        Epic epic = taskManager.addEpic(new Epic("name", "descriptions"));
        SubTask subTask = taskManager.addSubTask(epic.getId(),new SubTask("name", "descriptions",
                LocalDateTime.now(), Duration.ofMinutes(10)));
        Assertions.assertNotNull(subTask, "SubTask не добавлен");
        Assertions.assertNotNull(taskManager.getSubtask(subTask.getId()), "SubTask не возвращается по id");
    }

    @Test //проверьте, что объект Subtask нельзя сделать своим же эпиком;
    public void shouldReturnNullIfEpicIdEqualsSubTaskId() {
        SubTask subTask = new SubTask("name", "descriptions", LocalDateTime.now(), Duration.ofMinutes(10));
        subTask.setId(1);
        SubTask addedSubTask = taskManager.addSubTask(1, subTask);
        Assertions.assertNull(addedSubTask,"В список подзадач занесена подзадача, у которой" +
                "id занесено в поле для ее epicId");
    }

    @Test
    public void shouldEqualZeroIfRemoveSubTask(){
        Epic epic = taskManager.addEpic(new Epic("name1", "descriptions1"));
        SubTask subTask = taskManager.addSubTask(epic.getId(), new SubTask("name2", "descriptions2",
                LocalDateTime.now(), Duration.ofMinutes(10)));

        Assertions.assertTrue(epic.getSubTasksId().contains(subTask.getId()), "Подзадачи нет в списке подзадач эпика");

        taskManager.removeSubTask(subTask.getId());

        Assertions.assertEquals(0,epic.getSubTasksId().size(), "Подзадача осталась в списке подзадач эпика" +
                "после удаления");
    }

    @Test
    public void shouldThrowExceptionThenAddTaskWithIntersectionDuration(){
        Task task1 = taskManager.addTask(new Task("name1", "descriptions1", LocalDateTime.now(), Duration.ofMinutes(10)));
        Task task2 = new Task("name2", "descriptions2", LocalDateTime.now(), Duration.ofMinutes(10));
        Task task3 = new Task("name2", "descriptions2", LocalDateTime.now().plusMinutes(5),
                Duration.ofMinutes(10));

        Assertions.assertThrows(IntersectDurationTaskException.class, () -> taskManager.addTask(task2),
                "При добавлении задачи начинающейся в тоже время, что и предыдущая не выбрасывается исключение");
        Assertions.assertThrows(IntersectDurationTaskException.class, () -> taskManager.addTask(task3),
                "При добавлении задачи с пересечением по времени выполнения не выбрасывается исключение");
    }


    @Test
    public void shouldBePrioritized(){
        Task task1 = taskManager.addTask(new Task("name1", "descriptions1", LocalDateTime.now().plusMinutes(30), Duration.ofMinutes(10)));
        Task task2 = taskManager.addTask(new Task("name2", "descriptions2", LocalDateTime.now(), Duration.ofMinutes(10)));
        Task task3 = taskManager.addTask(new Task("name2", "descriptions2", LocalDateTime.now().plusMinutes(60), Duration.ofMinutes(10)));

        Assertions.assertEquals(task2,taskManager.getPrioritizedTasks().get(0), "Задача с более высоким приоритетом не на 0 индексе");
        Assertions.assertEquals(task1,taskManager.getPrioritizedTasks().get(1), "Задача не на 1 индексе");
        Assertions.assertEquals(task3,taskManager.getPrioritizedTasks().get(2), "Задача с более низким приоритетом не на 2 индексе");
        taskManager.removeTask(task2.getId());
        Assertions.assertEquals(task1, taskManager.getPrioritizedTasks().get(0),"После удаления задачи с самым высоким приоритетом" +
                "следующая задача не на 0 индексе");
    }

    @Test
    public void startTimeAndDurationEpicShouldBeCalculatedBySubTasks(){
        Epic epic = taskManager.addEpic(new Epic("name1", "descriptions1"));
        SubTask subTask1 = taskManager.addSubTask(epic.getId(), new SubTask("name2", "descriptions2",
                LocalDateTime.now().plusMinutes(60), Duration.ofMinutes(10)));
        SubTask subTask2 = taskManager.addSubTask(epic.getId(), new SubTask("name3", "descriptions3",
                LocalDateTime.now(), Duration.ofMinutes(10)));

        Assertions.assertEquals(taskManager.getSubtask(subTask2.getId()).getStartTime(), taskManager.getEpic(epic.getId()).getStartTime(),
                "Время начала Эпика не равно самому раннему времени начала подзадачи");
        Assertions.assertEquals((taskManager.getSubtask(subTask1.getId()).getDuration().toMinutes()
                        + taskManager.getSubtask(subTask2.getId()).getDuration().toMinutes()),
                taskManager.getEpic(epic.getId()).getDuration().toMinutes(),
                "Продолжительность Эпика не равно сумме продолжительности подзадач");
    }

    @Test
    public void taskStatusEpicShouldBeCalculatedBySubTasks(){
        Epic epic = taskManager.addEpic(new Epic("name1", "descriptions1"));
        SubTask subTask1 = taskManager.addSubTask(epic.getId(), new SubTask("name2", "descriptions2",
                LocalDateTime.now().plusMinutes(60), Duration.ofMinutes(10)));
        SubTask subTask2 = taskManager.addSubTask(epic.getId(), new SubTask("name3", "descriptions3",
                LocalDateTime.now(), Duration.ofMinutes(10)));

        Assertions.assertTrue(epic.getTaskStatus() == TaskStatus.NEW, "Статус epic не NEW, когда у двух" +
                "subtask статус NEW");

        subTask1.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(subTask1);
        Assertions.assertTrue(epic.getTaskStatus() == TaskStatus.IN_PROGRESS,"Статус epic не IN_PROGRESS," +
                " когда у оной из двух subtask статус IN_PROGRESS");

        subTask1.setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTask1);
        subTask2.setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTask2);
        Assertions.assertTrue(epic.getTaskStatus() == TaskStatus.DONE, "Статус epic не DONE, когда у двух" +
                "subtask статус DONE");
    }
}


