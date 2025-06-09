package task;

import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    void managerShouldAddAndFindTasks() {
        Task task = new Task("Task", "Description", 1);
        Epic epic = new Epic("Epic", "Description", 2);
        Subtask subtask = new Subtask("Subtask", "Description", 3, Status.NEW, 2);

        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);

        assertAll(
                () -> assertEquals(task, taskManager.getById(1)),
                () -> assertEquals(epic, taskManager.getById(2)),
                () -> assertEquals(subtask, taskManager.getById(3))
        );
    }
    @Test
    void shouldNotModifyInternalTaskState() {
        Task task = new Task("Test", "Desc", 1);
        taskManager.addTask(task);

        task.setTitle("Hacked");
        Task saved = taskManager.getById(1);

        assertNotEquals("Hacked", saved.getTitle());
    }
}
