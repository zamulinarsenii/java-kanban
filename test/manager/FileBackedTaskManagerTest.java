package manager;

import org.junit.jupiter.api.*;
import task.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static manager.Managers.getDefaultFile;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    File tempFile;

    @BeforeEach
    void setup() throws IOException {
        tempFile = File.createTempFile("tasksTest", ".txt");
    }

    @AfterEach
    void cleanup() {
        tempFile.delete();
    }

    @Test
    void saveAndLoadEmptyFile() {
        TaskManager manager = getDefaultFile(tempFile);

        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(tempFile);
        assertTrue(loaded.getAllTasks().isEmpty());
        assertTrue(loaded.getAllEpics().isEmpty());
        assertTrue(loaded.getAllSubtasks().isEmpty());
    }

    @Test
    void saveAndLoadMultipleTasks() {
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);

        Task task1 = new Task("Task1", "Description1", 1, Status.NEW);
        Epic epic1 = new Epic("Epic1", "Epic description",2);
        Subtask sub1 = new Subtask("Sub1", "Sub description", 3, Status.NEW, 2); // epicId будет 2

        manager.addTask(task1);
        manager.addEpic(epic1);
        manager.addSubtask(sub1);

        // Повторно загружаем из файла
        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(tempFile);

        List<Task> tasks = loaded.getAllTasks();
        List<Task> epics = loaded.getAllEpics();
        List<Task> subtasks = loaded.getAllSubtasks();

        assertEquals(1, tasks.size());
        assertEquals("Task1", tasks.get(0).getName());

        assertEquals(1, epics.size());
        assertEquals("Epic1", epics.get(0).getName());

        assertEquals(1, subtasks.size());
        assertEquals("Sub1", subtasks.get(0).getName());
        assertEquals(epics.get(0).getId(), ((Subtask) subtasks.get(0)).getEpicId());
    }
}
