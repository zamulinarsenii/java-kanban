import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends TaskSkeleton {
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public Epic(String title, ArrayList<Subtask> subtasks) {
        super(title);
        int statusNewCounter = 0;
        for (int id = 0; id < subtasks.size(); id++) {
            this.subtasks.put(id, subtasks.get(id));
            if (subtasks.get(id).status == Status.NEW) {
                statusNewCounter++;
            }
        }
        if (statusNewCounter == subtasks.size())
            status = Status.NEW;
        else if (statusNewCounter == 0)
            status = Status.DONE;
        else
            status = Status.IN_PROGRESS;
        type = TypeOfTask.EPIC;
    }

    @Override
    public String toString() {
        String text = title;
        for (int i = 0; i < subtasks.size(); i++) {
            text += "\n - " + subtasks.get(i).title;
            for (String task : subtasks.get(i).tasks) {
                text += "\n  - " + task;
            }
        }
        return text;
    }
}
