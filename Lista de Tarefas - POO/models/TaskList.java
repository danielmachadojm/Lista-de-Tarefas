package todoapp.models;

import todoapp.annotations.TaskProperty;
import todoapp.enums.Priority;
import todoapp.interfaces.*;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TaskList implements TaskOperations, TaskFiltering, TaskPersistence, TaskSharing, Serializable {
    private static final long serialVersionUID = 1L;

    @TaskProperty(description = "Nome da lista de tarefas", required = true)
    private String name;

    @TaskProperty(description = "Coleção de tarefas da lista", required = true)
    private List<Task> tasks;

    @TaskProperty(description = "Proprietário da lista", required = true)
    private String owner;

    @TaskProperty(description = "Usuários com quem a lista é compartilhada")
    private Set<String> sharedWith;

    public TaskList() {
        this.tasks = new ArrayList<>();
        this.sharedWith = new HashSet<>();
    }

    public TaskList(String name, String owner) {
        this();
        this.name = name;
        this.owner = owner;
    }

    @Override
    public void addTask(Task task) { tasks.add(task); }

    @Override
    public void removeTask(UUID id) { tasks.removeIf(task -> task.getId().equals(id)); }

    @Override
    public void updateTask(Task updatedTask) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().equals(updatedTask.getId())) {
                tasks.set(i, updatedTask);
                return;
            }
        }
    }

    @Override
    public Task getTask(UUID id) {
        return tasks.stream().filter(task -> task.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Task> getAllTasks() { return new ArrayList<>(tasks); }

    @Override
    public void markAsCompleted(UUID id) {
        Task task = getTask(id);
        if (task != null) task.setCompleted(true);
    }

    @Override
    public void markAsPending(UUID id) {
        Task task = getTask(id);
        if (task != null) task.setCompleted(false);
    }

    @Override
    public List<Task> filterByStatus(boolean completed) {
        return tasks.stream().filter(task -> task.isCompleted() == completed).collect(Collectors.toList());
    }

    @Override
    public List<Task> filterByPriority(Priority priority) {
        return tasks.stream().filter(task -> task.getPriority() == priority).collect(Collectors.toList());
    }

    @Override
    public List<Task> sortByPriority() {
        return tasks.stream().sorted(Comparator.comparing((Task t) -> t.getPriority().getValue()).reversed()).collect(Collectors.toList());
    }

    @Override
    public List<Task> sortByCreationDate() {
        return tasks.stream().sorted(Comparator.comparing(Task::getCreationDate)).collect(Collectors.toList());
    }

    @Override
    public List<Task> sortByCustomCriteria(Comparator<Task> comparator) {
        return tasks.stream().sorted(comparator).collect(Collectors.toList());
    }

    @Override
    public void saveToFile(String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(this);
        }
    }

    @Override
    public void loadFromFile(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            TaskList loadedList = (TaskList) ois.readObject();
            this.name = loadedList.name;
            this.tasks = loadedList.tasks;
            this.owner = loadedList.owner;
            this.sharedWith = loadedList.sharedWith;
        }
    }

    @Override
    public String exportToCSV() {
        StringBuilder csv = new StringBuilder();
        csv.append("ID,Título,Descrição,Data Criação,Prioridade,Concluída\n");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Task task : tasks) {
            csv.append(task.getId()).append(",");
            csv.append(task.getTitle()).append(",");
            csv.append(task.getDescription()).append(",");
            csv.append(task.getCreationDate().format(formatter)).append(",");
            csv.append(task.getPriority()).append(",");
            csv.append(task.isCompleted()).append("\n");
        }
        return csv.toString();
    }

    @Override
    public void importFromCSV(String csvContent) {
        String[] lines = csvContent.split("\n");
        if (lines.length <= 1) return;
        for (int i = 1; i < lines.length; i++) {
            String[] fields = lines[i].split(",");
            if (fields.length >= 6) {
                Task task = new Task();
                task.setTitle(fields[1]);
                task.setDescription(fields[2]);
                try {
                    task.setPriority(Priority.valueOf(fields[4]));
                } catch (IllegalArgumentException e) {
                    task.setPriority(Priority.MÉDIA);
                }
                task.setCompleted(Boolean.parseBoolean(fields[5]));
                tasks.add(task);
            }
        }
    }

    @Override
    public void shareWith(String userId) { sharedWith.add(userId); }

    @Override
    public void removeSharing(String userId) { sharedWith.remove(userId); }

    @Override
    public boolean isSharedWith(String userId) { return sharedWith.contains(userId); }

    @Override
    public List<String> getSharedUsers() { return new ArrayList<>(sharedWith); }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
}