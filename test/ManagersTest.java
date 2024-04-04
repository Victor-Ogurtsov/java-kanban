import managers.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ManagersTest {

    @Test //убедитесь, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров;
    public void shouldReturnNotNullIfCreateInMemoryHistoryManager(){
        Assertions.assertNotNull(Managers.getDefault(), "getDefault() возвращает null");
    }

    @Test //убедитесь, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров;
    public void shouldReturnNotNullIfCreateHistoryManager(){
        Assertions.assertNotNull(Managers.getDefaultHistory(), "getDefaultHistory() возвращает null");
    }

}
