package managers;

import tasks.*;

import java.util.ArrayList;

public interface TaskManager {
    Task addTask(Task task);

    Epic addEpic(Epic epic);

    SubTask addSubTask(int epicId, SubTask subTask);

    void setActualTaskStatusEpic(Epic epic);

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<SubTask> getAllSubTasks();

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    Task getTask(int id);

    Epic getEpic(int id);

    SubTask getSubtask(int id);

    String getAllSubTaskOfEpic(int epicId);

    void removeTask(int taskId);

    void removeEpic(int epicId);

    void removeSubTask(Integer subTaskId);

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubTasks();

    void removeAllTasksTypes();

    ArrayList<Task> getHistory();
}
