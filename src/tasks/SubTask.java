package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class SubTask extends Task{
   private int epicId;

    public SubTask(String name, String description, LocalDateTime startTime, Duration duration){
        super(name, description, startTime, duration);
    }

    @Override
    public String toString() {
        String endT = "null";
        if (getEndTime() != null) {
            endT = getEndTime().format(formatter);
        }
        return "SubTask{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus +
                ", startTime=" + startTime.format(formatter) +
                ", duration=" + duration +
                ", endTime=" + endT +
                '}';
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public Task createCopyTask(Task task){
        SubTask newSubTask = new SubTask(task.getName(), task.getDescription(), getStartTime(), getDuration());
        newSubTask.setId(task.getId());
        newSubTask.setTaskStatus(task.getTaskStatus());
        newSubTask.setEpicId(((SubTask) task).getEpicId());
        return newSubTask;
    }
}
