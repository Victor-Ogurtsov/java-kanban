package managers;

import tasks.*;
import java.util.List;

public interface TaskManager {
    Task addTask(Task task);

    Epic addEpic(Epic epic);

    SubTask addSubTask(int epicId, SubTask subTask);

    void setActualTaskStatusEpic(Epic epic);

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<SubTask> getAllSubTasks();

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    Task getTask(int id);

    Epic getEpic(int id);

    SubTask getSubtask(int id);

    List<SubTask> getAllSubTaskOfEpic(int epicId);

    void removeTask(int taskId);

    void removeEpic(int epicId);

    void removeSubTask(Integer subTaskId);

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubTasks();

    void removeAllTasksTypes();

    List<Task> getHistory();

    void setActualStartTimeEpic(Epic epic);

    void setActualEndTimeEpic(Epic epic);

    void setActualDurationEpic(Epic epic);

    List<Task> getPrioritizedTasks();

    void checkIntersectionDurationTask(Task task1,Task task2);

    void setActualParametersEpic(Epic epic);
}
