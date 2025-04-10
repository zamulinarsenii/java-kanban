import java.util.ArrayList;

public class Subtask extends TaskSkeleton {
    public ArrayList<String> tasks;
    public Subtask(String title,Status status, ArrayList<String> tasks) {
        super(title,status);
        this.tasks = tasks;
        type = TypeOfTask.SUBTASK;
    }
}
