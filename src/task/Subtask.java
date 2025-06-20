package task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private final int epicId;


    public Subtask(int id, String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration, int epicId) {
        super(id, name, description, status, startTime, duration);
        this.epicId = epicId;
        this.type = TaskType.SUBTASK;
    }

    public Subtask(Subtask subtask) {
        super(subtask);
        this.epicId = subtask.epicId;
        this.type = TaskType.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        String text = name + " | Status: " + status + ", id: " + id + "\n";
        text += description + "\n";
        text += "Принадлежит эпику с id = " + epicId + "\n";
        text += "Начало: " + startTime.format(dateTimeFormatter) + "\n";
        text += "Продолжительность: " + duration.toMinutes() + "\n";
        return text;
    }

    @Override
    public Task copy() {
        return new Subtask(this);
    }
}
