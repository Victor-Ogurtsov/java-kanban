

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        System.out.println("Создаем две задачи:");
        System.out.println(taskManager.newTask("Приготовить завтрак", "Приготовить кашу, чай зеленый с сахаром," +
                        " накрыть на стол", TaskStatus.IN_PROGRESS));
        System.out.println(taskManager.newTask("Прибраться на кухне", "Помыть посуду, протереть пол," +
                " убрать пыль", TaskStatus.NEW));

        System.out.println("Создание двух сложных Epic задач:");
        System.out.println(taskManager.newEpic("Подарок к НГ", "Выбрать, купить, подарить подарок"));
        System.out.println(taskManager.newEpic("Выбрать бригаду для ремонта", "Сравнить сметы нескольких бригад по ремонту"));

        System.out.println("Cоздание трех подзадач");
        System.out.println(taskManager.newSubTask("Выбрать подарок", "Изучить предложения, забронировать подарок",
                TaskStatus.IN_PROGRESS, 3));
        System.out.println(taskManager.newSubTask("Забрать и вручить подарок", "Забрать подарок по адресу... Вручить.",
                TaskStatus.NEW, 3));
        System.out.println(taskManager.newSubTask("Собрать рекомендации", "Поспрашвать у всех знакомых, есть ли бригада," +
                        "которую они готовы советовать", TaskStatus.NEW, 4));

        System.out.println("Вывод списка всех задач:");
        System.out.println(taskManager.getAllTasks());
        System.out.println("Вывод списка всех сложных задач Epic");
        System.out.println(taskManager.getAllEpics());
        System.out.println("Вывод списка всех подзадач");
        System.out.println(taskManager.getAllSubTasks());

        System.out.println("Изменение статуса в задаче с идентификатором 1:");
        taskManager.updateTask(1,"Приготовить завтрак", "Приготовить кашу, чай зеленый с сахаром," +
                " накрыть на стол", TaskStatus.DONE);
        System.out.println("Вывод задачи (id=1) по идентификатору");
        System.out.println(taskManager.getTask(1));

        System.out.println("Изменение в названии в Epic задаче с идентификатором 3:");
        taskManager.updateEpic(3, "Подарок к 8 марта", "Выбрать, купить, подарить подарок");

        System.out.println("Вывод Epic по идентификатору");
        System.out.println(taskManager.getTask(3));

        System.out.println("Изменение статуса в подзадаче с идентификатором 5:");
        taskManager.updateSubTask(5,"Выбрать подарок", "Изучить предложения, забронировать подарок",
                TaskStatus.NEW, 3);
        System.out.println("Вывод подзадачи по идентификатору:");
        System.out.println(taskManager.getTask(5));

        System.out.println("Статус задачи Epic изменился за статусом подзадачи, вывод по идентификатору:");
        System.out.println(taskManager.getTask(3));
        System.out.println("Вывод всех подзадач Epic задач с индентификатором 3");
        System.out.println(taskManager.getAllSubTaskEpic(3));

        System.out.println("Попытка удалить Epic c id = 4 c существующей подзадачей:");
        taskManager.removeAnyTask(4);
        System.out.println("Удаление подзадачи с id = 7 у Epic c id = 4:");
        taskManager.removeAnyTask(7);
        System.out.println("Удаление Epic c id = 4:");
        taskManager.removeAnyTask(4);

        System.out.println("Попытка вывода удаленного Epic 4");
        System.out.println(taskManager.getTask(4));

        System.out.println("Удаление всех задач/Epic задач/подзадач");
        taskManager.removeAllTask();

        System.out.println("Вывод списка всех задач");
        System.out.println(taskManager.getAllTasks());
        System.out.println("Вывод списка всех сложных задач Epic");
        System.out.println(taskManager.getAllEpics());
        System.out.println("Вывод списка всех подзадач");
        System.out.println(taskManager.getAllSubTasks());
    }
}
