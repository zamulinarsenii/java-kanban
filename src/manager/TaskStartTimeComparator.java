package manager;

import task.Task;

import java.util.Comparator;

public class TaskStartTimeComparator implements Comparator<Task> {
    @Override
    public int compare(Task t1, Task t2) {
        int cmp = t1.getStartTime().compareTo(t2.getStartTime());
        if (cmp == 0) {
            return Integer.compare(t1.getId(), t2.getId());
        }
        return cmp;
    }
}
