import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {


    private int counter = 0;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
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
        epics.get(subtask.getEpicId()).getSubtasksId().add(subtask.getId());
        updateEpicStatus(subtask);
        counter++;
    }

    private void updateEpicStatus(Subtask subtask) {
        ArrayList<Status> statuses = new ArrayList<>();
        for (Integer subtaskId : epics.get(subtask.getEpicId()).getSubtasksId()) {
            statuses.add(subtasks.get(subtaskId).getStatus());
        }
        if (statuses.contains(Status.NEW) && statuses.contains(Status.DONE)) {
            epics.get(subtask.getEpicId()).setStatus(Status.IN_PROGRESS);
        } else if (statuses.contains(Status.DONE)) {
            epics.get(subtask.getEpicId()).setStatus(Status.DONE);
        } else {
            epics.get(subtask.getEpicId()).setStatus(Status.NEW);
        }
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Subtask> getAllSubtasksByEpicId(int epicId) {
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == epicId) {
                subtaskArrayList.add(subtask);
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
        for (Epic epic : epics.values()) {
            epic.setSubtasksId(new ArrayList<>());
            epic.setStatus(Status.NEW);
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
            for (Integer subtaskId : epics.get(id).getSubtasksId()) {
                subtasks.remove(subtaskId);
            }
            epics.remove(id);
        } else if (subtasks.containsKey(id)) {
            epics.get(subtasks.get(id).getEpicId()).getSubtasksId().remove(id);
            updateEpicStatus(subtasks.get(id));
            subtasks.remove(id);
        }
    }

    public void updateTask(Task newTask) {
        if (tasks.containsKey(newTask.getId())) {
            tasks.put(newTask.getId(), newTask);
        }
    }

    public void updateEpic(Epic newEpic) {
        if (epics.containsKey(newEpic.getId())) {
            epics.put(newEpic.getId(), newEpic);
        }
    }

    public void updateSubtask(Subtask newSubtask) {
        if (subtasks.containsKey(newSubtask.getId())) {
            subtasks.put(newSubtask.getId(), newSubtask);
            updateEpicStatus(newSubtask);
        }
    }
}
