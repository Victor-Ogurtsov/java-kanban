package managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import tasks.*;

public class InMemoryTaskManager implements TaskManager {
    protected int id = 1;

    protected final Map<Integer, Task> tasks;
    protected final Map<Integer, Epic> epics;
    protected final Map<Integer, SubTask> subTasks;
    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    public InMemoryTaskManager(){
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subTasks = new HashMap<>();
    }
    @Override
    public Task addTask(Task task) {
        prioritizedTasks.forEach(task1 -> checkIntersectionDurationTask(task1, task));
        task.setId(id++);
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
        return task;
    }

    @Override
    public Epic addEpic(Epic epic){
        epic.setId(id++);
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public SubTask addSubTask(int epicId, SubTask subTask) {
        if (epics.get(epicId) == null) {
            return null;
        }
        prioritizedTasks.forEach(task -> checkIntersectionDurationTask(task, subTask));
        subTask.setId(id++);
        subTask.setEpicId(epicId);
        subTasks.put(subTask.getId(), subTask);
        epics.get(epicId).setSubTaskId(subTask.getId());
        setActualStartTimeEpic(epics.get(epicId));
        setActualDurationEpic(epics.get(epicId));
        setActualEndTimeEpic(epics.get(epicId));
        prioritizedTasks.add(subTask);
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
    public void updateTask(Task task) {
        if (tasks.get(task.getId()) == null) {
            return;
        }
        prioritizedTasks.stream().filter(task1 -> task1.getId() != task.getId()).forEach(task1 -> checkIntersectionDurationTask(task1, task));
        tasks.put(task.getId(), task);
        ((InMemoryHistoryManager) historyManager).updateHistory(task);
        prioritizedTasks.remove(tasks.get(task.getId()));
        prioritizedTasks.add(task);
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
    public void updateSubTask(SubTask subTask) {
        if (subTasks.get(subTask.getId()) == null) {
            return;
        }
        prioritizedTasks.stream().filter(task -> task.getId() != subTask.getId()).forEach(task -> checkIntersectionDurationTask(task, subTask));
        subTasks.put(subTask.getId(), subTask);
        setActualParametersEpic(epics.get(subTask.getEpicId()));
        ((InMemoryHistoryManager) historyManager).updateHistory(subTask);
        ((InMemoryHistoryManager) historyManager).updateHistory(epics.get(subTask.getEpicId()));
        prioritizedTasks.remove(subTasks.get(subTask.getId()));
        prioritizedTasks.add(subTask);
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
    public List<SubTask> getAllSubTaskOfEpic(int epicId){
        return  epics.get(epicId).getSubTasksId().stream()
                .map(subTasks::get)
                .toList();
    }

    @Override
    public void removeTask(int taskId){
        prioritizedTasks.remove(tasks.get(taskId));
        tasks.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void removeEpic(int epicId){
        ArrayList<Integer> subTasksId = epics.get(epicId).getSubTasksId();
        for (Integer id : subTasksId){
            prioritizedTasks.remove(subTasks.get(id));
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
        prioritizedTasks.remove(subTasks.get(subTaskId));
        subTasks.remove(subTaskId);
        historyManager.remove(subTaskId);
        setActualParametersEpic(epics.get(epicId));
        ((InMemoryHistoryManager) historyManager).updateHistory(epics.get(epicId));
    }

    @Override
    public void removeAllTasks(){
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
            prioritizedTasks.remove(task);
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
            prioritizedTasks.remove(subTask);
        }
        subTasks.clear();
    }

    @Override
    public void removeAllSubTasks(){
        for (SubTask subTask : subTasks.values()) {
            historyManager.remove(subTask.getId());
            prioritizedTasks.remove(subTask);
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
        prioritizedTasks.clear();
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
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public void checkIntersectionDurationTask(Task task1,Task task2) {
        boolean isIntersect = task1.getStartTime().isAfter(task2.getStartTime()) && task1.getStartTime().isBefore(task2.getEndTime()) ||
                task1.getStartTime().isBefore(task2.getStartTime()) && task1.getEndTime().isAfter(task2.getStartTime())
                || task1.getStartTime().isEqual(task2.getStartTime()) || task1.getEndTime().isEqual(task2.getEndTime());
        if(isIntersect) {
            throw new IntersectDurationTaskException("Пересечение задач по времени выполнения");
        }
    }



    @Override
    public void setActualParametersEpic(Epic epic){
        setActualTaskStatusEpic(epic);
        setActualStartTimeEpic(epic);
        setActualDurationEpic(epic);
        setActualEndTimeEpic(epic);
    }
}
