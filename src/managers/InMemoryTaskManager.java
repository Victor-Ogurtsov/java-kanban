package managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import tasks.*;

public class InMemoryTaskManager implements TaskManager {
    protected static int id = 1;

    protected final Map<Integer, Task> tasks;
    protected final Map<Integer, Epic> epics;
    protected final Map<Integer, SubTask> subTasks;
    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    protected final TreeSet<Task> tasksByPriority = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    public InMemoryTaskManager(){
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subTasks = new HashMap<>();
    }
    @Override
    public Task addTask(Task task){
        if(tasksByPriority.stream().anyMatch(task1 -> checkIntersectionDurationTask(task1, task))) {
            return null;
        }
        task.setId(id++);
        tasks.put(task.getId(), task);
        tasksByPriority.add(task);
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
        if(tasksByPriority.stream().anyMatch(task -> checkIntersectionDurationTask(task, subTask))) {
            return null;
        }
        subTask.setId(id++);
        subTask.setEpicId(epicId);
        subTasks.put(subTask.getId(), subTask);
        epics.get(epicId).setSubTaskId(subTask.getId());
        setActualStartTimeEpic(epics.get(epicId));
        setActualDurationEpic(epics.get(epicId));
        setActualEndTimeEpic(epics.get(epicId));
        tasksByPriority.add(subTask);
        return subTask;
    }

    @Override
    public void setActualTaskStatusEpic(Epic epic){
        if (epic.getSizeSubTasksIdList() == epic.getSubTasksId().stream()
                .filter(id -> subTasks.get(id).getTaskStatus() == TaskStatus.NEW)
                .count()) {
            epic.setTaskStatus(TaskStatus.NEW);
        } else if (epic.getSizeSubTasksIdList() == epic.getSubTasksId().stream()
                    .filter(id -> subTasks.get(id).getTaskStatus() == TaskStatus.DONE)
                    .count()) {
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
        if(tasksByPriority.stream().anyMatch(task1 -> checkIntersectionDurationTask(task1, task))) {
            return;
        }
        tasks.put(task.getId(), task);
        ((InMemoryHistoryManager) historyManager).updateHistory(task);
        tasksByPriority.remove(tasks.get(task.getId()));
        tasksByPriority.add(task);
    }

    @Override
    public void updateEpic(Epic epic){
        if (epics.get(epic.getId()) == null) {
            return;
        }
        epics.put(epic.getId(), epic);
        ((InMemoryHistoryManager) historyManager).updateHistory(epic);
    }

    @Override
    public void updateSubTask(SubTask subTask){
        if (subTasks.get(subTask.getId()) == null) {
            return;
        }
        if(tasksByPriority.stream().anyMatch(task -> checkIntersectionDurationTask(task, subTask))) {
            return;
        }
        subTasks.put(subTask.getId(), subTask);
        setActualParametersEpic(epics.get(subTask.getEpicId()));
        ((InMemoryHistoryManager) historyManager).updateHistory(subTask);
        ((InMemoryHistoryManager) historyManager).updateHistory(epics.get(subTask.getEpicId()));
        tasksByPriority.remove(subTasks.get(subTask.getId()));
        tasksByPriority.add(subTask);
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
        return  epics.get(epicId).getSubTasksId().stream()
                .map(subTasks::get)
                .toList()
                .toString();
    }

    @Override
    public void removeTask(int taskId){
        tasksByPriority.remove(tasks.get(taskId));
        tasks.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void removeEpic(int epicId){
        ArrayList<Integer> subTasksId = epics.get(epicId).getSubTasksId();
        for (Integer id : subTasksId){
            tasksByPriority.remove(subTasks.get(id));
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
        tasksByPriority.remove(subTasks.get(subTaskId));
        subTasks.remove(subTaskId);
        historyManager.remove(subTaskId);
        setActualParametersEpic(epics.get(epicId));
        ((InMemoryHistoryManager) historyManager).updateHistory(epics.get(epicId));
    }

    @Override
    public void removeAllTasks(){
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
            tasksByPriority.remove(task);
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
            tasksByPriority.remove(subTask);
        }
        subTasks.clear();
    }

    @Override
    public void removeAllSubTasks(){
        for (SubTask subTask : subTasks.values()) {
            historyManager.remove(subTask.getId());
            tasksByPriority.remove(subTask);
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
        tasksByPriority.clear();
    }

    @Override
    public List<Task> getHistory(){
        return historyManager.getHistory();
    }

    @Override
    public void setActualStartTimeEpic(Epic epic){
        epic.setStartTime(epic.getSubTasksId().stream()
                .map(id -> subTasks.get(id).getStartTime())
                .min(LocalDateTime::compareTo).orElse(null)
        );
    }

    @Override
    public void setActualEndTimeEpic(Epic epic){
        epic.setEndTime(epic.getSubTasksId().stream()
                .map(id -> subTasks.get(id).getEndTime())
                .max(LocalDateTime::compareTo).orElse(null)
        );
    }

    @Override
    public void setActualDurationEpic(Epic epic){
        epic.setDuration(Duration.ofMinutes(epic.getSubTasksId().stream()
                        .map(id -> subTasks.get(id).getDuration().toMinutes())
                        .mapToLong(Long::intValue)
                        .sum()
                )
        );
    }

    @Override
    public List<Task> getPrioritizedTasks(){
        return tasksByPriority.stream().toList();
    }

    @Override
    public boolean checkIntersectionDurationTask(Task task1,Task task2){
        return task1.getStartTime().isAfter(task2.getStartTime()) && task1.getStartTime().isBefore(task2.getEndTime()) ||
                task1.getStartTime().isBefore(task2.getStartTime()) && task1.getEndTime().isAfter(task2.getStartTime())
                || task1.getStartTime().isEqual(task2.getStartTime()) || task1.getEndTime().isEqual(task2.getEndTime());
    }

    @Override
    public void setActualParametersEpic(Epic epic){
        setActualTaskStatusEpic(epic);
        setActualStartTimeEpic(epic);
        setActualDurationEpic(epic);
        setActualEndTimeEpic(epic);
    }
}
