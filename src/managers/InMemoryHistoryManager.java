package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    private class Node {
        Node next;
        Node prev;
        Task task;

        public Node(Node next, Node prev, Task task) {
            this.next = next;
            this.prev = prev;
            this.task = task;
        }

        private void deleteNode(){
            if (this.prev == null) {
                head = this.next;
                if (this.next != null) {
                    this.next.prev = null;
                }
            } else {
                this.prev.next = this.next;
            }
            if (this.next == null) {
                tail = this.prev;
                if (this.prev != null) {
                    this.prev.next = null;
                }
            } else {
                this.next.prev = this.prev;
            }
        }
    }

    Node head;
    Node tail;
    private final Map<Integer, Node> nodesMap = new HashMap<>();

    @Override
    public void add(Task task){
        addNewNode(getCopyTask(task));
    }

    public void updateHistory(Task task) {
        if (nodesMap.containsKey(task.getId())) {
            nodesMap.get(task.getId()).task = getCopyTask(task);
        }
    }

    public Task getCopyTask(Task task) {
        return task.createCopyTask(task);
    }

    void addNewNode(Task task){
        if (nodesMap.containsKey(task.getId())) {
            nodesMap.get(task.getId()).deleteNode();
            nodesMap.remove(task.getId());
        }
        nodesMap.put(task.getId(),new Node(null, tail, task));
        if (tail != null) {
            tail.next = nodesMap.get(task.getId());
        }
        tail = nodesMap.get(task.getId());
        if (nodesMap.size() == 1) {
            head = tail;
        }
    }

    @Override
    public void remove(int id){
        if (nodesMap.containsKey(id)) {
            nodesMap.remove(id).deleteNode();
        }
    }

    @Override
    public List<Task> getHistory(){
        List<Task> historyViewedTasks = new ArrayList<>();
        Node node = head;

        while (node != null) {
            historyViewedTasks.add(node.task);
            node = node.next;
        }
        return historyViewedTasks;
    }
}
