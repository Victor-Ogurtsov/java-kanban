package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> historyViewedTasks = new ArrayList<>();

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

        if (historyViewedTasks.size() > 10){
            historyViewedTasks.remove(0);
        }
    }

    @Override
    public List<Task> getHistory(){
        return historyViewedTasks;
    }
}
