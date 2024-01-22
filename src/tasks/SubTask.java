package tasks;

import java.util.ArrayList;

public class SubTask extends Task{
   private int epicId;

    public SubTask( String name, String description){
        super(name, description);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus +
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
        SubTask newSubTask = new SubTask(task.getName(), task.getDescription());
        newSubTask.setId(task.getId());
        newSubTask.setTaskStatus(task.getTaskStatus());
        newSubTask.setEpicId(((SubTask) task).getEpicId());
        return newSubTask;
    }
}
