package tasks;
import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTasksId;

    public Epic( String name, String description){
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
        ArrayList<Integer> subTasksId = ((Epic) task).getSubTasksId();
        ArrayList<Integer> newSubTasksId = new ArrayList<>();
        for(Integer id : subTasksId) {
            newSubTasksId.add(id);
        }
        ((Epic) newEpic).setSubTasksId(newSubTasksId);
        return newEpic;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasksId=" + subTasksId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }

    public int getSizeSubTasksIdList(){
        return subTasksId.size();
    }

    public void setSubTasksId(ArrayList<Integer> subTasksId) {
        this.subTasksId = subTasksId;
    }
}
