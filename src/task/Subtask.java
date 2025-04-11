package task;

public class Subtask extends Task {
    public int epicId;

    public Subtask(String title, String description, int id, Status status, int epicId) {
        super(title, description, id, status);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        String text = title + " | Status: " + status + ", id: " + id + "\n";
        text += description + "\n";
        text+= "Принадлежит эпику с id = "+epicId+ "\n";
        return text;
    }
}
