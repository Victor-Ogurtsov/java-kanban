package managers;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import tasks.*;

public class InMemoryTaskManager implements TaskManager {
    private static int id = 1;

    private final Map<Integer, Task> tasks;
    private final Map<Integer, Epic> epics;
    private final Map<Integer, SubTask> subTasks;
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    public InMemoryTaskManager(){
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subTasks = new HashMap<>();
    }
    @Override
    public Task addTask(Task task){
        task.setId(id++);
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic addEpic(Epic epic){
        epic.setId(id++);
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public SubTask addSubTask(int epicId, SubTask subTask){
        if (epics.get(epicId) == null) {
            return null;
        }
        subTask.setId(id++);
        subTask.setEpicId(epicId);
        subTasks.put(subTask.getId(), subTask);
        epics.get(epicId).setSubTaskId(subTask.getId());
        return subTask;
    }

    @Override
    public void setActualTaskStatusEpic(Epic epic){
        int n = 0;
        int d = 0;

        for (Integer id : epic.getSubTasksId()) {
            if (subTasks.get(id).getTaskStatus() == TaskStatus.NEW) {
                n++;
            } else if (subTasks.get(id).getTaskStatus() == TaskStatus.DONE) {
                d++;
            }
        }

        if (n == epic.getSizeSubTasksIdList()) {
            epic.setTaskStatus(TaskStatus.NEW);
        } else if (d == epic.getSizeSubTasksIdList()) {
            epic.setTaskStatus(TaskStatus.DONE);
        } else {
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getAllTasks(){
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics(){
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getAllSubTasks(){
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void updateTask(Task task){
        if (tasks.get(task.getId()) == null) {
            return;
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic){
        if (epics.get(epic.getId()) == null) {
            return;
        }
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubTask(SubTask subTask){
        if (subTasks.get(subTask.getId()) == null) {
            return;
        }
        subTasks.put(subTask.getId(), subTask);
        setActualTaskStatusEpic(epics.get(subTask.getEpicId()));
    }

    @Override
    public Task getTask(int id){
        Task task = tasks.get(id);
        if (task != null){
            historyManager.add(tasks.get(id));
        }
        return task;
    }

    @Override
    public Epic getEpic(int id){
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epics.get(id));
        }
        return epic;
    }

    @Override
    public SubTask getSubtask(int id){
        SubTask subTask = subTasks.get(id);
        if (subTask != null){
            historyManager.add(subTasks.get(id));
        }
        return subTask;
    }

    @Override
    public String getAllSubTaskOfEpic(int epicId){
        ArrayList<Integer> subTasksIdList = epics.get(epicId).getSubTasksId();
        ArrayList<SubTask> subTasksOfEpic = new ArrayList<>();
        for(Integer id : subTasksIdList){
            subTasksOfEpic.add(subTasks.get(id));
        }
        return subTasksOfEpic.toString();
    }

    @Override
    public void removeTask(int taskId){
        tasks.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void removeEpic(int epicId){
        ArrayList<Integer> subTasksId = epics.get(epicId).getSubTasksId();
        for (Integer id : subTasksId){
            subTasks.remove(id);
            historyManager.remove(id);
        }
        epics.remove(epicId);
        historyManager.remove(epicId);
    }

    @Override
    public void removeSubTask(Integer subTaskId){
        int epicId = subTasks.get(subTaskId).getEpicId();
        epics.get(epicId).getSubTasksId().remove(subTaskId);
        subTasks.remove(subTaskId);
        historyManager.remove(subTaskId);
        setActualTaskStatusEpic(epics.get(epicId));
    }

    @Override
    public void removeAllTasks(){
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
        }
        tasks.clear();
    }

    @Override
    public void removeAllEpics(){
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getId());
        }
        epics.clear();
        for (SubTask subTask : subTasks.values()) {
            historyManager.remove(subTask.getId());
        }
        subTasks.clear();
    }

    @Override
    public void removeAllSubTasks(){
        for (SubTask subTask : subTasks.values()) {
            historyManager.remove(subTask.getId());
        }
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubTasksId().clear();
            setActualTaskStatusEpic(epic);
        }
    }

    @Override
    public void removeAllTasksTypes(){
        tasks.clear();
        epics.clear();
        subTasks.clear();
    }

    @Override
    public List<Task> getHistory(){
        return historyManager.getHistory();
    }
}
