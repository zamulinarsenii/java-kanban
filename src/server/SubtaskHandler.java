package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Subtask;
import task.Task;
import task.TaskType;

import java.io.IOException;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public SubtaskHandler(TaskManager taskManager) {
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
                Subtask newSubtask = gson.fromJson(body, Subtask.class);
                newSubtask.setId(taskManager.getCounter());
                taskManager.addSubtask(newSubtask);
                sendText(httpExchange, "subtask с ID " + newSubtask.getId() + " создан");
            } catch (Exception e) {
                sendBadRequest(httpExchange, "Ошибка при создании subtask: " + e.getMessage());
            }
        } else if (pathParts.length == 3) {
            try {
                int id = Integer.parseInt(pathParts[2]);
                Subtask updatedSubtask = gson.fromJson(body, Subtask.class);
                updatedSubtask.setId(id);
                Task task = taskManager.getById(id);
                if (task != null && task.getType() == TaskType.SUBTASK) {
                    taskManager.updateSubtask(updatedSubtask);
                    sendText(httpExchange, "subtask с ID " + id + " обновлен");
                } else {
                    sendNotFound(httpExchange, "subtask с ID " + id + " не найден");
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
            List<Task> subtasks = taskManager.getAllSubtasks();
            String response = gson.toJson(subtasks);
            sendText(httpExchange, response);
            return;
        }

        try {
            int id = Integer.parseInt(pathParts[2]);
            Task subtask = taskManager.getById(id);
            if (subtask != null && subtask.getType() == TaskType.SUBTASK) {
                String response = gson.toJson(subtask);
                sendText(httpExchange, response);
            } else {
                sendNotFound(httpExchange, "subtask с ID " + id + " не найден");
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
            Task subtask = taskManager.getById(id);
            if (subtask != null && subtask.getType() == TaskType.SUBTASK) {
                taskManager.deleteById(id);
                sendText(httpExchange, "subtask с ID " + id + " удален");
            } else {
                sendNotFound(httpExchange, "subtask с ID " + id + " не найден");
            }
        } catch (NumberFormatException e) {
            sendBadRequest(httpExchange, "Неверный формат ID");
        }
    }
}