package manager;

import java.io.File;

public class FileBackedTaskManagerTest extends TaskManagerTest {
    @Override
    protected FileBackedTaskManager createTaskManager() {
        return new FileBackedTaskManager(new File("test_file.txt"));
    }
}
