package manager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import task.Task;
import java.util.List;

class InMemoryHistoryManagerTest {
    private final InMemoryHistoryManager history = new InMemoryHistoryManager();

    @Test
    void shouldAddTasksToHistory() {
        Task task1 = new Task("Task1", "Description1", 1);
        Task task2 = new Task("Task2", "Description2", 2);

        history.add(task1);
        history.add(task2);

        assertEquals(List.of(task1, task2), history.getHistory());
    }

    @Test
    void shouldRemoveDuplicatesAndKeepLastPosition() {
        Task task = new Task("Task1", "Description1", 1);

        history.add(task);
        history.add(new Task("Task2", "Description2", 2));
        history.add(task); // Повторное добавление

        assertEquals(2, history.getHistory().size());
        assertEquals(task, history.getHistory().get(1)); // Проверяем последнюю позицию
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        Task task = new Task("Task1", "Description1", 1);
        history.add(task);

        history.remove(1);

        assertTrue(history.getHistory().isEmpty());
    }

    @Test
    void shouldHandleNodeRemovalCorrectly() {
        Task task1 = new Task("Task1", "Description1", 1);
        Task task2 = new Task("Task2", "Description2", 2);

        history.add(task1);
        history.add(task2);
        history.remove(1); // Удаление из середины/начала

        assertEquals(List.of(task2), history.getHistory());
    }
}
