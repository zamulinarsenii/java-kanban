package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        switch (method) {
            case "GET":
                handleGetRequest(httpExchange);
                break;
            default:
                sendUncorrect(httpExchange, "Некорректный метод!");
        }
    }

    private void handleGetRequest(HttpExchange httpExchange) throws IOException {
        String[] pathParts = getPathParts(httpExchange);
        if (pathParts.length > 2) {
            sendNotFound(httpExchange, "Неверный путь запроса!");
            return;
        }
        Gson gson = createGson();

        if (pathParts.length == 2) {
            List<Task> tasks = taskManager.getHistory();
            String response = gson.toJson(tasks);
            sendText(httpExchange, response);
        }
    }
}
