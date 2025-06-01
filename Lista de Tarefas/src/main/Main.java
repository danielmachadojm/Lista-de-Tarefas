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
            System.out.println("1. Listar todas as tarefas");
            System.out.println("2. Listar tarefas pendentes");
            System.out.println("3. Listar tarefas concluídas");
            System.out.println("4. Adicionar tarefa");
            System.out.println("5. Excluir tarefa");
            System.out.println("6. Marcar tarefa como concluída");
            System.out.println("7. Ordenar tarefas por prioridade");
            System.out.println("8. Ordenar tarefas por data de criação");
            System.out.println("9. Ordenar tarefas por ordem alfabética");
            System.out.println("10.Ordenar tarefas por status");
//            System.out.println("7. Salvar alterações");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            System.out.println("\n-----------------------------------");

            opcao = scanner.nextInt();
            scanner.nextLine(); // limpar o buffer

            switch (opcao) {
                case 1 -> listarTarefas();
                case 2 -> listarTarefasPendentes();
                case 3 -> listarTarefasConcluidas();
                case 4 -> adicionarTarefa();
                case 5 -> excluirTarefa();
                case 6 -> marcarComoConcluida();
                case 7 -> ordenarPorPrioridade();
                case 8 -> ordenarPorData();
                case 9 -> ordenarPorTitulo();
                case 10 -> ordenarPorStatus();
//              case 7 -> PersistenciaTarefa.salvarTarefas(tarefas);
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
            System.out.printf("%d. %s | Descrição: %s | Prioridade: %s | Status: %s | Criada em: %s | Concluir até: %s\n",
                    i + 1,
                    t.getTitulo(),
                    t.getDescricao(),
                    t.getPrioridade(),
                    t.getStatus(),
                    t.getDataCriacao().format(FORMATADOR),
                    t.getDataConclusao() != null ? t.getDataConclusao().format(FORMATADOR) : "Não definida"
            );


        }
    }

    private static void listarTarefasPendentes() {
        List<Tarefa> pendentes = tarefas.stream()
                .filter(t -> t.getStatus() == Status.PENDENTE)
                .collect(Collectors.toList());

        if (pendentes.isEmpty()) {
            System.out.println("\nNenhuma tarefa pendente.");
            return;
        }

        System.out.println("\n--- TAREFAS PENDENTES ---");
        for (int i = 0; i < pendentes.size(); i++) {
            Tarefa t = pendentes.get(i);
            System.out.printf("%d. %s | Descrição: %s | Prioridade: %s | Criada em: %s | Concluir até: %s\n",
                    i + 1,
                    t.getTitulo(),
                    t.getDescricao(),
                    t.getPrioridade(),
                    t.getDataCriacao().format(FORMATADOR),
                    t.getDataConclusao() != null ? t.getDataConclusao().format(FORMATADOR) : "Não definida"
            );
        }
    }

    private static void listarTarefasConcluidas() {
        List<Tarefa> concluidas = tarefas.stream()
                .filter(t -> t.getStatus() == Status.CONCLUIDA)
                .collect(Collectors.toList());

        if (concluidas.isEmpty()) {
            System.out.println("\nNenhuma tarefa concluída.");
            return;
        }

        System.out.println("\n--- TAREFAS CONCLUÍDAS ---");
        for (int i = 0; i < concluidas.size(); i++) {
            Tarefa t = concluidas.get(i);
            System.out.printf("%d. %s | Descrição: %s | Prioridade: %s | Criada em: %s | Concluída em: %s\n",
                    i + 1,
                    t.getTitulo(),
                    t.getDescricao(),
                    t.getPrioridade(),
                    t.getDataCriacao().format(FORMATADOR),
                    t.getDataConclusao() != null ? t.getDataConclusao().format(FORMATADOR) : "Não definida"
            );
        }
    }

    private static void adicionarTarefa() {
        System.out.println("Título da tarefa: ");
        String titulo = scanner.nextLine();

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


        Tarefa tarefa = new Tarefa(titulo, descricao, prioridade);
        tarefa.setDataCriacao(dataCriacao);
        tarefas.add(tarefa);



        System.out.print("Digite a data de conclusão (dd-MM-yyyy HH:mm) ou pressione Enter para usar agora: ");
        String dataStr = scanner.nextLine();
        LocalDateTime dataConclusao;

        if (dataStr.isBlank()) {
            dataConclusao = LocalDateTime.now();
        } else {
            try {
                dataConclusao = LocalDateTime.parse(dataStr, FORMATADOR);
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida. Usando a data e hora atual.");
                dataConclusao = LocalDateTime.now();
            }
        }
        tarefa.setDataConclusao(dataConclusao);

        PersistenciaTarefa.salvarTarefas(tarefas);
        System.out.println("Tarefa adicionada com sucesso!");

    }

    private static void excluirTarefa() {
        if (tarefas.isEmpty()) {
            System.out.println("\nNenhuma tarefa para excluir.");
            return;
        }

        listarTarefas();

        System.out.print("\nDigite o número da tarefa que deseja excluir: ");
        try {
            int indice = Integer.parseInt(scanner.nextLine());
            if (indice < 1 || indice > tarefas.size()) {
                System.out.println("Número inválido.");
                return;
            }

            System.out.print("Tem certeza que deseja excluir a tarefa \"" + tarefas.get(indice - 1).getTitulo() + "\"? (s/n): ");
            String confirmacao = scanner.nextLine().trim().toLowerCase();
            if (!confirmacao.equals("s")) {
                System.out.println("Exclusão cancelada.");
                return;
            }


            Tarefa removida = tarefas.remove(indice - 1);
            PersistenciaTarefa.salvarTarefas(tarefas);
            System.out.println("Tarefa \"" + removida.getTitulo() + "\" excluída com sucesso.");
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Digite um número válido.");
        }
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
        PersistenciaTarefa.salvarTarefas(tarefas);
    }

    private static void ordenarPorPrioridade() {
        tarefas.sort(Comparator.comparing(Tarefa::getPrioridade).reversed());
        System.out.println("Tarefas ordenadas por prioridade.");
        PersistenciaTarefa.salvarTarefas(tarefas);
    }

    private static void ordenarPorData() {
        tarefas.sort(Comparator.comparing(Tarefa::getDataCriacao));
        System.out.println("Tarefas ordenadas por data de criação.");
        PersistenciaTarefa.salvarTarefas(tarefas);
    }

    private static void ordenarPorTitulo() {
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa cadastrada para ordenar.");
            return;
        }

        tarefas.sort(Comparator.comparing(t -> t.getTitulo().toLowerCase()));
        PersistenciaTarefa.salvarTarefas(tarefas);
        System.out.println("Tarefas ordenadas por ordem alfabética.");

    }

    private static void ordenarPorStatus() {
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa cadastrada para ordenar.");
            return;
        }

        tarefas.sort(Comparator.comparing(Tarefa::getStatus));
        PersistenciaTarefa.salvarTarefas(tarefas);
        System.out.println("Tarefas ordenadas por status (PENDENTE antes de CONCLUÍDA).");
        PersistenciaTarefa.salvarTarefas(tarefas);
    }

}