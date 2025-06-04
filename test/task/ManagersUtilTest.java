package task;

import manager.Managers;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ManagersUtilTest {
    @Test
    void managersShouldBeInitialized() {
        assertNotNull(Managers.getDefault(), "Менеджер должен быть проинициализирован");
        assertNotNull(Managers.getDefaultHistory(), "История должна быть проинициализирована");
    }
}
