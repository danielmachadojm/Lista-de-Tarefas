package main;

import servico.GerenciadorTarefas;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.InputMismatchException; // Importar InputMismatchException
import java.util.Scanner;


public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    @Retention(RetentionPolicy.RUNTIME)
    public @interface ExportarComoPDF {}

    public static void main(String[] args) {
        GerenciadorTarefas gerenciador = GerenciadorTarefas.getInstance(scanner);

        System.out.println("\nS I S T E M A   L I S T A   D E   T A R E F A S");

        int opcao;
        do {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Listar tarefas");
            System.out.println("2. Adicionar tarefa");
            System.out.println("3. Excluir tarefa");
            System.out.println("4. Marcar tarefa como concluída");
            System.out.println("5. Marcar tarefa como pendente");
            System.out.println("6. Ordenar tarefas");
            System.out.println("7. Exportar lista");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            System.out.println("\n-----------------------------------");

            try { // Início do bloco try
                opcao = scanner.nextInt();
                scanner.nextLine(); // Consumir a quebra de linha após o nextInt()
            } catch (InputMismatchException e) { // Captura a exceção de entrada inválida
                System.out.println("Digite apenas o número correspondente da opção!");
                scanner.nextLine(); // Limpa o buffer do scanner para evitar loop infinito
                opcao = -1; // Atribui um valor inválido para continuar o loop do-while
            }

            switch (opcao) {
                case 1 -> {
                    System.out.println("\n-- LISTAGEM DE TAREFAS --");
                    System.out.println("1. Listar todas as tarefas");
                    System.out.println("2. Listar tarefas pendentes");
                    System.out.println("3. Listar tarefas concluídas");
                    System.out.print("Escolha uma opção: ");
                    try { // Try-catch para o submenu de listagem
                        int subOpcao = scanner.nextInt();
                        scanner.nextLine();
                        switch (subOpcao) {
                            case 1 -> gerenciador.listarTodas();
                            case 2 -> gerenciador.listarPorStatus(false);
                            case 3 -> gerenciador.listarPorStatus(true);
                            default -> System.out.println("Opção inválida.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Digite apenas o número correspondente da opção!");
                        scanner.nextLine();
                    }
                }
                case 2 -> gerenciador.adicionarTarefa();
                case 3 -> gerenciador.excluirTarefa();
                case 4 -> gerenciador.marcarComoConcluida();
                case 5 -> gerenciador.marcarComoPendente();
                case 6 -> {
                    System.out.println("\n-- ORDENAR TAREFAS --");
                    System.out.println("1. Por prioridade");
                    System.out.println("2. Por data de criação");
                    System.out.println("3. Por ordem alfabética");
                    System.out.println("4. Por status");
                    System.out.print("Escolha uma opção: ");
                    try { // Try-catch para o submenu de ordenação
                        int subOpcao = scanner.nextInt();
                        scanner.nextLine();
                        switch (subOpcao) {
                            case 1 -> gerenciador.ordenarPorPrioridade();
                            case 2 -> gerenciador.ordenarPorDataCriacao();
                            case 3 -> gerenciador.ordenarPorTitulo();
                            case 4 -> gerenciador.ordenarPorStatus();
                            default -> System.out.println("Opção inválida.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Digite apenas o número correspondente da opção!");
                        scanner.nextLine();
                    }
                }
                case 7 -> gerenciador.exportarTarefas();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }

        } while (opcao != 0);
    }
}