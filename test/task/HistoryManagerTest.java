package task;

import manager.HistoryManager;
import manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void historyShouldKeepTaskVersions() {
        Task task = new Task("Task", "Desc", 1, Status.NEW);
        historyManager.add(task);

        task.setStatus(Status.DONE);
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertAll(
                () -> assertEquals(Status.NEW, history.get(0).getStatus()),
                () -> assertEquals(Status.DONE, history.get(1).getStatus())
        );
    }
}