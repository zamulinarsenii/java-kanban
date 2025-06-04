package manager;

import manager.TaskManager;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int counter = 0;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public int getCounter() {
        counter++;
        return counter;
    }

    @Override
    public void addTask(Task task) {
        Task copy = new Task(task);
        copy.setId(getCounter());
        tasks.put(copy.getId(), copy);
    }

    @Override
    public void addEpic(Epic epic) {
        Epic copy = new Epic(epic);
        copy.setId(getCounter());
        epics.put(copy.getId(), copy);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (subtask.getEpicId() == subtask.getId() || !epics.containsKey(subtask.getEpicId())) {
            return;
        }
        Subtask copy = new Subtask(subtask);
        copy.setId(getCounter());
        subtasks.put(copy.getId(), copy);
        epics.get(copy.getEpicId()).getSubtasksId().add(copy.getId());
        updateEpicStatus(copy);
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

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Task> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Task> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Task> getAllSubtasksByEpicId(int epicId) {
        ArrayList<Task> subtaskArrayList = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == epicId) {
                subtaskArrayList.add(subtask);
            }
        }
        return subtaskArrayList;
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.setSubtasksId(new ArrayList<>());
            epic.setStatus(Status.NEW);
        }
        subtasks.clear();
    }

    @Override
    public Task getById(int id) {
        Task task = null;
        if (tasks.containsKey(id)) {
            task = tasks.get(id);
        } else if (epics.containsKey(id)) {
            task = epics.get(id);
        } else if (subtasks.containsKey(id)) {
            task = subtasks.get(id);
        }

        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
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

    @Override
    public void updateTask(Task newTask) {
        if (tasks.containsKey(newTask.getId())) {
            tasks.put(newTask.getId(), newTask);
        }
    }

    @Override
    public void updateEpic(Epic newEpic) {
        if (epics.containsKey(newEpic.getId())) {
            epics.put(newEpic.getId(), newEpic);
        }
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        if (subtasks.containsKey(newSubtask.getId())) {
            subtasks.put(newSubtask.getId(), newSubtask);
            updateEpicStatus(newSubtask);
        }
    }
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
