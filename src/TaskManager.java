import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private static int id = 1;

    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, SubTask> subTasks;

    TaskManager(){
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subTasks = new HashMap<>();
    }
    public Task newTask(String name, String description, TaskStatus taskStatus){
        Task task = new Task(id++, name, description, taskStatus);
        tasks.put(task.id, task);
        return task;
    }

    public Task newEpic(String name, String description){
        Epic epic = new Epic(id++, name, description, TaskStatus.NEW); //NEW cтатус для Epic поумолчанию, если нет задач
        epics.put(epic.id, epic);
        return epic;
    }

    public SubTask newSubTask(String name, String description, TaskStatus taskStatus, int epicId){
        SubTask subTask = new SubTask(id, name, description, taskStatus, epicId);
        subTasks.put(subTask.id, subTask);
        epics.get(epicId).setSubTask(id); //Добавляю подзадачу в список-поле соответствующего обьекта Epic
        setActualTaskStatusEpic(epics.get(epicId)); //После создания подзадачи проверяю статус ее Epic
        id++;
        return subTask;
    }

    public void setActualTaskStatusEpic(Epic epic){
        int n = 0;
        int d = 0;

        for (Integer id : epic.getSubTasksId()) {
            if (subTasks.get(id).getTaskStatus() == TaskStatus.NEW) {
                n++;
            } else if (subTasks.get(id).getTaskStatus() == TaskStatus.DONE) {
                d++;
            }
        }

        if (n == epic.getSizeSubTasksIdList()) {
            epic.setTaskStatus(TaskStatus.NEW);
        } else if (d == epic.getSizeSubTasksIdList()) {
            epic.setTaskStatus(TaskStatus.DONE);
        } else {
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }

    public String getAllTasks(){
        String result = "";
        int i = 0;

        if (tasks.isEmpty()){
            result = "Список задач пуст";
        } else {
            for (Task task : tasks.values()) {
                i++;
                if (i == tasks.size()) {
                    result += task.toString();
                } else {
                    result += task.toString() + "\n";
                }
            }
        }
        return result;
    }

    public String getAllEpics(){
        String result = "";
        int i = 0;

        if (epics.isEmpty()){
            result = "Список Epic задач пуст";
        } else {
            for (Epic epic : epics.values()) {
                i++;
                if (i == epics.size()) {
                    result += epic.toString();
                } else {
                    result += epic.toString() + "\n";
                }
            }
        }
        return result;
    }

    public String getAllSubTasks(){
        String result = "";
        int i = 0;

        if (subTasks.isEmpty()){
            result = "Список подзадач пуст";
        } else {
            for (SubTask subTask : subTasks.values()) {
                i++;
                if (i == subTasks.size()) {
                    result += subTask.toString();
                } else {
                result += subTask.toString() + "\n";
                }
            }
        }
        return result;
    }

    public void updateTask(int id, String name, String description, TaskStatus taskStatusk){
        if (tasks.containsKey(id)){
            Task newVersionTask = new Task(id, name, description, taskStatusk);
            if (tasks.get(id).equals(newVersionTask)){
                System.out.println("Изменения в задаче с индентификатором " + id + " не обнаружены.");
            } else {
                tasks.remove(id);
                tasks.put(id, newVersionTask);
            }
        } else {
            System.out.println("Задача с таким идентификатором не найдена");
        }
    }

    public void updateEpic(int id, String name, String description){
        if (epics.containsKey(id)){
            TaskStatus taskStatus = epics.get(id).getTaskStatus();
            ArrayList<Integer> subTasksId = epics.get(id).getSubTasksId();
            Epic newVersionEpic = new Epic(id, name, description, taskStatus);
            if (epics.get(id).equals(newVersionEpic)){
                System.out.println("Изменения в Epic задаче с индентификатором " + id + " не обнаружены.");
            } else {
                epics.remove(id);
                epics.put(id, newVersionEpic);
                newVersionEpic.setSubTasksId(subTasksId); //В поле новой Epic присваиваю ссылку на список подзадач из удаленной версии Epic
            }
        } else {
            System.out.println("Epic задача с таким идентификатором не найдена");
        }
    }

    public void updateSubTask(int id, String name, String description, TaskStatus taskStatusk, int epicId){
        if (subTasks.containsKey(id)){
            Epic epic = epics.get(epicId);
            SubTask newVersionSubTask = new SubTask(id, name, description, taskStatusk, epicId);
            if (subTasks.get(id).equals(newVersionSubTask)){
                System.out.println("Изменения в подзадаче с индентификатором " + id + " не обнаружены.");
            } else {
                //Если у подзадачи меняют Epic идентификатор, у прежнего Epic убираем подзадачу из списка
                //И добавляем подзадачу в спискок подзадачу другого обьекта Epic
                int oldEpicId =subTasks.get(id).getEpicId();
                if(epicId != oldEpicId){
                    epics.get(oldEpicId).removeIdSubTasks(id);
                    epics.get(epicId).addIdInSubTasksList(id);
                    setActualTaskStatusEpic(epic);
                }
                subTasks.remove(id);
                subTasks.put(id, newVersionSubTask);
                setActualTaskStatusEpic(epic);
            }
        } else {
            System.out.println("Подзадача с таким идентификатором не найдена");
        }

    }

    public Task getTask(int id){
        for(Integer i : tasks.keySet()){
            if (i == id) {
                return tasks.get(id);
            }
        }

        for(Integer i : epics.keySet()){
            if (i == id) {
                return epics.get(id);
            }
        }

        for(Integer i : subTasks.keySet()){
            if (i == id) {
                return subTasks.get(id);
            }
        }
        System.out.println("Задачи с идентификатором " + id + " не существует");
        return null;
    }

    public String getAllSubTaskEpic(int epicId){
        ArrayList<Integer> subTasksIdList = epics.get(epicId).getSubTasksId();
        String result = "";
        for(Integer id : subTasksIdList){
            result += subTasks.get(id).toString() + "\n";
        }
        return result;
    }

    public void removeAnyTask (int taskId){
        for(Integer id : tasks.keySet()){
            if (id == taskId) {
                tasks.remove(taskId);
                return;
            }
        }

        for(Integer id : epics.keySet()){
            if (id == taskId) {
                if(epics.get(taskId).getSizeSubTasksIdList() != 0){
                    System.out.println("Задача Epic не может быть удалена пока у нее есть подзадачи. Сначала удалите все подзадачи.");
                    return;
                } else {
                    epics.remove(taskId);
                    return;
                }
            }
        }

        for(Integer id : subTasks.keySet()){
            if (id == taskId) {
                int eId = subTasks.get(taskId).getEpicId(); //Получаем ID Epic задачи
                epics.get(eId).removeIdSubTasks(taskId); //Удаляем ID подзадачи из списка идентификаторов подзадач в объекте Epic
                setActualTaskStatusEpic(epics.get(eId)); //Проверяем статус Epic
                subTasks.remove(taskId);
                return;
            }
        }
    }

    public void removeAllTask(){
        tasks.clear();
        epics.clear();
        subTasks.clear();
    }
}
