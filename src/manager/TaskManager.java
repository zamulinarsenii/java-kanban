package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;

public interface TaskManager {
    int getCounter();
    void setCounter(int counter);
    void addTask(Task task);
    void addEpic(Epic epic);
    void addSubtask(Subtask subtask);
    ArrayList<Task> getAllTasks();
    ArrayList<Epic> getAllEpics();
    ArrayList<Subtask> getAllSubtasks();
    ArrayList<Subtask> getAllSubtasksByEpicId(int epicId);
    void deleteAllTasks();
    void deleteAllEpics();
    void deleteAllSubtasks();
    Task getById(int id);
    void deleteById(int id);
    void updateTask(Task newTask);
    void updateEpic(Epic newEpic);
    void updateSubtask(Subtask newSubtask);
    ArrayList<Task> getHistory();
}
