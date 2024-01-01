package tasks;
import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTasksId;

    public Epic( String name, String description){
        super(name, description);
        subTasksId = new ArrayList<>();
    }

    public void setSubTaskId(Integer subTaskId) {
        subTasksId.add(subTaskId);
    }

    public ArrayList<Integer> getSubTasksId() {
        return subTasksId;
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
}
