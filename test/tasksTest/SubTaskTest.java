package tasksTest;

import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.SubTask;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTaskTest {

    //проверьте, что наследники класса Task равны друг другу, если равен их id;
    @Test
    public void shouldEqualsSubTasksIfIdEquals(){
        SubTask subTask1 = new SubTask("name", "descriptions", LocalDateTime.now(), Duration.ofMinutes(10));
        SubTask subTask2 = new SubTask("name", "descriptions", LocalDateTime.now(), Duration.ofMinutes(10));
        subTask1.setId(1);
        subTask2.setId(1);
        Assertions.assertEquals(subTask1, subTask2, "При равных id экземпляры SubTask не равны");
    }
}