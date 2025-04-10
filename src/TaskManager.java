import task.Status;
import task.TaskSkeleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    public static int counter = 0;
    public static HashMap<Integer, TaskSkeleton> tasks = new HashMap<>();

    public void add(TaskSkeleton task) {
        tasks.put(counter, task);
        counter++;
    }

    public ArrayList<TaskSkeleton> getAllTasks() {
        ArrayList<TaskSkeleton> objects = new ArrayList<>();
        for (Map.Entry<Integer, TaskSkeleton> entry: tasks.entrySet()) {
            objects.add(entry.getValue());
        }
        return objects;
    }
    public ArrayList<TaskSkeleton> getNewTasks() {
        ArrayList<TaskSkeleton> objects = new ArrayList<>();
        for (Map.Entry<Integer, TaskSkeleton> entry: tasks.entrySet()) {
            if (entry.getValue().status == Status.NEW)
                objects.add(entry.getValue());
        }
        return objects;
    }
    public ArrayList<TaskSkeleton> getDoneTasks() {
        ArrayList<TaskSkeleton> objects = new ArrayList<>();
        for (Map.Entry<Integer, TaskSkeleton> entry: tasks.entrySet()) {
            if (entry.getValue().status == Status.DONE)
                objects.add(entry.getValue());
        }
        return objects;
    }
    public ArrayList<TaskSkeleton> getInProgressTasks() {
        ArrayList<TaskSkeleton> objects = new ArrayList<>();
        for (Map.Entry<Integer, TaskSkeleton> entry: tasks.entrySet()) {
            if (entry.getValue().status == Status.IN_PROGRESS)
                objects.add(entry.getValue());
        }
        return objects;
    }
    public void deleteAllTasks(){
        tasks.clear();
        counter = 0;
    }
    public TaskSkeleton getById(int id){
        return tasks.get(id);
    }
    public void deleteById(int id){
        tasks.remove(id);
    }
    public void updateTask(int id, TaskSkeleton task){
        tasks.put(id, task);
    }
}
