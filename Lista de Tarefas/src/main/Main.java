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

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Tarefa> tarefas = PersistenciaTarefa.carregarTarefas();
        System.out.println(tarefas.size() + " tarefas carregadas.");

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Adicionar tarefa");
            System.out.println("2. Listar tarefas");
            System.out.println("3. Marcar tarefa como concluída");
            System.out.println("4. Ordenar tarefas");
            System.out.println("0. Sair");

            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    System.out.print("Descrição da tarefa: ");
                    String descricao = scanner.nextLine();

                    System.out.print("Prioridade (BAIXA, MEDIA, ALTA): ");
                    Prioridade prioridade = Prioridade.valueOf(scanner.nextLine().toUpperCase());

                    tarefas.add(new Tarefa(descricao, prioridade));
                    System.out.println("Tarefa adicionada.");
                    break;

                case "2":
                    if (tarefas.isEmpty()) {
                        System.out.println("Nenhuma tarefa encontrada.");
                    } else {
                        for (int i = 0; i < tarefas.size(); i++) {
                            System.out.println(i + ". " + tarefas.get(i));
                        }
                    }
                    break;

                case "3":
                    System.out.print("Número da tarefa a marcar como concluída: ");
                    int indice = Integer.parseInt(scanner.nextLine());
                    if (indice >= 0 && indice < tarefas.size()) {
                        tarefas.get(indice).setStatus(Status.CONCLUIDA);
                        System.out.println("Tarefa marcada como concluída.");
                    } else {
                        System.out.println("Índice inválido.");
                    }
                    break;

                case "4":
                    System.out.println("Ordenar por:");
                    System.out.println("1. Prioridade");
                    System.out.println("2. Data de criação");
                    String tipoOrdenacao = scanner.nextLine();

                    Comparator<Tarefa> comparator;
                    if (tipoOrdenacao.equals("1")) {
                        comparator = Comparator.comparing(Tarefa::getPrioridade).reversed();
                    } else {
                        comparator = Comparator.comparing(Tarefa::getDataCriacao);
                    }

                    List<Tarefa> ordenadas = tarefas.stream()
                            .sorted(comparator)
                            .collect(Collectors.toList());

                    for (Tarefa t : ordenadas) {
                        System.out.println(t);
                    }
                    break;

                case "0":
                    PersistenciaTarefa.salvarTarefas(tarefas);
                    System.out.println("Tarefas salvas. Saindo...");
                    return;

                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}
