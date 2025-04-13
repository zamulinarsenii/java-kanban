package task;

import java.util.Objects;

public class Task {
    protected String title;
    protected String description;
    protected int id;
    protected Status status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Task(String title, String description, int id, Status status) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public Task(String title, String description, int id) {
        this.title = title;
        this.description = description;
        this.id = id;
        status = Status.NEW;
    }

    public Task(Task task) {
        this(task.title, task.description, task.id, task.status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        String text = title + " | Status: " + status + ", id: " + id + "\n";
        text += description + "\n";

        return text;
    }
}
