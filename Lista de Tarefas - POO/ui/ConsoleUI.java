package todoapp.ui;

import todoapp.enums.Priority;
import todoapp.models.Task;
import todoapp.models.TaskList;
import todoapp.factories.TaskListFactory;
import todoapp.utils.TaskReflectionUtil;

import java.io.*;
import java.util.*;

public class ConsoleUI {
    private Scanner scanner;
    private TaskList currentList;
    private String currentUser;

    public ConsoleUI() {
        scanner = new Scanner(System.in);
        currentUser = "User" + new Random().nextInt(1000);
        System.out.println("ID do usuário atual: " + currentUser);
    }

    public void start() {
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Digite sua escolha: ");
            switch (choice) {
                case 1 -> createTaskList();
                case 2 -> loadTaskList();
                case 3 -> { if (currentList != null) manageTaskList(); else System.out.println("Nenhuma lista carregada."); }
                case 4 -> shareTaskList();
                case 5 -> accessSharedList();
                case 0 -> running = false;
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("Lista atual: " + (currentList != null ? currentList.getName() : "Nenhuma"));
        System.out.println("1. Criar nova lista");
        System.out.println("2. Carregar lista de arquivo");
        System.out.println("3. Gerenciar lista");
        System.out.println("4. Compartilhar lista");
        System.out.println("5. Acessar lista compartilhada");
        System.out.println("0. Sair");
    }

    private void createTaskList() {
        String name = getStringInput("Nome da lista: ");
        currentList = TaskListFactory.createPersonalTaskList(name, currentUser);
        System.out.println("Lista criada com sucesso.");
    }

    private void loadTaskList() {
        String path = getStringInput("Caminho do arquivo: ");
        try {
            TaskList loaded = new TaskList();
            loaded.loadFromFile(path);
            currentList = loaded;
            System.out.println("Lista carregada.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void manageTaskList() {
        boolean managing = true;
        while (managing) {
            System.out.println("\n=== GERENCIAR LISTA ===");
            System.out.println("1. Adicionar tarefa");
            System.out.println("2. Visualizar tarefas");
            System.out.println("3. Marcar como concluída");
            System.out.println("4. Marcar como pendente");
            System.out.println("5. Remover tarefa");
            System.out.println("0. Voltar");
            int op = getIntInput("Escolha: ");
            switch (op) {
                case 1 -> addTask();
                case 2 -> viewTasks();
                case 3 -> changeStatus(true);
                case 4 -> changeStatus(false);
                case 5 -> removeTask();
                case 0 -> managing = false;
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void addTask() {
        String title = getStringInput("Título: ");
        String desc = getStringInput("Descrição: ");
        System.out.println("Prioridade: 1.BAIXA 2.MÉDIA 3.ALTA");
        int p = getIntInput("Escolha: ");
        Priority priority = switch (p) {
            case 1 -> Priority.BAIXA;
            case 3 -> Priority.ALTA;
            default -> Priority.MÉDIA;
        };
        Task task = new Task(title, desc, priority);
        currentList.addTask(task);
        System.out.println("Tarefa adicionada.");
        TaskReflectionUtil.displayTaskProperties(task);
    }

    private void viewTasks() {
        List<Task> tasks = currentList.getAllTasks();
        if (tasks.isEmpty()) System.out.println("Lista vazia.");
        else tasks.forEach(t -> System.out.println(t));
    }

    private void changeStatus(boolean complete) {
        List<Task> tasks = currentList.filterByStatus(!complete);
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i).getTitle());
        }
        int idx = getIntInput("Escolha a tarefa: ") - 1;
        if (idx >= 0 && idx < tasks.size()) {
            if (complete) currentList.markAsCompleted(tasks.get(idx).getId());
            else currentList.markAsPending(tasks.get(idx).getId());
        }
    }

    private void removeTask() {
        List<Task> tasks = currentList.getAllTasks();
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i).getTitle());
        }
        int idx = getIntInput("Escolha a tarefa: ") - 1;
        if (idx >= 0 && idx < tasks.size()) {
            currentList.removeTask(tasks.get(idx).getId());
            System.out.println("Tarefa removida.");
        }
    }

    private void shareTaskList() {
        if (currentList == null) {
            System.out.println("Nenhuma lista carregada.");
            return;
        }
        String userId = getStringInput("ID do usuário para compartilhar: ");
        currentList.shareWith(userId);
        System.out.println("Lista compartilhada.");
    }

    private void accessSharedList() {
        List<String> names = TaskListFactory.getSharedListNames();
        if (names.isEmpty()) {
            System.out.println("Nenhuma lista compartilhada.");
            return;
        }
        for (int i = 0; i < names.size(); i++) {
            System.out.println((i + 1) + ". " + names.get(i));
        }
        int idx = getIntInput("Escolha a lista: ") - 1;
        if (idx >= 0 && idx < names.size()) {
            currentList = TaskListFactory.getSharedTaskList(names.get(idx), currentUser);
            System.out.println("Lista carregada.");
        }
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Número inválido.");
            }
        }
    }
}
