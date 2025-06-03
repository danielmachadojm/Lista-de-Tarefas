package servico;

import modelo.Tarefa;
import modelo.Status;
import modelo.Prioridade;
import data.PersistenciaTarefa;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GerenciadorTarefas {
    // Instância única da classe
    private static GerenciadorTarefas instance;

    private final List<Tarefa> tarefas;
    private static final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private final Scanner scanner;

    // Construtor privado
    private GerenciadorTarefas(Scanner scanner) {
        this.tarefas = PersistenciaTarefa.carregarTarefas();
        this.scanner = scanner;
    }

    // Metodo estático para obter a instância
    public static GerenciadorTarefas getInstance(Scanner scanner) {
        if (instance == null) {
            instance = new GerenciadorTarefas(scanner);
        }
        return instance;
    }

    public void adicionarTarefa() {
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

    public void listarTodas() {
        if (tarefas.isEmpty()) {
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

    public void listarPorStatus(boolean concluidas) {
        List<Tarefa> listaFiltrada = tarefas.stream()
                .filter(t -> concluidas ? t.getStatus() == Status.CONCLUIDA : t.getStatus() == Status.PENDENTE)
                .toList();

        if (listaFiltrada.isEmpty()) {
            System.out.println(concluidas ? "\nNenhuma tarefa concluída." : "\nNenhuma tarefa pendente.");
            return;
        }

        System.out.println(concluidas ? "\n--- TAREFAS CONCLUÍDAS ---" : "\n--- TAREFAS PENDENTES ---");
        for (int i = 0; i < listaFiltrada.size(); i++) {
            Tarefa t = listaFiltrada.get(i);

            System.out.printf("%d. %s | Descrição: %s | Prioridade: %s | Criada em: %s | %s: %s\n",
                    i + 1,
                    t.getTitulo(),
                    t.getDescricao(),
                    t.getPrioridade(),
                    t.getDataCriacao().format(FORMATADOR), // Adicionado o argumento da data de criação
                    concluidas ? "Concluída em" : "Concluir até",
                    t.getDataConclusao() != null ? t.getDataConclusao().format(FORMATADOR) : "Não definida"
            );
        }
    }

    public void excluirTarefa() {
        if (tarefas.isEmpty()) {
            System.out.println("\nNenhuma tarefa para excluir.");
            return;
        }

        listarTodas();

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

    public void marcarComoConcluida() {
        listarTodas();
        if (tarefas.isEmpty()) return;

        System.out.print("Digite o número da tarefa a ser concluída: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();

        if (index >= 0 && index < tarefas.size()) {
            tarefas.get(index).setStatus(Status.CONCLUIDA);
            tarefas.get(index).setDataConclusao(LocalDateTime.now());
            System.out.println("Tarefa marcada como concluída.");
        } else {
            System.out.println("Índice inválido.");
        }
        PersistenciaTarefa.salvarTarefas(tarefas);
    }

    public void marcarComoPendente() {
        listarTodas();
        if (tarefas.isEmpty()) return;

        System.out.print("Digite o número da tarefa a marcar como pendente: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();

        if (index >= 0 && index < tarefas.size()) {
            tarefas.get(index).setStatus(Status.PENDENTE);
            tarefas.get(index).setDataConclusao(null);
            System.out.println("Tarefa marcada como pendente.");
            PersistenciaTarefa.salvarTarefas(tarefas);
        } else {
            System.out.println("Índice inválido.");
        }
    }

    public void ordenarPorPrioridade() {
        tarefas.sort(Comparator.comparing(Tarefa::getPrioridade).reversed());
        System.out.println("Tarefas ordenadas por prioridade.");
        PersistenciaTarefa.salvarTarefas(tarefas);
    }

    public void ordenarPorDataCriacao() {
        tarefas.sort(Comparator.comparing(Tarefa::getDataCriacao));
        System.out.println("Tarefas ordenadas por data de criação.");
        PersistenciaTarefa.salvarTarefas(tarefas);
    }

    public void ordenarPorTitulo() {
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa cadastrada para ordenar.");
            return;
        }
        tarefas.sort(Comparator.comparing(t -> t.getTitulo().toLowerCase()));
        PersistenciaTarefa.salvarTarefas(tarefas);
        System.out.println("Tarefas ordenadas por ordem alfabética.");
    }

    public void ordenarPorStatus() {
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa cadastrada para ordenar.");
            return;
        }
        tarefas.sort(Comparator.comparing(Tarefa::getStatus));
        PersistenciaTarefa.salvarTarefas(tarefas);
        System.out.println("Tarefas ordenadas por status (PENDENTE antes de CONCLUÍDA).");
    }

    public void exportarTarefas() {
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa para exportar.");
            return;
        }

        System.out.print("Digite o nome do arquivo (sem extensão): ");
        String nomeBase = scanner.nextLine().trim();

        System.out.println("Escolha o formato de exportação:");
        System.out.println("1. TXT");
        System.out.println("2. CSV");
        System.out.println("3. PDF");

        String escolha = scanner.nextLine();

        String nomeArquivo;
        switch (escolha) {
            case "1" -> {
                nomeArquivo = nomeBase + ".txt";
                exportarComoTxt(nomeArquivo);
            }
            case "2" -> {
                nomeArquivo = nomeBase + ".csv";
                exportarComoCsv(nomeArquivo);
            }
            case "3" -> {
                nomeArquivo = nomeBase + ".pdf";
                exportarComoPdf(nomeArquivo);
            }
            default -> System.out.println("Formato inválido.");
        }
    }

    private void exportarComoTxt(String nomeArquivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
            for (Tarefa t : tarefas) {
                writer.write("Título: " + t.getTitulo());
                writer.newLine();
                writer.write("Descrição: " + t.getDescricao());
                writer.newLine();
                writer.write("Prioridade: " + t.getPrioridade());
                writer.newLine();
                writer.write("Status: " + t.getStatus());
                writer.newLine();
                writer.write("Data de criação: " + t.getDataCriacao().format(FORMATADOR));
                writer.newLine();
                writer.write("Data de conclusão: " +
                        (t.getDataConclusao() != null ? t.getDataConclusao().format(FORMATADOR) : "Não definida"));
                writer.newLine();
                writer.write("---------------------------------------------------");
                writer.newLine();
            }
            System.out.println("Tarefas exportadas com sucesso para " + nomeArquivo);
        } catch (IOException e) {
            System.out.println("Erro ao exportar tarefas: " + e.getMessage());
        }
    }

    private void exportarComoCsv(String nomeArquivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
            writer.write("Titulo,Descricao,Prioridade,Status,DataCriacao,DataConclusao");
            writer.newLine();
            for (Tarefa t : tarefas) {
                String linha = String.join(",",
                        "\"" + t.getTitulo() + "\"",
                        "\"" + t.getDescricao() + "\"",
                        t.getPrioridade().name(),
                        t.getStatus().name(),
                        t.getDataCriacao().format(FORMATADOR),
                        t.getDataConclusao() != null ? t.getDataConclusao().format(FORMATADOR) : ""
                );
                writer.write(linha);
                writer.newLine();
            }
            System.out.println("Tarefas exportadas com sucesso para " + nomeArquivo);
        } catch (IOException e) {
            System.out.println("Erro ao exportar tarefas: " + e.getMessage());
        }
    }

    private void exportarComoPdf(String nomeArquivo) {
        try {
            Class<?> clazz = Tarefa.class;

            if (!clazz.isAnnotationPresent(main.Main.ExportarComoPDF.class)) {
                System.out.println("A classe Tarefa não está anotada com @ExportarComoPDF.");
                return;
            }

            com.lowagie.text.Document document = new com.lowagie.text.Document();
            com.lowagie.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(nomeArquivo));
            document.open();

            com.lowagie.text.Font tituloFonte = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 14, com.lowagie.text.Font.BOLD);
            com.lowagie.text.Font textoFonte = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 12, com.lowagie.text.Font.NORMAL);

            document.add(new com.lowagie.text.Paragraph("Lista de Tarefas", tituloFonte));
            document.add(new com.lowagie.text.Paragraph(" "));

            for (Tarefa t : tarefas) {
                document.add(new com.lowagie.text.Paragraph("Título: " + t.getTitulo(), textoFonte));
                document.add(new com.lowagie.text.Paragraph("Descrição: " + t.getDescricao(), textoFonte));
                document.add(new com.lowagie.text.Paragraph("Prioridade: " + t.getPrioridade(), textoFonte));
                document.add(new com.lowagie.text.Paragraph("Status: " + t.getStatus(), textoFonte));
                document.add(new com.lowagie.text.Paragraph("Data de criação: " + t.getDataCriacao().format(FORMATADOR), textoFonte));
                document.add(new com.lowagie.text.Paragraph("Data de conclusão: " +
                        (t.getDataConclusao() != null ? t.getDataConclusao().format(FORMATADOR) : "Não definida"), textoFonte));
                document.add(new com.lowagie.text.Paragraph("--------------------------------------------------"));
            }

            document.close();
            System.out.println("Tarefas exportadas com sucesso para " + nomeArquivo);

        } catch (Exception e) {
            System.out.println("Erro ao exportar para PDF: " + e.getMessage());
        }
    }
}