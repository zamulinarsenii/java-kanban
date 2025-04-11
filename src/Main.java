import task.*;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        addTask(taskManager);
        addEpic(taskManager);
        addSubtask(taskManager);
        printAllTasks(taskManager);
        getById(taskManager);
        updateTask(taskManager);
        printAllTasks(taskManager);

        deleteById(taskManager);
        printAllTasks(taskManager);

        deleteAllTasks(taskManager);
        printAllTasks(taskManager);
    }

    public static void addTask(TaskManager taskManager) {
        String title = "Задача 1";
        String description = "Описание задачи 1";
        Status status = Status.NEW;
        Task task = new Task(title, description, taskManager.counter, status);// id = 0
        taskManager.addTask(task);

        title = "Задача 2";
        description = "Описание задачи 2";
        status = Status.DONE;
        task = new Task(title, description, taskManager.counter, status);// id = 1
        taskManager.addTask(task);
    }

    public static void addEpic(TaskManager taskManager) {
        String title = "Эпик 1";
        String description = "Описание эпика 1";
        Epic epic = new Epic(title, description, taskManager.counter);// id = 2
        taskManager.addEpic(epic);

        title = "Эпик 2";
        description = "Описание эпика 2";
        epic = new Epic(title, description, taskManager.counter);// id = 3
        taskManager.addEpic(epic);
    }

    public static void addSubtask(TaskManager taskManager) {
        String title = "Подзадача 1";
        String description = "Описание подзадачи 1";
        Status status = Status.NEW;
        int epicId = 2;
        Subtask subtask = new Subtask(title, description, taskManager.counter, status, epicId);// id = 4
        taskManager.addSubtask(subtask);

        title = "Подзадача 2";
        description = "Описание подзадачи 2";
        status = Status.DONE;
        epicId = 2;
        subtask = new Subtask(title, description, taskManager.counter, status, epicId);// id = 5
        taskManager.addSubtask(subtask);

        title = "Подзадача 3";
        description = "Описание подзадачи 3";
        status = Status.DONE;
        epicId = 3;
        subtask = new Subtask(title, description, taskManager.counter, status, epicId);// id = 6
        taskManager.addSubtask(subtask);
    }

    public static void printAllTasks(TaskManager taskManager) {
        System.out.println("=== === ===");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic);
        }
        for (Subtask subtask : taskManager.getAllSubtasks()) {
            System.out.println(subtask);
        }
        System.out.println("Список подзадач эпика с id = 2");
        System.out.println("--- --- ---");
        for (Subtask subtask : taskManager.getAllSubtasksByEpicId(2)) {
            System.out.println(subtask);
        }
        System.out.println("=== === ===");
    }

    public static void deleteAllTasks(TaskManager taskManager) {
        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();
        taskManager.deleteAllSubtasks();
    }

    public static void getById(TaskManager taskManager) {
        System.out.println("Поиск задачи по id = 1");
        System.out.println("--- --- ---");
        Task task = taskManager.getById(1);
        System.out.println(task);

        System.out.println("Поиск задачи по id = 2");
        System.out.println("--- --- ---");
        task = taskManager.getById(2);
        System.out.println(task);

        System.out.println("Поиск задачи по id = 5");
        System.out.println("--- --- ---");
        task = taskManager.getById(5);
        System.out.println(task);
    }

    public static void updateTask(TaskManager taskManager) {
        String title = "Обновленная Задача 1";
        String description = "Обновленное Описание задачи 1";
        int id = 0;
        Status status = Status.DONE;
        Task task = new Task(title, description, id, status);// id = 0
        taskManager.updateTask(task);

        title = "Обновленный Эпик 2";
        description = "Обновленное Описание эпика 2";
        id = 3;
        Epic epic = new Epic(title, description, id);// id = 3
        taskManager.updateEpic(epic);

        title = "Обновленная Подзадача 1";
        description = "Обновленное Описание подзадачи 1";
        status = Status.DONE;
        int epicId = 2;
        id = 4;
        Subtask subtask = new Subtask(title, description, id, status, epicId);// id = 4
        taskManager.updateSubtask(subtask);
    }

    public static void deleteById(TaskManager taskManager) {
        taskManager.deleteById(2);
    }
}
