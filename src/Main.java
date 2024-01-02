import managers.TaskManager;
import tasks.*;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        System.out.println("Создаем две задачи:");
        Task newTask1 = new Task("Приготовить завтрак", "Приготовить кашу, чай зеленый с сахаром, накрыть на стол");
        System.out.println(taskManager.addTask(newTask1));
        Task newTask2 = new Task("Прибраться на кухне", "Помыть посуду, протереть пол, убрать пыль");
        System.out.println(taskManager.addTask(newTask2));

        System.out.println("Создание двух сложных Epic задач:");
        Epic newEpic1 = new Epic("Подарок к НГ", "Выбрать, купить, подарить подарок");
        System.out.println(taskManager.addEpic(newEpic1));
        Epic newEpic2 = new Epic("Выбрать бригаду для ремонта", "Сравнить сметы нескольких бригад по ремонту");
        System.out.println(taskManager.addEpic(newEpic2));

        System.out.println("Cоздание трех подзадач");
        SubTask newSubTask1 = new SubTask("Выбрать подарок", "Изучить предложения, забронировать подарок");
        System.out.println(taskManager.addSubTask(newEpic1.getId(), newSubTask1));
        SubTask newSubTask2 = new SubTask("Забрать и вручить подарок", "Забрать подарок по адресу... Вручить.");
        System.out.println(taskManager.addSubTask(newEpic1.getId(), newSubTask2));
        SubTask newSubTask3 = new SubTask("Собрать рекомендации", "Поспрашвать у всех знакомых, есть ли бригада,которую они готовы советовать");
        System.out.println(taskManager.addSubTask(newEpic2.getId(), newSubTask3));

        System.out.println("Вывод списка всех задач:");
        System.out.println(taskManager.getAllTasks());
        System.out.println("Вывод списка всех сложных задач Epic");
        System.out.println(taskManager.getAllEpics());
        System.out.println("Вывод списка всех подзадач");
        System.out.println(taskManager.getAllSubTasks());

        System.out.println("Изменение описания в задаче:");
        Task taskToUpdate = taskManager.getTask(newTask1.getId());
        taskToUpdate.setDescription("Приготовить кашу, кофе, накрыть на стол");
        taskManager.updateTask(taskToUpdate);
        System.out.println("Вывод задачи по идентификатору:");
        System.out.println(taskManager.getTask(taskToUpdate.getId()));

        System.out.println("Изменение названия в Epic:");
        Epic epicToUpdate = taskManager.getEpic(newEpic1.getId());
        epicToUpdate.setName("Подарок на 8 марта");
        taskManager.updateEpic(epicToUpdate);
        System.out.println("Вывод Epic по идентификатору:");
        System.out.println(taskManager.getEpic(epicToUpdate.getId()));

        System.out.println("Изменение статуса в подзадаче:");
        SubTask subTaskToUpdate = taskManager.getSubtask(newSubTask1.getId());
        subTaskToUpdate.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(subTaskToUpdate);
        System.out.println("Вывод подзадачи по идентификатору:");
        System.out.println(taskManager.getSubtask(subTaskToUpdate.getId()));
        System.out.println("Вывод Epic, статус изменился после обновления подзадачи:");
        System.out.println(taskManager.getEpic(newEpic1.getId()));

        System.out.println("Вывод списка всех подзадач по идентификатору Epic");
        System.out.println(taskManager.getAllSubTaskOfEpic(newEpic1.getId()));

        System.out.println("Удаление задачи по идентификатору");
        taskManager.removeTask(newTask1.getId());
        System.out.println("Удаление Epic по идентификатору");
        taskManager.removeEpic(newEpic2.getId());
        System.out.println("Удаление подзадачи по идентификатору");
        taskManager.removeSubTask(newSubTask1.getId());

        System.out.println("Вывод списка всех задач");
        System.out.println(taskManager.getAllTasks());
        System.out.println("Вывод списка всех сложных задач Epic");
        System.out.println(taskManager.getAllEpics());
        System.out.println("Вывод списка всех подзадач");
        System.out.println(taskManager.getAllSubTasks());

        System.out.println("Удаление всех задач/Epic задач/подзадач");
        taskManager.removeAllTasks();
        taskManager.removeAllEpics();
        taskManager.removeAllSubTasks();

        System.out.println("Вывод списка всех задач");
        System.out.println(taskManager.getAllTasks());
        System.out.println("Вывод списка всех сложных задач Epic");
        System.out.println(taskManager.getAllEpics());
        System.out.println("Вывод списка всех подзадач");
        System.out.println(taskManager.getAllSubTasks());
    }
}
