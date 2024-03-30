package managers;

import tasks.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class FormatterUtil {

    public static String taskToString(Task task){
        TaskType taskType = TaskType.TASK;
        String epicId = "null";
        String startTime = "null";
        String duration = "null";

        if(task.getStartTime() != null) {
            startTime = task.getStartTime().format(Task.formatter);
        }

        if(task.getDuration() != null) {
            duration = ((Long) task.getDuration().toMinutes()).toString();
        }

        if (task instanceof Epic) {
            taskType = TaskType.EPIC;
        } else if (task instanceof SubTask) {
            taskType = TaskType.SUBTASK;
            epicId = ((Integer)((SubTask) task).getEpicId()).toString();
        }
        return task.getId() + "," + taskType + "," + task.getName() + "," + task.getTaskStatus() + ","
                + task.getDescription() + "," + epicId + "," + startTime + "," + duration;
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
            Task task = new Task(split[2], split[4], LocalDateTime.parse(split[6], Task.formatter), Duration.ofMinutes(Integer.parseInt(split[7])));
            task.setId(Integer.parseInt(split[0]));
            task.setTaskStatus(taskStatus);
            return task;
        } else if (split[1].equals("EPIC")) {
            Epic epic = new Epic(split[2], split[4]);
            epic.setId(Integer.parseInt(split[0]));
            epic.setTaskStatus(taskStatus);
            if (!split[6].equals("null")){
                epic.setStartTime(LocalDateTime.parse(split[6], Task.formatter));
            }
            if (!split[7].equals("null")){
                epic.setDuration(Duration.ofMinutes(Integer.parseInt(split[7])));
            }
            return epic;
        } else if (split[1].equals("SUBTASK")) {
            SubTask subTask = new SubTask(split[2], split[4], LocalDateTime.parse(split[6], Task.formatter), Duration.ofMinutes(Integer.parseInt(split[7])));
            subTask.setId(Integer.parseInt(split[0]));
            subTask.setTaskStatus(taskStatus);
            subTask.setEpicId(Integer.parseInt(split[5]));
            return subTask;
        }
        return null;
    }
}
