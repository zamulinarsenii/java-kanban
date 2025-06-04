package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    int getCounter();
    void addTask(Task task);
    void addEpic(Epic epic);
    void addSubtask(Subtask subtask);
    List<Task> getAllTasks();
    List<Task> getAllEpics();
    List<Task> getAllSubtasks();
    List<Task> getAllSubtasksByEpicId(int epicId);
    void deleteAllTasks();
    void deleteAllEpics();
    void deleteAllSubtasks();
    Task getById(int id);
    void deleteById(int id);
    void updateTask(Task newTask);
    void updateEpic(Epic newEpic);
    void updateSubtask(Subtask newSubtask);
    List<Task> getHistory();
}
