import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        addTask(inMemoryTaskManager);
        addEpic(inMemoryTaskManager);
        addSubtask(inMemoryTaskManager);
        printAllTasks(inMemoryTaskManager);
        getById(inMemoryTaskManager);
        updateTask(inMemoryTaskManager);
        printAllTasks(inMemoryTaskManager);
        deleteById(inMemoryTaskManager);
        printAllTasks(inMemoryTaskManager);
        deleteAllTasks(inMemoryTaskManager);
        printAllTasks(inMemoryTaskManager);
    }

    public static void addTask(TaskManager inMemoryTaskManager) {
        String title = "Задача 1";
        String description = "Описание задачи 1";
        Status status = Status.NEW;
        Task task = new Task(title, description, inMemoryTaskManager.getCounter(), status);// id = 0
        inMemoryTaskManager.addTask(task);

        title = "Задача 2";
        description = "Описание задачи 2";
        status = Status.DONE;
        task = new Task(title, description, inMemoryTaskManager.getCounter(), status);// id = 1
        inMemoryTaskManager.addTask(task);
    }

    public static void addEpic(TaskManager inMemoryTaskManager) {
        String title = "Эпик 1";
        String description = "Описание эпика 1";
        Epic epic = new Epic(title, description, inMemoryTaskManager.getCounter());// id = 2
        inMemoryTaskManager.addEpic(epic);

        title = "Эпик 2";
        description = "Описание эпика 2";
        epic = new Epic(title, description, inMemoryTaskManager.getCounter());// id = 3
        inMemoryTaskManager.addEpic(epic);
    }

    public static void addSubtask(TaskManager inMemoryTaskManager) {
        String title = "Подзадача 1";
        String description = "Описание подзадачи 1";
        Status status = Status.NEW;
        int epicId = 2;
        Subtask subtask = new Subtask(title, description, inMemoryTaskManager.getCounter(), status, epicId);// id = 4
        inMemoryTaskManager.addSubtask(subtask);

        title = "Подзадача 2";
        description = "Описание подзадачи 2";
        status = Status.DONE;
        epicId = 2;
        subtask = new Subtask(title, description, inMemoryTaskManager.getCounter(), status, epicId);// id = 5
        inMemoryTaskManager.addSubtask(subtask);

        title = "Подзадача 3";
        description = "Описание подзадачи 3";
        status = Status.DONE;
        epicId = 3;
        subtask = new Subtask(title, description, inMemoryTaskManager.getCounter(), status, epicId);// id = 6
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
        String title = "Обновленная Задача 1";
        String description = "Обновленное Описание задачи 1";
        int id = 0;
        Status status = Status.DONE;
        Task task = new Task(title, description, id, status);// id = 0
        inMemoryTaskManager.updateTask(task);

        title = "Обновленный Эпик 2";
        description = "Обновленное Описание эпика 2";
        id = 3;
        Epic epic = new Epic(title, description, id);// id = 3
        inMemoryTaskManager.updateEpic(epic);

        title = "Обновленная Подзадача 1";
        description = "Обновленное Описание подзадачи 1";
        status = Status.DONE;
        int epicId = 2;
        id = 4;
        Subtask subtask = new Subtask(title, description, id, status, epicId);// id = 4
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
