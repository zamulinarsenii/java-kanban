package manager;

import task.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int HISTORY_MAX_SIZE = 10;
    private LinkedList<Task> history = new LinkedList<>();

    @Override
    public void add(Task task) {
        Task copy = task.copy();
        history.add(copy);
        if (history.size() > HISTORY_MAX_SIZE) {
            history.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new LinkedList<>(history);
    }
}
