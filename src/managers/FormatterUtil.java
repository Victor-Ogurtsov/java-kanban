package managers;

import tasks.*;
import java.util.ArrayList;
import java.util.List;

public class FormatterUtil {

    public static String taskToString(Task task){
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

    public static String historyToString(HistoryManager manager){
        List<Task> taskList = manager.getHistory();
        StringBuilder builder = new StringBuilder("History");
        for(Task task : taskList) {
            builder.append(",");
            builder.append(task.getId());
        }
        return builder.toString();
    }

    public static Task taskFromString(String value){
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

    public static List<Integer> listHistoryFromString(String value){
        List<Integer> hystoryList = new ArrayList<>();
        String[] split = value.split(",");
        for (int i = 1; i < split.length; i++) {
            hystoryList.add(Integer.parseInt(split[i]));
        }
        return hystoryList;
    }
}
