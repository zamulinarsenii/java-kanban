package task;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId = new ArrayList<>();

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void setSubtasksId(ArrayList<Integer> subtasksId) {
        this.subtasksId = subtasksId;
    }

    public Epic(String title, String description, int id) {
        super(title, description, id);
    }

    @Override
    public String toString() {
        String text = title + " | Status: " + status + ", id: " + id + "\n";
        text += description + "\n";
        text += "Содержит подзадачи с id = " + subtasksId + "\n";
        return text;
    }
}
