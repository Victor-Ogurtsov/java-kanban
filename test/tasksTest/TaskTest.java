package tasksTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Task;

public class TaskTest {

    //проверьте, что экземпляры класса Task равны друг другу, если равен их id
    @Test
    public void shouldEqualsTasksIfIdEquals(){
        Task task1 = new Task("name", "descriptions");
        Task task2 = new Task("name", "descriptions");
        task1.setId(1);
        task2.setId(1);
        Assertions.assertEquals(task1, task2, "При равных id экземпляры Task не равны");
    }
}
