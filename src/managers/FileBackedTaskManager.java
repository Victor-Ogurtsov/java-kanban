package managers;
import tasks.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    final private File file;

    public FileBackedTaskManager(File file) {
        super();
        loadTasksAndHistoryFromFile(file);
        this.file = file;
    }
    public void save() {
        try {
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
                bufferedWriter.write(tasksAndHistoryToString());
            } catch (IOException e) {
                throw new ManagerSaveException("Ошибка сохранения");
            }
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    public String taskToString(Task task){
        TaskType taskType = TaskType.TASK;
        int epicId = -1;
        if (task instanceof Epic) {
            taskType = TaskType.EPIC;
        } else if (task instanceof SubTask) {
            taskType = TaskType.SUBTASK;
            epicId = ((SubTask) task).getEpicId();
        }
        return task.getId() + "," + taskType + "," + task.getName() + "," + task.getTaskStatus() + ","
                + task.getDescription() + "," + epicId;
    }
    public String tasksAndHistoryToString(){
        StringBuilder builder = new StringBuilder("id,type,name,status,description,epic\n");

        for (Task task : tasks.values()) {
            builder.append(taskToString(task));
            builder.append("\n");
        }
        for (Epic epic : epics.values()) {
            builder.append(taskToString(epic));
            builder.append("\n");
        }
        for (SubTask subTask : subTasks.values()) {
            builder.append(taskToString(subTask));
            builder.append("\n");
        }
        builder.append(historyToString(historyManager));
        return builder.toString();
    }

    public static String historyToString(HistoryManager manager){
        List<Task> taskList = manager.getHistory();
        StringBuilder builder = new StringBuilder("History");
        for(Task task : taskList) {
            builder.append(",");
            builder.append(task.getId());
        }
        return builder.toString();
    }

    public Task taskFromString(String value){
        TaskStatus taskStatus;

        String[] split = value.split(",");
        if (split[3].equals("NEW")) {
            taskStatus = TaskStatus.NEW;
        } else if (split[3].equals("IN_PROGRESS")) {
            taskStatus = TaskStatus.IN_PROGRESS;
        } else {
            taskStatus = TaskStatus.DONE;
        }

        if (split[1].equals("TASK")) {
            Task task = new Task(split[2], split[4]);
            task.setId(Integer.parseInt(split[0]));
            task.setTaskStatus(taskStatus);
            return task;
        } else if (split[1].equals("EPIC")) {
            Epic epic = new Epic(split[2], split[4]);
            epic.setId(Integer.parseInt(split[0]));
            epic.setTaskStatus(taskStatus);
            return epic;
        } else if (split[1].equals("SUBTASK")) {
            SubTask subTask = new SubTask(split[2], split[4]);
            subTask.setId(Integer.parseInt(split[0]));
            subTask.setTaskStatus(taskStatus);
            subTask.setEpicId(Integer.parseInt(split[5]));
            return subTask;
        }
        return null;
    }

    public void loadTask(Task task) {
        if (task instanceof Epic) {
            epics.put(task.getId(),(Epic) task);
        } else if (task instanceof SubTask) {
            subTasks.put(task.getId(), (SubTask) task);
            epics.get(((SubTask) task).getEpicId()).getSubTasksId().add(task.getId());
        } else {
            tasks.put(task.getId(), task);
        }
        if (id <= task.getId()) {
            id = task.getId() + 1;
        }
    }

    static List<Integer> historyFromString(String value){
        List<Integer> hystoryList = new ArrayList<>();
        String[] split = value.split(",");
        for (int i = 1; i < split.length; i++) {
            hystoryList.add(Integer.parseInt(split[i]));
        }
        return hystoryList;
    }

    public void loadHistory(List<Integer> hystoryList) {
        for (Integer id : hystoryList) {
            if (tasks.containsKey(id)) {
                historyManager.add(tasks.get(id));
            } else if (epics.containsKey(id)) {
                historyManager.add(epics.get(id));
            } else if (subTasks.containsKey(id)) {
                historyManager.add(subTasks.get(id));
            }
        }
    }

    public void loadTasksAndHistoryFromFile(File file) {

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (line.contains("History")){
                    List<Integer> idHystoryList = historyFromString(line);
                    loadHistory(idHystoryList);
                } else if (line.contains("TASK") || line.contains("EPIC") || line.contains("SUBTASK")) {
                    Task task = taskFromString(line);
                    loadTask(task);
                }
            }
        } catch (IOException exception) {
            System.out.println("Ошибка загрузки сохранений");
            exception.getStackTrace();
        }
    }

    @Override
    public Task addTask(Task task) {
        Task taskToAdd = super.addTask(task);
        save();
        return taskToAdd;
    }

    @Override
    public Epic addEpic(Epic epic) {
        Epic epicToAdd = super.addEpic(epic);
        save();
        return epicToAdd;
    }

    @Override
    public SubTask addSubTask(int epicId, SubTask subTask) {
        SubTask subTaskToAdd = super.addSubTask(epicId, subTask);
        save();
        return subTaskToAdd;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task taskToGet = super.getTask(id);
        save();
        return taskToGet;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epicToGet = super.getEpic(id);
        save();
        return epicToGet;
    }

    @Override
    public SubTask getSubtask(int id) {
        SubTask subTaskToGet = super.getSubtask(id);
        save();
        return subTaskToGet;
    }

    @Override
    public void removeTask(int taskId) {
        super.removeTask(taskId);
        save();
    }

    @Override
    public void removeEpic(int epicId) {
        super.removeEpic(epicId);
        save();
    }

    @Override
    public void removeSubTask(Integer subTaskId) {
        super.removeSubTask(subTaskId);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();
        save();
    }

    @Override
    public void removeAllTasksTypes() {
        super.removeAllTasksTypes();
        save();
    }
}
