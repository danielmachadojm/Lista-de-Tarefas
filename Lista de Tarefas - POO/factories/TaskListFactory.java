package todoapp.factories;

import todoapp.models.TaskList;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TaskListFactory {

    private static final Map<String, TaskList> sharedLists = new ConcurrentHashMap<>();

    // Cria uma lista pessoal
    public static TaskList createPersonalTaskList(String name, String owner) {
        return new TaskList(name, owner);
    }

    // Retorna uma lista compartilhada, ou cria uma nova se não existir
    public static TaskList getSharedTaskList(String name, String owner) {
        return sharedLists.computeIfAbsent(name, k -> {
            TaskList newList = new TaskList(name, owner);
            System.out.println("Nova lista compartilhada criada: " + name);
            return newList;
        });
    }

    // Lista os nomes das listas compartilhadas
    public static List<String> getSharedListNames() {
        return new ArrayList<>(sharedLists.keySet());
    }

    // Remove uma lista compartilhada pelo nome
    public static void removeSharedList(String name) {
        sharedLists.remove(name);
    }
}
