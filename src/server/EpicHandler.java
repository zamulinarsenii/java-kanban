package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Epic;
import task.Task;
import task.TaskType;

import java.io.IOException;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
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
                Epic newEpic = gson.fromJson(body, Epic.class);
                newEpic.setId(taskManager.getCounter());
                taskManager.addEpic(newEpic);
                sendText(httpExchange, "epic с ID " + newEpic.getId() + " создан");
            } catch (Exception e) {
                sendBadRequest(httpExchange, "Ошибка при создании subtask: " + e.getMessage());
            }
        } else if (pathParts.length == 3) {
            try {
                int id = Integer.parseInt(pathParts[2]);
                Epic updatedEpic = gson.fromJson(body, Epic.class);
                updatedEpic.setId(id);
                Task epic = taskManager.getById(id);
                if (epic != null && epic.getType() == TaskType.EPIC) {
                    taskManager.updateEpic(updatedEpic);
                    sendText(httpExchange, "epic с ID " + id + " обновлен");
                } else {
                    sendNotFound(httpExchange, "epic с ID " + id + " не найден");
                }
            } catch (NumberFormatException e) {
                sendBadRequest(httpExchange, "Неверный формат ID");
            } catch (Exception e) {
                sendBadRequest(httpExchange, "Ошибка при обновлении epic: " + e.getMessage());
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
            List<Task> epics = taskManager.getAllEpics();
            String response = gson.toJson(epics);
            sendText(httpExchange, response);
            return;
        }

        try {
            int id = Integer.parseInt(pathParts[2]);
            Task epic = taskManager.getById(id);
            if (epic != null && epic.getType() == TaskType.EPIC) {
                String response = gson.toJson(epic);
                sendText(httpExchange, response);
            } else {
                sendNotFound(httpExchange, "epic с ID " + id + " не найден");
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
            Task epic = taskManager.getById(id);
            if (epic != null && epic.getType() == TaskType.EPIC) {
                taskManager.deleteById(id);
                sendText(httpExchange, "epic с ID " + id + " удален");
            } else {
                sendNotFound(httpExchange, "epic с ID " + id + " не найден");
            }
        } catch (NumberFormatException e) {
            sendBadRequest(httpExchange, "Неверный формат ID");
        }
    }
}