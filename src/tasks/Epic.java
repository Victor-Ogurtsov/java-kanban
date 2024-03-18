package tasks;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTasksId;
    private LocalDateTime endTime;

    public Epic(String name, String description){
        super(name, description);
        subTasksId = new ArrayList<>();
    }

    public void setSubTaskId(Integer subTaskId) {
        if (subTaskId == id) {
            return;
        }
        subTasksId.add(subTaskId);
    }

    public ArrayList<Integer> getSubTasksId() {
        return subTasksId;
    }

    @Override
    public Task createCopyTask(Task task){
        Epic newEpic = new Epic(task.getName(),task.getDescription());
        newEpic.setId(task.getId());
        newEpic.setTaskStatus(task.getTaskStatus());
        newEpic.setStartTime(task.getStartTime());
        newEpic.setDuration(task.getDuration());
        ArrayList<Integer> subTasksId = ((Epic) task).getSubTasksId();
        ArrayList<Integer> newSubTasksId = new ArrayList<>();
        for(Integer id : subTasksId) {
            newSubTasksId.add(id);
        }
        newEpic.setSubTasksId(newSubTasksId);
        return newEpic;
    }

    @Override
    public String toString() {
        String startT = "null";
        if (startTime != null) {
            startT = startTime.format(formatter);
        }
        String endT = "null";
        if (endTime != null) {
            endT = endTime.format(formatter);
        }
        return "Epic{" +
                "subTasksId=" + subTasksId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus +
                ", startTime=" + startT +
                ", duration=" + duration +
                ", endTime=" + endT +
                '}';
    }

    public int getSizeSubTasksIdList(){
        return subTasksId.size();
    }

    public void setSubTasksId(ArrayList<Integer> subTasksId) {
        this.subTasksId = subTasksId;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
