package todoapp.models;

import todoapp.annotations.*;
import todoapp.enums.Priority;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    @TaskProperty(description = "Identificador único da tarefa", required = true)
    private UUID id;

    @TaskProperty(description = "Título da tarefa", required = true)
    private String title;

    @TaskProperty(description = "Descrição detalhada da tarefa")
    private String description;

    @TaskProperty(description = "Data de criação da tarefa", required = true)
    private LocalDateTime creationDate;

    @TaskProperty(description = "Status de conclusão da tarefa")
    private boolean completed;

    @PriorityLevel
    @TaskProperty(description = "Nível de prioridade da tarefa", required = true)
    private Priority priority;

    public Task() {
        this.id = UUID.randomUUID();
        this.creationDate = LocalDateTime.now();
        this.completed = false;
        this.priority = Priority.MÉDIA;
    }

    public Task(String title, String description, Priority priority) {
        this();
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String status = completed ? "Concluída" : "Pendente";
        return String.format("[%s] %s (Prioridade: %s) - %s - Criada em: %s - %s",
                id.toString().substring(0, 8),
                title,
                priority,
                description,
                creationDate.format(formatter),
                status);
    }
}