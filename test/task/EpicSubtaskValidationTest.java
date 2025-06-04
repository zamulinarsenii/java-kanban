package task;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EpicSubtaskValidationTest {
    private TaskManager taskManager;
    private Epic epic;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
        epic = new Epic("Epic", "Description", 1);
        taskManager.addEpic(epic);
    }

    @Test
    void epicCannotBeSubtaskToItself() {
        Subtask invalidSubtask = new Subtask("Invalid", "Desc", 2, Status.NEW, epic.getId());
        taskManager.addSubtask(invalidSubtask);
        assertFalse(epic.getSubtasksId().contains(epic.getId()), "Эпик не должен содержать себя как подзадачу");
    }

    @Test
    void subtaskCannotBeItsOwnEpic() {
        Subtask invalidSubtask = new Subtask("Invalid", "Desc", 3, Status.NEW, 3);
        taskManager.addSubtask(invalidSubtask);
        assertNull(taskManager.getById(3), "Подзадача с epicId = собственному id не должна добавляться");
    }
}
