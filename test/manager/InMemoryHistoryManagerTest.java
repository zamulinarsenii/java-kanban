package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.*;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void shouldReturnEmptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    public void shouldAddAndPreventDuplicates() {
        Task task = new Task(1, "Task", "Desc", TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
        historyManager.add(task);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    public void shouldRemoveFromHistory() {
        Task task1 = new Task(1, "Task1", "Desc1", TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
        Task task2 = new Task(2, "Task2", "Desc2", TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(10));
        Task task3 = new Task(3, "Task3", "Desc3", TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(10));

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(1);
        assertEquals(2, historyManager.getHistory().size());
        historyManager.remove(3);
        assertEquals(1, historyManager.getHistory().size());
        historyManager.remove(2);
        assertTrue(historyManager.getHistory().isEmpty());
    }
}
