package task;

import task.Status;

import java.util.ArrayList;

public class Task extends TaskSkeleton {
    public ArrayList<String> tasks;

    public Task(String title, ArrayList<String> tasks) {
        super(title);
        type = TypeOfTask.TASK;
        this.tasks = tasks;
    }
    public Task(String title, Status status, ArrayList<String> tasks) {
        super(title, status);
        type = TypeOfTask.TASK;
        this.tasks = tasks;
    }
    @Override
    public String toString() {
        String text = title;
        for (String task : tasks) {
            text += "\n - " + task;
        }
        return text;
    }
}
