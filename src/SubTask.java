public class SubTask extends Task{
    private int epicId;

    public SubTask(int id, String name, String description, TaskStatus taskStatus, int epicId){
        super(id, name, description, taskStatus);
        this.epicId = epicId;
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
}
