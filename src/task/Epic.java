package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtasksId = new ArrayList<>();
    private LocalDateTime endTime;

    public List<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void setSubtasksId(List<Integer> subtasksId) {
        this.subtasksId = subtasksId;
    }

    public Epic(int id, String name, String description) {
        super(id, name, description, null, null);
        this.type = TaskType.EPIC;
    }

    public Epic(Epic epic) {
        super(epic);
        this.subtasksId = new ArrayList<>(epic.subtasksId);
        this.type = TaskType.EPIC;
    }

    public Epic(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status, null, null);
        this.type = TaskType.EPIC;
    }

    public void addSubtaskId(int subtaskId) {
        subtasksId.add(subtaskId);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        String text = name + " | Status: " + status + ", id: " + id + "\n";
        text += description + "\n";
        text += "Содержит подзадачи с id = " + subtasksId + "\n";
        if (startTime != null)
            text += "Начало: " + startTime.format(dateTimeFormatter) + "\n";
        if (duration != null)
            text += "Продолжительность: " + duration.toMinutes() + "\n";
        if (endTime != null)
            text += "Конец: " + endTime.format(dateTimeFormatter) + "\n";
        return text;
    }

    @Override
    public Task copy() {
        return new Epic(this);
    }
}
