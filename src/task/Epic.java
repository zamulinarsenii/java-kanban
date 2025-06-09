package task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtasksId = new ArrayList<>();
    public List<Integer> getSubtasksId() {
        return subtasksId;
    }
    public void setSubtasksId(List<Integer> subtasksId) {
        this.subtasksId = subtasksId;
    }
    public Epic(String title, String description, int id) {
        super(title, description, id);
    }
    public Epic(Epic epic) {
        super(epic);
        this.subtasksId = new ArrayList<>(epic.subtasksId);
    }
    @Override
    public String toString() {
        String text = title + " | Status: " + status + ", id: " + id + "\n";
        text += description + "\n";
        text += "Содержит подзадачи с id = " + subtasksId + "\n";
        return text;
    }
    @Override
    public Task copy() {
        return new Epic(this);
    }
}
