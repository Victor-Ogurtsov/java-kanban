package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> historyViewedTasks = new ArrayList<>();

    @Override
    public void add(Task task){
        if (task instanceof SubTask) {
            SubTask savesTask = (SubTask) ((SubTask) task).createCopyTask(task);
            historyViewedTasks.add(savesTask);
        } else if (task instanceof Epic) {
            Epic savesTask = (Epic) ((Epic) task).createCopyTask(task);
            historyViewedTasks.add(savesTask);
        } else {
            Task savesTask = task.createCopyTask(task);
            historyViewedTasks.add(savesTask);
        }

        if (historyViewedTasks.size() == 11){
            historyViewedTasks.remove(0);
        }
    }

    @Override
    public ArrayList<Task> getHistory(){
        return historyViewedTasks;
    }
}
