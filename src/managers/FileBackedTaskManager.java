package managers;
import tasks.*;
import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager {

    final private File file;

    public FileBackedTaskManager(File file) {
        super();
        loadTasksAndHistoryFromFile(file);
        this.file = file;
    }
    public void save() {
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
                bufferedWriter.write(tasksAndHistoryToString());
            } catch (IOException e) {
                throw new ManagerSaveException("Ошибка сохранения");
            }
    }

    public String tasksAndHistoryToString(){
        StringBuilder builder = new StringBuilder("id,type,name,status,description,epic\n");

        for (Task task : tasks.values()) {
            builder.append(FormatterUtil.taskToString(task));
            builder.append("\n");
        }
        for (Epic epic : epics.values()) {
            builder.append(FormatterUtil.taskToString(epic));
            builder.append("\n");
        }
        for (SubTask subTask : subTasks.values()) {
            builder.append(FormatterUtil.taskToString(subTask));
            builder.append("\n");
        }
        for (Task task : historyManager.getHistory()){
            builder.append("History,");
            builder.append(FormatterUtil.taskToString(task));
            builder.append("\n");
        }
        return builder.toString();
    }

    public void loadTask(Task task) {
        if (task instanceof Epic) {
            epics.put(task.getId(),(Epic) task);
        } else if (task instanceof SubTask) {
            subTasks.put(task.getId(), (SubTask) task);
            epics.get(((SubTask) task).getEpicId()).getSubTasksId().add(task.getId());
            tasksByPriority.add(task);
        } else {
            tasks.put(task.getId(), task);
            tasksByPriority.add(task);
        }
        if (id <= task.getId()) {
            id = task.getId() + 1;
        }
    }

    public void loadTasksAndHistoryFromFile(File file) {

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (line.startsWith("History")){
                    Task task = FormatterUtil.taskFromString(line.substring(8));
                    historyManager.add(task);
                } else if (line.contains("TASK") || line.contains("EPIC") || line.contains("SUBTASK")) {
                    Task task = FormatterUtil.taskFromString(line);
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
