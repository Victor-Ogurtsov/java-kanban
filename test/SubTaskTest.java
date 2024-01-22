import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.SubTask;

public class SubTaskTest {

    //проверьте, что наследники класса Task равны друг другу, если равен их id;
    @Test
    public void shouldEqualsSubTasksIfIdEquals(){
        SubTask subTask1 = new SubTask("name", "descriptions");
        SubTask subTask2 = new SubTask("name", "descriptions");
        subTask1.setId(1);
        subTask2.setId(1);
        Assertions.assertEquals(subTask1, subTask2, "При равных id экземпляры SubTask не равны");
    }
}