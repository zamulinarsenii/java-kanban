package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Task;
import task.TaskType;

import java.io.IOException;
import java.util.List;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        switch (method) {
            case "POST":
                handlePostRequest(httpExchange);
                break;
            case "GET":
                handleGetRequest(httpExchange);
                break;
            case "DELETE":
                handleDeleteRequest(httpExchange);
                break;
            default:
                sendUncorrect(httpExchange, "Некорректный метод!");
        }
    }

    private void handlePostRequest(HttpExchange httpExchange) throws IOException {
        String[] pathParts = getPathParts(httpExchange);
        if (pathParts.length > 3) {
            sendNotFound(httpExchange, "Неверный путь запроса!");
            return;
        }

        Gson gson = createGson();
        String body = readRequestBody(httpExchange);

        if (pathParts.length == 2) {
            try {
                Task newTask = gson.fromJson(body, Task.class);
                newTask.setId(taskManager.getCounter());
                taskManager.addTask(newTask);
                sendText(httpExchange, "task с ID " + newTask.getId() + " создан");
            } catch (Exception e) {
                sendBadRequest(httpExchange, "Ошибка при создании задачи: " + e.getMessage());
            }
        } else if (pathParts.length == 3) {
            try {
                int id = Integer.parseInt(pathParts[2]);
                Task updatedTask = gson.fromJson(body, Task.class);
                updatedTask.setId(id);
                Task task = taskManager.getById(id);
                if (task != null && task.getType() == TaskType.TASK) {
                    taskManager.updateTask(updatedTask);
                    sendText(httpExchange, "task с ID " + id + " обновлен");
                } else {
                    sendNotFound(httpExchange, "task с ID " + id + " не найден");
                }
            } catch (NumberFormatException e) {
                sendBadRequest(httpExchange, "Неверный формат ID");
            } catch (Exception e) {
                sendBadRequest(httpExchange, "Ошибка при обновлении задачи: " + e.getMessage());
            }
        }
    }

    private void handleGetRequest(HttpExchange httpExchange) throws IOException {
        String[] pathParts = getPathParts(httpExchange);
        if (pathParts.length > 3) {
            sendNotFound(httpExchange, "Неверный путь запроса!");
            return;
        }
        Gson gson = createGson();

        if (pathParts.length == 2) {
            List<Task> tasks = taskManager.getAllTasks();
            String response = gson.toJson(tasks);
            sendText(httpExchange, response);
            return;
        }
        try {
            int id = Integer.parseInt(pathParts[2]);
            Task task = taskManager.getById(id);
            if (task != null && task.getType() == TaskType.TASK) {
                String response = gson.toJson(task);
                sendText(httpExchange, response);
            } else {
                sendNotFound(httpExchange, "task с ID " + id + " не найден");
            }
        } catch (NumberFormatException e) {
            sendBadRequest(httpExchange, "Неверный формат ID");
        }
    }

    private void handleDeleteRequest(HttpExchange httpExchange) throws IOException {
        String[] pathParts = getPathParts(httpExchange);
        if (pathParts.length != 3) {
            sendNotFound(httpExchange, "Неверный путь запроса!");
            return;
        }
        try {
            int id = Integer.parseInt(pathParts[2]);
            Task task = taskManager.getById(id);
            if (task != null && task.getType() == TaskType.TASK) {
                taskManager.deleteById(id);
                sendText(httpExchange, "task с ID " + id + " удален");
            } else {
                sendNotFound(httpExchange, "task с ID " + id + " не найден");
            }
        } catch (NumberFormatException e) {
            sendBadRequest(httpExchange, "Неверный формат ID");
        }
    }
}
