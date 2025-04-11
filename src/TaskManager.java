import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    public int counter = 0;
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public void addTask(Task task) {
        tasks.put(counter, task);
        counter++;
    }

    public void addEpic(Epic epic) {
        epics.put(counter, epic);
        counter++;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.put(counter, subtask);
        epics.get(subtask.epicId).subtasksId.add(subtask.id);
        updateEpicStatus(subtask);
        counter++;
    }

    public void updateEpicStatus(Subtask subtask) {
        ArrayList<Status> statuses = new ArrayList<>();
        for (Integer subtaskId : epics.get(subtask.epicId).subtasksId) {
            statuses.add(subtasks.get(subtaskId).status);
        }
        if (statuses.contains(Status.NEW) && statuses.contains(Status.DONE)) {
            epics.get(subtask.epicId).status = Status.IN_PROGRESS;
        } else if (statuses.contains(Status.DONE)) {
            epics.get(subtask.epicId).status = Status.DONE;
        } else {
            epics.get(subtask.epicId).status = Status.NEW;
        }
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> taskArrayList = new ArrayList<>();
        for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
            taskArrayList.add(entry.getValue());
        }
        return taskArrayList;
    }

    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> epicArrayList = new ArrayList<>();
        for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
            epicArrayList.add(entry.getValue());
        }
        return epicArrayList;
    }

    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>();
        for (Map.Entry<Integer, Subtask> entry : subtasks.entrySet()) {
            subtaskArrayList.add(entry.getValue());
        }
        return subtaskArrayList;
    }

    public ArrayList<Subtask> getAllSubtasksByEpicId(int epicId) {
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>();
        for (Map.Entry<Integer, Subtask> entry : subtasks.entrySet()) {
            Subtask subtask = entry.getValue();
            if (subtask.epicId == epicId) {
                subtaskArrayList.add(entry.getValue());
            }
        }
        return subtaskArrayList;
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    public void deleteAllSubtasks() {
        for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
            Epic epic = entry.getValue();
            epic.subtasksId = new ArrayList<>();
            epic.status = Status.NEW;
        }
        subtasks.clear();
    }

    public Task getById(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else if (epics.containsKey(id)) {
            return epics.get(id);
        } else if (subtasks.containsKey(id)) {
            return subtasks.get(id);
        }
        return null;
    }

    public void deleteById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            for (Integer subtaskId : epics.get(id).subtasksId) {
                subtasks.remove(subtaskId);
            }
            epics.remove(id);
        } else if (subtasks.containsKey(id)) {
            epics.get(subtasks.get(id).epicId).subtasksId.remove(id);
            subtasks.remove(id);
        }
    }

    public void updateTask(Task newTask) {
        if (tasks.containsKey(newTask.id)) {
            tasks.put(newTask.id, newTask);
        }
    }

    public void updateEpic(Epic newEpic) {
        if (epics.containsKey(newEpic.id)) {
            for (Integer subtaskId : epics.get(newEpic.id).subtasksId) {
                subtasks.remove(subtaskId);
            }
            epics.put(newEpic.id, newEpic);
        }
    }

    public void updateSubtask(Subtask newSubtask) {
        if (subtasks.containsKey(newSubtask.id)) {
            subtasks.put(newSubtask.id, newSubtask);
            updateEpicStatus(newSubtask);
        }
    }
}
