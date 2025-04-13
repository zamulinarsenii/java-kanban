package task;

public class Subtask extends Task {
    private int epicId;

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    public Subtask(String title, String description, int id, Status status, int epicId) {
        super(title, description, id, status);
        this.epicId = epicId;
    }

    public Subtask(Subtask subtask) {
        super(subtask);
        this.epicId = subtask.epicId;
    }

    @Override
    public String toString() {
        String text = title + " | Status: " + status + ", id: " + id + "\n";
        text += description + "\n";
        text += "Принадлежит эпику с id = " + epicId + "\n";
        return text;
    }
}
