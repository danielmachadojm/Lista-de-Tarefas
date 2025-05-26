package todoapp.analytics;

import todoapp.enums.Priority;
import todoapp.models.Task;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TaskAnalytics {

    public static List<Task> filterTasks(List<Task> tasks, Predicate<Task> predicate) {
        return tasks.stream().filter(predicate).collect(Collectors.toList());
    }

    public static Map<String, Long> getTaskStatistics(List<Task> tasks) {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", (long) tasks.size());
        stats.put("completed", tasks.stream().filter(Task::isCompleted).count());
        stats.put("pending", tasks.stream().filter(t -> !t.isCompleted()).count());
        for (Priority p : Priority.values()) {
            stats.put(p.name().toLowerCase(), tasks.stream().filter(t -> t.getPriority() == p).count());
        }
        return stats;
    }

    public static Map<String, Double> analyzeTrends(List<Task> tasks) {
        Map<String, Double> trends = new HashMap<>();
        if (!tasks.isEmpty()) {
            trends.put("completionRate", (double) tasks.stream().filter(Task::isCompleted).count() / tasks.size());
            for (Priority p : Priority.values()) {
                trends.put("priority_" + p.name().toLowerCase(), (double) tasks.stream().filter(t -> t.getPriority() == p).count() / tasks.size());
            }
        }
        return trends;
    }
}
