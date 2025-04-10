public class TaskSkeleton {
    public String title;
    public Status status;
    public TypeOfTask type;

    public TaskSkeleton(String title) {
        this.title = title;
        this.status = Status.NEW;
    }
    public TaskSkeleton(String title, Status status) {
        this.title = title;
        this.status = status;
    }
}
