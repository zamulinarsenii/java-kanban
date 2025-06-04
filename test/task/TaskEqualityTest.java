package task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskEqualityTest {

    @Test
    void tasksWithSameIdShouldBeEqual() {
        Task task1 = new Task("Task 1", "Description 1", 1);
        Task task2 = new Task("Task 2", "Description 2", 1);
        assertEquals(task1, task2, "Задачи с одинаковым id должны быть равны");
    }

    @Test
    void epicsWithSameIdShouldBeEqual() {
        Epic epic1 = new Epic("Epic 1", "Epic Description", 2);
        Epic epic2 = new Epic("Epic 2", "Another Description", 2);
        assertEquals(epic1, epic2, "Эпики с одинаковым id должны быть равны");
    }
}