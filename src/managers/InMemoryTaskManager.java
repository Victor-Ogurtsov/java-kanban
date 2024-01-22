package managers;

import java.util.ArrayList;
import java.util.HashMap;
import tasks.*;

public class InMemoryTaskManager implements TaskManager {
    private static int id = 1;

    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;
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
    public ArrayList<Task> getAllTasks(){
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics(){
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks(){
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
        if (tasks.containsKey(id) == false){
            return null;
        } else {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        }
    }

    @Override
    public Epic getEpic(int id){
        if (epics.containsKey(id) == false){
            return null;
        } else {
            historyManager.add(epics.get(id));
            return epics.get(id);
        }
    }

    @Override
    public SubTask getSubtask(int id){
        if (subTasks.containsKey(id) == false){
            return null;
        } else {
            historyManager.add(subTasks.get(id));
            return subTasks.get(id);
        }
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
    }

    @Override
    public void removeEpic(int epicId){
        ArrayList<Integer> subTasksId= epics.get(epicId).getSubTasksId();
        for (Integer id : subTasksId){
            subTasks.remove(id);
        }
        epics.remove(epicId);
    }

    @Override
    public void removeSubTask(Integer subTaskId){
        int epicId = subTasks.get(subTaskId).getEpicId();
        epics.get(epicId).getSubTasksId().remove(subTaskId);
        subTasks.remove(subTaskId);
        setActualTaskStatusEpic(epics.get(epicId));
    }

    @Override
    public void removeAllTasks(){
        tasks.clear();
    }

    @Override
    public void removeAllEpics(){
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void removeAllSubTasks(){
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
    public ArrayList<Task> getHistory(){
        return historyManager.getHistory();
    }
}
