package todoapp.interfaces;

import todoapp.models.Task;
import java.util.List;
import java.util.UUID;

public interface TaskOperations {
    void addTask(Task task);
    void removeTask(UUID id);
    void updateTask(Task task);
    Task getTask(UUID id);
    List<Task> getAllTasks();
    void markAsCompleted(UUID id);
    void markAsPending(UUID id);
}