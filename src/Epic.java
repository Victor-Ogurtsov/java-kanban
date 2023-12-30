import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTasksId;

    Epic(int id, String name, String description, TaskStatus taskStatus){
        super(id, name, description, taskStatus);
        subTasksId = new ArrayList<>();
    }

    public void setSubTask(Integer subTaskId) {
        subTasksId.add(subTaskId);
    }

    public ArrayList<Integer> getSubTasksId() {
        return subTasksId;
    }

    public void setSubTasksId(ArrayList<Integer> subTasksId) {
        this.subTasksId = subTasksId;
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

    void removeIdSubTasks(Integer subTaskId){
        subTasksId.remove(subTaskId);
    }

    void addIdInSubTasksList(int id){
        subTasksId.add(id);
    }

    int getSizeSubTasksIdList(){
        return subTasksId.size();
    }
}
