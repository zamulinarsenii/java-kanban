import manager.FileBackedTaskManager;
import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.TaskStatus;
import task.Subtask;
import task.Task;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        File file = new File("tasks.txt");
        TaskManager inFileTaskManager = Managers.getDefaultFile(file);
        FileBackedTaskManager.loadFromFile(file);
        addTask(inFileTaskManager);
        addEpic(inFileTaskManager);
        addSubtask(inFileTaskManager);
        printAllTasks(inFileTaskManager);
        getById(inFileTaskManager);
        updateTask(inFileTaskManager);
        printAllTasks(inFileTaskManager);
        deleteById(inFileTaskManager);
        printAllTasks(inFileTaskManager);
        //deleteAllTasks(inFileTaskManager);
        printAllTasks(inFileTaskManager);
    }

    public static void addTask(TaskManager inMemoryTaskManager) {
        String name = "Задача 1";
        String description = "Описание задачи 1";
        TaskStatus status = TaskStatus.NEW;
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 1, 0, 0, 0);
        Duration duration = Duration.ofMinutes(30);
        Task task = new Task(inMemoryTaskManager.getCounter(), name, description, status, startTime, duration);// id = 1
        inMemoryTaskManager.addTask(task);

        name = "Задача 2";
        description = "Описание задачи 2";
        status = TaskStatus.DONE;
        startTime = LocalDateTime.of(2025, 6, 2, 0, 15, 0);
        duration = Duration.ofMinutes(60);
        task = new Task(inMemoryTaskManager.getCounter(), name, description, status, startTime, duration);// id = 2
        inMemoryTaskManager.addTask(task);
    }

    public static void addEpic(TaskManager inMemoryTaskManager) {
        String name = "Эпик 1";
        String description = "Описание эпика 1";
        Epic epic = new Epic(inMemoryTaskManager.getCounter(), name, description);// id = 3
        inMemoryTaskManager.addEpic(epic);

        name = "Эпик 2";
        description = "Описание эпика 2";
        epic = new Epic(inMemoryTaskManager.getCounter(), name, description);// id = 4
        inMemoryTaskManager.addEpic(epic);
    }

    public static void addSubtask(TaskManager inMemoryTaskManager) {
        String name = "Подзадача 1";
        String description = "Описание подзадачи 1";
        TaskStatus status = TaskStatus.NEW;
        int epicId = 3;
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 3, 12, 0, 0);
        Duration duration = Duration.ofMinutes(30);
        Subtask subtask = new Subtask(inMemoryTaskManager.getCounter(), name, description, status, startTime, duration, epicId);// id = 5
        inMemoryTaskManager.addSubtask(subtask);

        name = "Подзадача 2";
        description = "Описание подзадачи 2";
        status = TaskStatus.DONE;
        epicId = 3;
        startTime = LocalDateTime.of(2025, 6, 4, 13, 0, 0);
        duration = Duration.ofMinutes(60);
        subtask = new Subtask(inMemoryTaskManager.getCounter(), name, description, status, startTime, duration, epicId);// id = 6
        inMemoryTaskManager.addSubtask(subtask);

        name = "Подзадача 3";
        description = "Описание подзадачи 3";
        status = TaskStatus.DONE;
        epicId = 4;
        startTime = LocalDateTime.of(2025, 6, 5, 13, 30, 0);
        duration = Duration.ofMinutes(30);
        subtask = new Subtask(inMemoryTaskManager.getCounter(), name, description, status, startTime, duration, epicId);// id = 7
        inMemoryTaskManager.addSubtask(subtask);
    }

    public static void deleteAllTasks(TaskManager inMemoryTaskManager) {
        inMemoryTaskManager.deleteAllTasks();
        inMemoryTaskManager.deleteAllEpics();
        inMemoryTaskManager.deleteAllSubtasks();
    }

    public static void getById(TaskManager inMemoryTaskManager) {
        System.out.println("Поиск задачи по id = 1");
        System.out.println("--- --- ---");
        Task task = inMemoryTaskManager.getById(1);
        System.out.println(task);

        System.out.println("Поиск задачи по id = 2");
        System.out.println("--- --- ---");
        task = inMemoryTaskManager.getById(2);
        System.out.println(task);

        System.out.println("Поиск задачи по id = 5");
        System.out.println("--- --- ---");
        task = inMemoryTaskManager.getById(5);
        System.out.println(task);
    }

    public static void updateTask(TaskManager inMemoryTaskManager) {
        String name = "Обновленная Задача 1";
        String description = "Обновленное Описание задачи 1";
        int id = 0;
        TaskStatus status = TaskStatus.DONE;
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 2, 0, 0, 0);
        Duration duration = Duration.ofMinutes(15);
        Task task = new Task(id, name, description, status, startTime, duration);// id = 0
        inMemoryTaskManager.updateTask(task);

        name = "Обновленный Эпик 2";
        description = "Обновленное Описание эпика 2";
        id = 3;
        Epic epic = new Epic(id, name, description);// id = 3
        inMemoryTaskManager.updateEpic(epic);

        name = "Обновленная Подзадача 1";
        description = "Обновленное Описание подзадачи 1";
        status = TaskStatus.DONE;
        int epicId = 3;
        id = 4;
        startTime = LocalDateTime.of(2025, 6, 2, 12, 0, 0);
        duration = Duration.ofMinutes(90);
        Subtask subtask = new Subtask(id, name, description, status, startTime, duration, epicId);// id = 4
        inMemoryTaskManager.updateSubtask(subtask);
    }

    public static void deleteById(TaskManager inMemoryTaskManager) {
        inMemoryTaskManager.deleteById(2);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : manager.getAllSubtasksByEpicId(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
