package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.*;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest {
    protected TaskManager taskManager;

    protected abstract TaskManager createTaskManager();

    @BeforeEach
    public void beforeEach() {
        taskManager = createTaskManager();
    }

    @Test
    public void shouldAddTaskAndGetItById() {
        Task task = new Task(taskManager.getCounter(), "Task1", "Description1", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(15));
        taskManager.addTask(task);
        Task savedTask = taskManager.getById(task.getId());
        assertEquals(task, savedTask);
    }

    @Test
    public void shouldAddEpicAndSubtaskAndCheckEpicStatus_AllNew() {
        Epic epic = new Epic(taskManager.getCounter(), "Epic1", "EpicDesc");
        taskManager.addEpic(epic);

        Subtask subtask1 = new Subtask(taskManager.getCounter(), "Sub1", "Desc", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30), epic.getId());
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask(taskManager.getCounter(), "Sub2", "Desc", TaskStatus.NEW,
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(30), epic.getId());
        taskManager.addSubtask(subtask2);

        Epic savedEpic = (Epic) taskManager.getById(epic.getId());
        assertEquals(TaskStatus.NEW, savedEpic.getStatus());
    }

    @Test
    public void shouldSetEpicStatusToDone_WhenAllSubtasksDone() {
        Epic epic = new Epic(taskManager.getCounter(), "Epic1", "EpicDesc");
        taskManager.addEpic(epic);

        Subtask subtask1 = new Subtask(taskManager.getCounter(), "Sub1", "Desc", TaskStatus.DONE,
                LocalDateTime.now(), Duration.ofMinutes(30), epic.getId());
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask(taskManager.getCounter(), "Sub2", "Desc", TaskStatus.DONE,
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(30), epic.getId());
        taskManager.addSubtask(subtask2);

        Epic savedEpic = (Epic) taskManager.getById(epic.getId());
        assertEquals(TaskStatus.DONE, savedEpic.getStatus());
    }

    @Test
    public void shouldSetEpicStatusToInProgress_WhenSubtasksNewAndDone() {
        Epic epic = new Epic(taskManager.getCounter(), "Epic1", "EpicDesc");
        taskManager.addEpic(epic);

        Subtask subtask1 = new Subtask(taskManager.getCounter(), "Sub1", "Desc", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30), epic.getId());
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask(taskManager.getCounter(), "Sub2", "Desc", TaskStatus.DONE,
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(30), epic.getId());
        taskManager.addSubtask(subtask2);

        Epic savedEpic = (Epic) taskManager.getById(epic.getId());
        assertEquals(TaskStatus.IN_PROGRESS, savedEpic.getStatus());
    }

    @Test
    public void shouldSetEpicStatusToInProgress_WhenAllSubtasksInProgress() {
        Epic epic = new Epic(taskManager.getCounter(), "Epic1", "EpicDesc");
        taskManager.addEpic(epic);

        Subtask subtask1 = new Subtask(taskManager.getCounter(), "Sub1", "Desc", TaskStatus.IN_PROGRESS,
                LocalDateTime.now(), Duration.ofMinutes(30), epic.getId());
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask(taskManager.getCounter(), "Sub2", "Desc", TaskStatus.IN_PROGRESS,
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(30), epic.getId());
        taskManager.addSubtask(subtask2);

        Epic savedEpic = (Epic) taskManager.getById(epic.getId());
        assertEquals(TaskStatus.IN_PROGRESS, savedEpic.getStatus());
    }

    @Test
    public void shouldThrowException_WhenTasksIntersect() {
        Task task1 = new Task(taskManager.getCounter(), "Task1", "Desc", TaskStatus.NEW,
                LocalDateTime.of(2025, 6, 1, 12, 0), Duration.ofMinutes(60));
        taskManager.addTask(task1);

        Task task2 = new Task(taskManager.getCounter(), "Task2", "Desc", TaskStatus.NEW,
                LocalDateTime.of(2025, 6, 1, 12, 30), Duration.ofMinutes(60));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> taskManager.addTask(task2));
        assertEquals("Подзадача пересекается по времени с другой задачей.", exception.getMessage());
    }
}
