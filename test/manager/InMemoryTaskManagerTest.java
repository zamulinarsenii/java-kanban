package manager;

public class InMemoryTaskManagerTest extends TaskManagerTest {
    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }
}
