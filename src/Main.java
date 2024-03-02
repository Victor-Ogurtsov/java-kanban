import managers.Managers;
import managers.TaskManager;
import tasks.*;
import java.io.File;


public class Main {

    public static void main(String[] args) {
       //Первый запуск приложения
        TaskManager fileBackedTaskManager = Managers.loadFromFile(new File("src\\resources\\tasks.csv"));

        System.out.println("Создаем две задачи:");
        Task newTask1 = new Task("Приготовить завтрак", "Приготовить кашу и чай зеленый с сахаром и накрыть на стол");
        System.out.println(fileBackedTaskManager.addTask(newTask1));
        Task newTask2 = new Task("Прибраться на кухне", "Помыть посуду и протереть пол и убрать пыль");
        System.out.println(fileBackedTaskManager.addTask(newTask2));

        System.out.println("Создание двух сложных Epic задач:");
        Epic newEpic1 = new Epic("Подарок к НГ", "Выбрать и купить и подарить подарок");
        System.out.println(fileBackedTaskManager.addEpic(newEpic1));
        Epic newEpic2 = new Epic("Выбрать бригаду для ремонта", "Сравнить сметы нескольких бригад по ремонту");
        System.out.println(fileBackedTaskManager.addEpic(newEpic2));

        System.out.println("Cоздание трех подзадач");
        SubTask newSubTask1 = new SubTask("Выбрать подарок", "Изучить предложения и забронировать подарок");
        System.out.println(fileBackedTaskManager.addSubTask(newEpic1.getId(), newSubTask1));
        SubTask newSubTask2 = new SubTask("Забрать и вручить подарок", "Забрать подарок по адресу и вручить.");
        System.out.println(fileBackedTaskManager.addSubTask(newEpic1.getId(), newSubTask2));
        SubTask newSubTask3 = new SubTask("Собрать рекомендации", "Поспрашивать у всех знакомых");
        System.out.println(fileBackedTaskManager.addSubTask(newEpic2.getId(), newSubTask3));

        System.out.println("Вывод списка всех задач:");
        System.out.println(fileBackedTaskManager.getAllTasks());
        System.out.println("Вывод списка всех сложных задач Epic");
        System.out.println(fileBackedTaskManager.getAllEpics());
        System.out.println("Вывод списка всех подзадач");
        System.out.println(fileBackedTaskManager.getAllSubTasks());

        System.out.println("Изменение описания в задаче:");
        Task taskToUpdate = fileBackedTaskManager.getTask(newTask1.getId());
        taskToUpdate.setDescription("Приготовить кашу, кофе, накрыть на стол");
        fileBackedTaskManager.updateTask(taskToUpdate);
        System.out.println("Вывод задачи по идентификатору:");
        System.out.println(fileBackedTaskManager.getTask(taskToUpdate.getId()));

        System.out.println("Изменение названия в Epic:");
        Epic epicToUpdate = fileBackedTaskManager.getEpic(newEpic1.getId());
        epicToUpdate.setName("Подарок на 8 марта");
        fileBackedTaskManager.updateEpic(epicToUpdate);
        System.out.println("Вывод Epic по идентификатору:");
        System.out.println(fileBackedTaskManager.getEpic(epicToUpdate.getId()));

        System.out.println("Изменение статуса в подзадаче:");
        SubTask subTaskToUpdate = fileBackedTaskManager.getSubtask(newSubTask1.getId());
        subTaskToUpdate.setTaskStatus(TaskStatus.IN_PROGRESS);
        fileBackedTaskManager.updateSubTask(subTaskToUpdate);
        System.out.println("Вывод подзадачи по идентификатору:");
        System.out.println(fileBackedTaskManager.getSubtask(subTaskToUpdate.getId()));
        System.out.println("Вывод Epic, статус изменился после обновления подзадачи:");
        System.out.println(fileBackedTaskManager.getEpic(newEpic1.getId()));

        System.out.println("Вывод истории просмотра задач:");
        System.out.println(fileBackedTaskManager.getHistory());
        System.exit(0);

       //Второй запуск приложения
        /*TaskManager fileBackedTaskManager = Managers.loadFromFile(new File("src\\resources\\tasks.csv"));

        System.out.println("Вывод списка всех подзадач по идентификатору Epic");
        System.out.println(fileBackedTaskManager.getAllSubTaskOfEpic(3));

        System.out.println("Удаление задачи по идентификатору");
        fileBackedTaskManager.removeTask(1);
        System.out.println("Удаление Epic по идентификатору");
        fileBackedTaskManager.removeEpic(4);
        System.out.println("Удаление подзадачи по идентификатору");
        fileBackedTaskManager.removeSubTask(5);

        System.out.println("Вывод истории просмотра задач:");
        System.out.println(fileBackedTaskManager.getHistory());
        System.exit(0);*/

        //Третий запуск приложения
        /*TaskManager fileBackedTaskManager = Managers.loadFromFile(new File("src\\resources\\tasks.csv"));
        System.out.println("Вывод списка всех задач");
        System.out.println(fileBackedTaskManager.getAllTasks());
        System.out.println("Вывод списка всех сложных задач Epic");
        System.out.println(fileBackedTaskManager.getAllEpics());
        System.out.println("Вывод списка всех подзадач");
        System.out.println(fileBackedTaskManager.getAllSubTasks());

        System.out.println("Удаление всех задач/Epic задач/подзадач");
        fileBackedTaskManager.removeAllTasks();
        fileBackedTaskManager.removeAllEpics();
        fileBackedTaskManager.removeAllSubTasks();

        System.out.println("Вывод списка всех задач");
        System.out.println(fileBackedTaskManager.getAllTasks());
        System.out.println("Вывод списка всех сложных задач Epic");
        System.out.println(fileBackedTaskManager.getAllEpics());
        System.out.println("Вывод списка всех подзадач");
        System.out.println(fileBackedTaskManager.getAllSubTasks());

        System.out.println("Вывод истории просмотра задач:");
        System.out.println(fileBackedTaskManager.getHistory());*/
    }
}
