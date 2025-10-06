package server;

import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.*;
import task.Task;
import task.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTasksTest {
    private HttpTaskServer taskServer;
    private TaskManager manager;
    private Gson gson;
    private HttpClient client;

    @BeforeEach
    void setUp() throws IOException {
        manager = new InMemoryTaskManager();
        gson = new BaseHttpHandler().createGson();
        client = HttpClient.newHttpClient();

        taskServer = new HttpTaskServer(manager);
        taskServer.start();
    }

    @AfterEach
    void tearDown() {
        taskServer.stop();
    }

    @Test
    void shouldAddTaskViaPostRequest() throws IOException, InterruptedException {
        Task task = new Task(manager.getCounter(), "Test task", "Test desc", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(15));
        String json = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Ожидался статус 200 при создании задачи");

        List<Task> tasks = manager.getAllTasks();
        assertEquals(1, tasks.size(), "Ожидалась одна задача в менеджере");
        assertEquals("Test task", tasks.get(0).getName());
    }

    @Test
    void shouldReturnAllTasksViaGetRequest() throws IOException, InterruptedException {
        Task task = new Task(manager.getCounter(), "Task1", "Desc", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(10));
        manager.addTask(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Ожидался статус 200 при запросе всех задач");

        Task[] tasks = gson.fromJson(response.body(), Task[].class);
        assertEquals(1, tasks.length);
        assertEquals("Task1", tasks[0].getName());
    }

    @Test
    void shouldDeleteTaskById() throws IOException, InterruptedException {
        Task task = new Task(manager.getCounter(), "Task to delete", "Desc", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(20));
        manager.addTask(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/" + task.getId()))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Ожидался статус 200 при удалении задачи");

        assertTrue(manager.getAllTasks().isEmpty(), "После удаления задач не должно остаться");
    }

    @Test
    void shouldReturnNotFoundForInvalidPath() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/invalid"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Неверный путь должен вернуть 404");
    }
}
