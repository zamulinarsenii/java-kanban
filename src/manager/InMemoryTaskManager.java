package manager;

import task.Epic;
import task.TaskStatus;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int counter = -1;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final Set<Task> prioritizedTasks = new TreeSet<>(new TaskStartTimeComparator());

    @Override
    public int getCounter() {
        counter++;
        return counter;
    }

    @Override
    public void addTask(Task task) {
        Task copy = new Task(task);

        hasIntersections(copy);
        tasks.put(copy.getId(), copy);
        prioritizedTasks.add(copy);
    }

    @Override
    public void addEpic(Epic epic) {
        Epic copy = new Epic(epic);
        epics.put(copy.getId(), copy);
        // У эпика startTime может быть null, но пусть добавляется в TreeSet
        if(copy.getStartTime() != null)
            prioritizedTasks.add(copy);

    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (subtask.getEpicId() == subtask.getId() || !epics.containsKey(subtask.getEpicId())) {
            return;
        }
        Subtask copy = new Subtask(subtask);

        hasIntersections(copy);
        subtasks.put(copy.getId(), copy);
        epics.get(copy.getEpicId()).addSubtaskId(copy.getId());
        prioritizedTasks.add(copy);
        updateEpicFields(copy.getEpicId());
    }

    protected void updateEpicFields(int epicId) {
        Epic epic = epics.get(epicId);
        List<Integer> subtaskIds = epic.getSubtasksId();

        if (subtaskIds.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(Duration.ZERO);
            return;
        }

        // Статус
        boolean allNew = subtaskIds.stream()
                .allMatch(id -> subtasks.get(id).getStatus() == TaskStatus.NEW);
        boolean allDone = subtaskIds.stream()
                .allMatch(id -> subtasks.get(id).getStatus() == TaskStatus.DONE);
        if (allNew) {
            epic.setStatus(TaskStatus.NEW);
        } else if (allDone) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }

        // Время
        LocalDateTime minStartTime = subtaskIds.stream()
                .map(id -> subtasks.get(id).getStartTime())
                .filter(Objects::nonNull)
                .min(Comparator.naturalOrder())
                .orElse(null);
        epic.setStartTime(minStartTime);

        Duration totalDuration = subtaskIds.stream()
                .map(id -> subtasks.get(id).getDuration())
                .reduce(Duration.ZERO, Duration::plus);
        epic.setDuration(totalDuration);

        LocalDateTime maxEndTime = subtaskIds.stream()
                .map(id -> {
                    Subtask s = subtasks.get(id);
                    return s.getStartTime().plus(s.getDuration());
                })
                .max(Comparator.naturalOrder())
                .orElse(null);
        epic.setEndTime(maxEndTime);
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
        return subtasks.values().stream()
                .filter(subtask -> subtask.getEpicId() == epicId)
                .map(subtask -> (Task) subtask)
                .toList();
    }

    @Override
    public void deleteAllTasks() {
        for (Task task : tasks.values()) {
            prioritizedTasks.remove(task);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.values().forEach(prioritizedTasks::remove);
        for (Subtask subtask : subtasks.values()) {
            prioritizedTasks.remove(subtask);
        }
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.values().forEach(prioritizedTasks::remove);
        for (Epic epic : epics.values()) {
            epic.setSubtasksId(new ArrayList<>());
            epic.setStatus(TaskStatus.NEW);
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(Duration.ZERO);
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
            prioritizedTasks.remove(tasks.get(id));
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            epics.get(id).getSubtasksId().forEach(subtaskId -> {
                prioritizedTasks.remove(subtasks.get(subtaskId));
                subtasks.remove(subtaskId);
            });
            epics.remove(id);
        } else if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            prioritizedTasks.remove(subtask);
            epics.get(subtask.getEpicId()).getSubtasksId().remove(Integer.valueOf(id));
            subtasks.remove(id);
            updateEpicFields(subtask.getEpicId());
        }
    }

    @Override
    public void updateTask(Task newTask) {
        if (tasks.containsKey(newTask.getId())) {
            prioritizedTasks.remove(tasks.get(newTask.getId()));
            if (hasIntersections(newTask)) {
                prioritizedTasks.add(tasks.get(newTask.getId())); // откатываем обратно\
            }
            tasks.put(newTask.getId(), newTask);
            prioritizedTasks.add(newTask);
        }
    }

    @Override
    public void updateEpic(Epic newEpic) {
        if (epics.containsKey(newEpic.getId())) {
            prioritizedTasks.remove(epics.get(newEpic.getId()));

            epics.put(newEpic.getId(), newEpic);
        }
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        if (subtasks.containsKey(newSubtask.getId())) {
            prioritizedTasks.remove(subtasks.get(newSubtask.getId()));
            if (hasIntersections(newSubtask)) {
                prioritizedTasks.add(subtasks.get(newSubtask.getId())); // откатываем обратно
            }
            subtasks.put(newSubtask.getId(), newSubtask);
            prioritizedTasks.add(newSubtask);
            updateEpicFields(newSubtask.getEpicId());
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }
    private boolean isOverlapping(Task task1, Task task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null) {
            return false; // если хотя бы одна из задач не имеет времени — считаем, что не пересекаются
        }
        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = start1.plus(task1.getDuration());
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = start2.plus(task2.getDuration());

        return !(end1.isEqual(start2) || end1.isBefore(start2) || end2.isEqual(start1) || end2.isBefore(start1));
    }
    private boolean hasIntersections(Task newTask) {
        if (prioritizedTasks.stream()
                .filter(existingTask -> existingTask.getId() != newTask.getId()) // не сравниваем саму себя
                .anyMatch(existingTask -> isOverlapping(newTask, existingTask))) {
            throw new IllegalArgumentException("Подзадача пересекается по времени с другой задачей.");
        }
        return true;
    }

}
