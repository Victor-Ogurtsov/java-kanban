package managers;

import java.util.ArrayList;
import java.util.HashMap;
import tasks.*;

public class TaskManager {
    private static int id = 1;

    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, SubTask> subTasks;

    public TaskManager(){
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subTasks = new HashMap<>();
    }
    public Task addTask(Task task){
        task.setId(id++);
        tasks.put(task.getId(), task);
        return task;
    }

    public Task addEpic(Epic epic){
        epic.setId(id++);
        epics.put(epic.getId(), epic);
        return epic;
    }

    public SubTask addSubTask(int epicId, SubTask subTask){
        subTask.setId(id++);
        subTask.setEpicId(epicId);
        subTasks.put(subTask.getId(), subTask);
        epics.get(epicId).setSubTaskId(subTask.getId());
        return subTask;
    }

    private void setActualTaskStatusEpic(Epic epic){
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

    public ArrayList<Task> getAllTasks(){
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getAllEpics(){
        return new ArrayList<>(epics.values());
    }

    public ArrayList<SubTask> getAllSubTasks(){
        return new ArrayList<>(subTasks.values());
    }

    public void updateTask(Task task){
        if (tasks.get(task.getId()) == null) {
            return;
        }
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic){
        if (epics.get(epic.getId()) == null) {
            return;
        }
        epics.put(epic.getId(), epic);
    }

    public void updateSubTask(SubTask subTask){
        if (subTasks.get(subTask.getId()) == null) {
            return;
        }
        subTasks.put(subTask.getId(), subTask);
        setActualTaskStatusEpic(epics.get(subTask.getEpicId()));
    }

    public Task getTask(int id){
        return tasks.get(id);
    }

    public Epic getEpic(int id){
        return epics.get(id);
    }

    public SubTask getSubtask(int id){
        return subTasks.get(id);
    }

    public String getAllSubTaskOfEpic(int epicId){
        ArrayList<Integer> subTasksIdList = epics.get(epicId).getSubTasksId();
        ArrayList<SubTask> subTasksOfEpic = new ArrayList<>();
        for(Integer id : subTasksIdList){
            subTasksOfEpic.add(subTasks.get(id));
        }
        return subTasksOfEpic.toString();
    }

    public void removeTask (int taskId){
        tasks.remove(taskId);
    }

    public void removeEpic (int epicId){
        ArrayList<Integer> subTasksId= epics.get(epicId).getSubTasksId();
        for (Integer id : subTasksId){
            subTasks.remove(id);
        }
        epics.remove(epicId);
    }

    public void removeSubTask (Integer subTaskId){
        int epicId = subTasks.get(subTaskId).getEpicId();
        epics.get(epicId).getSubTasksId().remove(subTaskId);
        subTasks.remove(subTaskId);
    }

    public void removeAllTasks(){
        tasks.clear();
    }

    public void removeAllEpics(){
        epics.clear();
    }

    public void removeAllSubTasks(){
        subTasks.clear();
    }

    public void removeAllTasksTypes(){
        tasks.clear();
        epics.clear();
        subTasks.clear();
    }
}
