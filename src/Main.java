import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String command;
        do {
            openMenu();
            command = getTextFromConsole();
            switch (command) {
                case "1":
                    addTask();
                    break;
                case "2":
                    addEpic();
                    break;
                case "3":
                    printOutTasks();
                    break;
                case "4":
                    deleteAllTasks();
                    break;
                case "5":
                    getById();
                    break;
                case "6":
                    updateTask();
                    break;
                case "7":
                    deleteById();
                    break;
                case "0":
                    return;
            }
        } while (!command.isEmpty());
    }

    public static void openMenu() {
        String menu = "Выберите пункт меню:\n"
                + "1 - Добавить новую задачу\n"
                + "2 - Добавить новый эпик\n"
                + "3 - Получить список всех задач\n";
        if (TaskManager.counter != 0) {
            menu += "4 - Удаление всех задач\n"
                    + "5 - Получение по идентификатору\n"
                    + "6 - Обновить задачу\n"
                    + "7 - Удалить по идентификатору\n";
        }
        menu += "0 - Выход из программы";
        System.out.println(menu);
    }

    public static void addTask() {
        TaskManager taskManager = new TaskManager();
        taskManager.add(createTask());
        System.out.println("Добавление задачи завершено");
    }

    public static Task createTask() {
        System.out.print("Введите название: ");
        String title = getTextFromConsole();
        while (title.isEmpty()) {
            System.out.println("Некорректное название для задачи!");
            System.out.print("Введите другое название: ");
            title = getTextFromConsole();
        }
        ArrayList<String> tasks = new ArrayList<>();
        String taskStr;
        System.out.println("(Для завершения нажмите Enter)");
        System.out.println("Введите список задач: ");
        do {
            taskStr = getTextFromConsole();
            if (!taskStr.isEmpty())
                tasks.add(taskStr);
        } while (!taskStr.isEmpty());
        System.out.println("Укажите статус для задачи:\n1 - NEW\n2 - DONE");
        String statusStr = getTextFromConsole();
        while (!statusStr.equals("1") && !statusStr.equals("2")) {
            System.out.println("Некорректный статус для задачи!");
            System.out.print("Введите другой статус: ");
            statusStr = getTextFromConsole();
        }
        Status status;
        if (statusStr.equals("1")) {
            status = Status.NEW;
        } else {
            status = Status.DONE;
        }
        Task task = new Task(title, status, tasks);
        return task;
    }

    public static void addEpic() {
        TaskManager taskManager = new TaskManager();
        taskManager.add(createEpic());
        System.out.println("Добавление эпика завершено");
    }

    public static Epic createEpic() {
        System.out.print("Введите название: ");
        String title = getTextFromConsole();
        while (title.isEmpty()) {
            System.out.println("Некорректное название для эпика!");
            System.out.print("Введите другое название: ");
            title = getTextFromConsole();
        }
        ArrayList<Subtask> subtasks = new ArrayList<>();
        String subtaskTitle;
        do {
            System.out.println("(Для завершения нажмите Enter)");
            System.out.println("Введите название подзадачи: ");
            subtaskTitle = getTextFromConsole();
            if (subtaskTitle.isEmpty()) {
                break;
            }
            System.out.println("(Для завершения нажмите Enter)");
            System.out.println("Введите список задач: ");
            String taskStr;
            ArrayList<String> tasks = new ArrayList<>();
            do {
                taskStr = getTextFromConsole();
                if (!taskStr.isEmpty())
                    tasks.add(taskStr);
            } while (!taskStr.isEmpty());
            System.out.println("Укажите статус для подзадачи:\n1 - NEW\n2 - DONE");
            String statusStr = getTextFromConsole();
            while (!statusStr.equals("1") && !statusStr.equals("2")) {
                System.out.println("Некорректный статус для подзадачи!");
                System.out.print("Введите другой статус: ");
                statusStr = getTextFromConsole();
            }
            Status status;
            if (statusStr.equals("1")) {
                status = Status.NEW;
            } else {
                status = Status.DONE;
            }
            subtasks.add(new Subtask(subtaskTitle, status, tasks));
        } while (!subtaskTitle.isEmpty());
        Epic epic = new Epic(title, subtasks);
        return epic;
    }

    public static void printOutTasks() {
        TaskManager taskManager = new TaskManager();
        if (TaskManager.counter == 0) {
            System.out.println("none");
        } else {
            System.out.println("Список задач:");
            String toDo = "";
            String inProgress = "";
            String done = "";
            for (TaskSkeleton task : taskManager.getNewTasks()) {
                    toDo += task;
            }
            for (TaskSkeleton task : taskManager.getDoneTasks()) {
                    done += task;
            }
            for (TaskSkeleton task : taskManager.getInProgressTasks()) {
                inProgress += task;
            }
            if (toDo.equals("")){
                System.out.println("| To do:\nnone");
            }else {
                System.out.println("| To do:\n"+ toDo);
            }
            if (inProgress.equals("")){
                System.out.println("| In Progress:\nnone");
            }else {
                System.out.println("| In Progress:\n"+ inProgress);
            }
            if (done.equals("")){
                System.out.println("| Done:\nnone");
            }else {
                System.out.println("| Done:\n"+ done);
            }
            System.out.println();
        }
    }

    public static void deleteAllTasks() {
        TaskManager taskManager = new TaskManager();
        taskManager.deleteAllTasks();
        System.out.println("Все задачи были удалены!");
    }

    public static void getById() {
        TaskManager taskManager = new TaskManager();
        System.out.print("Введите ID задачи до " + (TaskManager.counter - 1) + ": ");
        int id = Integer.parseInt(getTextFromConsole());
        while (id >= TaskManager.counter) {
            System.out.print("Задачи с таким ID нет, укажите ID до " + (TaskManager.counter - 1) + ": ");
            id = Integer.parseInt(getTextFromConsole());
        }
        System.out.println("Задача с ID = " + id + ":");
        System.out.println(taskManager.getById(id));
    }

    public static void updateTask() {
        TaskManager taskManager = new TaskManager();
        System.out.print("Введите ID задачи для обновления: ");
        int id = Integer.parseInt(getTextFromConsole());
        while (id >= TaskManager.counter) {
            System.out.print("Задачи с таким ID нет, укажите ID до " + (TaskManager.counter - 1) + ": ");
            id = Integer.parseInt(getTextFromConsole());
        }
        if (TaskManager.tasks.get(id).type == TypeOfTask.TASK) {
            taskManager.updateTask(id, createTask());
        } else {
            taskManager.updateTask(id, createEpic());
        }
    }

    public static void deleteById() {
        TaskManager taskManager = new TaskManager();
        System.out.print("Введите ID задачи до " + (TaskManager.counter - 1) + ": ");
        int id = Integer.parseInt(getTextFromConsole());
        while (id >= TaskManager.counter) {
            System.out.print("Задачи с таким ID нет, укажите ID до " + (TaskManager.counter - 1) + ": ");
            id = Integer.parseInt(getTextFromConsole());
        }
        taskManager.deleteById(id);
        System.out.println("Задача с ID = " + id + " была удалена");
    }

    public static String getTextFromConsole() {
        return scanner.nextLine().trim();
    }
}
