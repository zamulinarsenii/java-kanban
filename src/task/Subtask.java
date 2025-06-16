package task;

import manager.TaskType;

public class Subtask extends Task {
    private final int epicId;

    public int getEpicId() {
        return epicId;
    }

    public Subtask(String title, String description, int id, Status status, int epicId) {
        super(title, description, id, status);
        this.epicId = epicId;
        this.type = TaskType.SUBTASK;
    }

    public Subtask(Subtask subtask) {
        super(subtask);
        this.epicId = subtask.epicId;
        this.type = TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        String text = name + " | Status: " + status + ", id: " + id + "\n";
        text += description + "\n";
        text += "Принадлежит эпику с id = " + epicId + "\n";
        return text;
    }

    @Override
    public Task copy() {
        return new Subtask(this);
    }
}
