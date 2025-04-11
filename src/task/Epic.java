package task;

import java.util.ArrayList;

public class Epic extends Task{
    public ArrayList<Integer> subtasksId = new ArrayList<>();
    public Epic(String title, String description, int id) {
        super(title, description, id);
    }
    @Override
    public String toString() {
        String text = title + " | Status: " + status + ", id: " + id + "\n";
        text += description + "\n";
        text+= "Содержит подзадачи с id = "+subtasksId+ "\n";
        return text;
    }
}
