package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final int HISTORY_MAX_SIZE = 10;
    private ArrayList<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        Task copy = createCopy(task);
        history.add(copy);
        if (history.size() > HISTORY_MAX_SIZE) {
            history.remove(0);
        }
    }

    private Task createCopy(Task task) {
        if (task instanceof Epic) {
            return new Epic((Epic) task);
        } else if (task instanceof Subtask) {
            return new Subtask((Subtask) task);
        } else {
            return new Task(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
