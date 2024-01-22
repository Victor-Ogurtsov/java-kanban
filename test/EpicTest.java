import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;

public class EpicTest {

    //проверьте, что наследники класса Task равны друг другу, если равен их id;
    @Test
    public void shouldEqualsEpicsIfIdEquals(){
        Epic epic1 = new Epic("name", "descriptions");
        Epic epic2 = new Epic("name", "descriptions");
        epic1.setId(1);
        epic2.setId(1);
        Assertions.assertEquals(epic1, epic2, "При равных id экземпляры Epic не равны");
    }

    @Test //проверьте, что объект Epic нельзя добавить в самого себя в виде подзадачи;
    public void shouldReturn(){
        Epic epic1 = new Epic("name", "descriptions");
        epic1.setId(1);
        epic1.setSubTaskId(1);
        Assertions.assertEquals(0,epic1.getSubTasksId().size(), "Id Epic занесен в список SubTasksId");
    }
}