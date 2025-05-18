package main;

import modelo.Tarefa;
import modelo.Status;
import modelo.Prioridade;
import data.PersistenciaTarefa;
import servico.GerenciadorTarefas;
import java.util.*;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;


public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static List<Tarefa> tarefas = new ArrayList<>();
    private static final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");


    public static void main(String[] args) {
        tarefas = PersistenciaTarefa.carregarTarefas();

        int opcao;
        do {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Listar tarefas");
            System.out.println("2. Adicionar tarefa");
            System.out.println("3. Marcar tarefa como concluída");
            System.out.println("4. Ordenar tarefas por prioridade");
            System.out.println("5. Ordenar tarefas por data de criação");
            System.out.println("6. Salvar alterações");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine(); // limpar o buffer

            switch (opcao) {
                case 1 -> listarTarefas();
                case 2 -> adicionarTarefa();
                case 3 -> marcarComoConcluida();
                case 4 -> ordenarPorPrioridade();
                case 5 -> ordenarPorData();
                case 6 -> PersistenciaTarefa.salvarTarefas(tarefas);
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private static void listarTarefas() {
        if (tarefas.isEmpty()) {
            System.out.println();
            System.out.println("\nNenhuma tarefa cadastrada.");
            return;
        }
        System.out.println("\n--- TAREFAS ---");
        for (int i = 0; i < tarefas.size(); i++) {
            Tarefa t = tarefas.get(i);
            System.out.printf("%d. [%s] %s (Prioridade: %s | Criada em: %s)\n",
                    i + 1,
                    t.getStatus(),
                    t.getDescricao(),
                    t.getPrioridade(),
                    t.getDataCriacao().format(FORMATADOR)
            );

        }
    }

    private static void adicionarTarefa() {
        System.out.print("Descrição da tarefa: ");
        String descricao = scanner.nextLine();

        Prioridade prioridade;
        while (true) {
            System.out.print("Prioridade (BAIXA, MEDIA, ALTA): ");
            try {
                prioridade = Prioridade.valueOf(scanner.nextLine().toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Prioridade inválida. Tente novamente.");
            }
        }

        System.out.print("Data de criação (dd-MM-yyyy HH:mm), ou ENTER para agora: ");
        String dataInput = scanner.nextLine();
        LocalDateTime dataCriacao;
        try {
            dataCriacao = dataInput.isEmpty() ? LocalDateTime.now() : LocalDateTime.parse(dataInput, FORMATADOR);
        } catch (DateTimeParseException e) {
            System.out.println("Formato de data inválido. Usando data atual.");
            dataCriacao = LocalDateTime.now();
        }


        Tarefa tarefa = new Tarefa(descricao, prioridade);
        tarefa.setDataCriacao(dataCriacao);
        tarefas.add(tarefa);
        System.out.println("Tarefa adicionada com sucesso!");
    }

    private static void marcarComoConcluida() {
        listarTarefas();
        if (tarefas.isEmpty()) return;

        System.out.print("Digite o número da tarefa a ser concluída: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();

        if (index >= 0 && index < tarefas.size()) {
            tarefas.get(index).setStatus(Status.CONCLUIDA);
            System.out.println("Tarefa marcada como concluída.");
        } else {
            System.out.println("Índice inválido.");
        }
    }

    private static void ordenarPorPrioridade() {
        tarefas.sort(Comparator.comparing(Tarefa::getPrioridade).reversed());
        System.out.println("Tarefas ordenadas por prioridade.");
    }

    private static void ordenarPorData() {
        tarefas.sort(Comparator.comparing(Tarefa::getDataCriacao));
        System.out.println("Tarefas ordenadas por data de criação.");
    }
}