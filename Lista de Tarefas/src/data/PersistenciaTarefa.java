package data;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import modelo.Status;
import modelo.Tarefa;
import modelo.Prioridade;

public class PersistenciaTarefa {
    private static final String ARQUIVO = "data/tarefas.csv";

    public static void salvarTarefas(List<Tarefa> tarefas) {
        try {
            Files.createDirectories(Paths.get("data"));
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(ARQUIVO));

            for (Tarefa t : tarefas) {
                String linha = String.join(",",
                        t.getTitulo(),
                        t.getDescricao(),
                        t.getStatus().name(),
                        t.getPrioridade().name(),
                        t.getDataCriacao().toString(),
                        t.getDataConclusao() != null ? t.getDataConclusao().toString() : ""
                );
                writer.write(linha);
                writer.newLine();
            }

            writer.close();
            System.out.println("\nSalvo com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar tarefas: " + e.getMessage());
        }
    }

    public static List<Tarefa> carregarTarefas() {
        List<Tarefa> tarefas = new ArrayList<>();
        Path caminho = Paths.get(ARQUIVO);

        if (!Files.exists(caminho)) return tarefas;

        try (BufferedReader reader = Files.newBufferedReader(caminho)) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(",");
                if (partes.length >= 5) {
                    String titulo = partes[0];
                    String descricao = partes[1];
                    Status status = Status.valueOf(partes[2]);
                    Prioridade prioridade = Prioridade.valueOf(partes[3]);
                    LocalDateTime dataCriacao = LocalDateTime.parse(partes[4]);

                    LocalDateTime dataConclusao = null;
                    if (partes.length >= 6 && !partes[5].isBlank()) {
                        dataConclusao = LocalDateTime.parse(partes[5]);
                    }

                    Tarefa t = new Tarefa(titulo, descricao, prioridade);
                    t.setStatus(status);
                    t.setDataCriacao(dataCriacao);
                    t.setDataConclusao(dataConclusao);
                    tarefas.add(t);
                }

            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar tarefas: " + e.getMessage());
        }

        return tarefas;
    }
}
