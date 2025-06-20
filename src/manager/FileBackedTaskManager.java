package manager;

import task.*;

import java.io.*;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Не удалось создать файл");
            }
        }
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            for (int i = 1; i < lines.size(); i++) {
                Task task = fromString(lines.get(i));
                switch (task.getType()) {
                    case TASK -> manager.addTask(task);
                    case EPIC -> manager.addEpic((Epic) task);
                    case SUBTASK -> manager.addSubtask((Subtask) task);
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка чтения из файла");
        }
        return manager;
    }

    protected void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,startTime,duration,epic\n");
            for (Task task : getAllTasks()) {
                writer.write(toString(task) + "\n");
            }
            for (Task epic : getAllEpics()) {
                writer.write(toString(epic) + "\n");
            }
            for (Task subtask : getAllSubtasks()) {
                writer.write(toString(subtask) + "\n");
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка сохранения в файл");
        }
    }

    private String toString(Task task) {
        return String.format("%d,%s,%s,%s,%s,%s,%s,%s",
                task.getId(),
                task.getType(),
                task.getName(),
                task.getStatus(),
                task.getDescription(),
                task.getStartTime() == null ? 0 : task.getStartTime().toEpochSecond(ZoneOffset.UTC),
                task.getDuration() == null ? 0 : task.getDuration().toMinutes(),
                task.getType() == TaskType.SUBTASK ? ((Subtask) task).getEpicId() : ""
        );
    }

    private static Task fromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        TaskType type = TaskType.valueOf(fields[1]);
        String name = fields[2];
        TaskStatus status = TaskStatus.valueOf(fields[3]);
        String description = fields[4];
        LocalDateTime startTime =LocalDateTime.ofEpochSecond(Long.parseLong(fields[5]), 0, ZoneOffset.UTC);
        Duration duration = Duration.ofMinutes(Long.parseLong(fields[6]));
        switch (type) {
            case TASK -> {return new Task(id, name, description, status, startTime, duration);}

            case EPIC ->{ return new Epic(id, name, description, status);}

            case SUBTASK -> {
                int epicId = Integer.parseInt(fields[7]);
                return new Subtask(id, name, description, status, startTime, duration, epicId);
            }
        }
        return null;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteById(int id) {
        super.deleteById(id);
        save();
    }

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        super.updateSubtask(newSubtask);
        save();
    }
}
