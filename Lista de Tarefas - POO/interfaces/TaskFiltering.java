package todoapp.interfaces;

import todoapp.models.Task;
import todoapp.enums.Priority;
import java.util.Comparator;
import java.util.List;

public interface TaskFiltering {
    List<Task> filterByStatus(boolean completed);
    List<Task> filterByPriority(Priority priority);
    List<Task> sortByPriority();
    List<Task> sortByCreationDate();
    List<Task> sortByCustomCriteria(Comparator<Task> comparator);
}