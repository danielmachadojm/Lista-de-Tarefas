package data;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

public class PersistenciaTarefa {
    private static final String ARQUIVO = "data/tarefas.csv";

    public static void salvarTarefas(List<Tarefa> tarefas) {
        try {
            Files.createDirectories(Paths.get("data"));
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(ARQUIVO));

            for (Tarefa t : tarefas) {
                String linha = String.join(",",
                        t.getDescricao(),
                        t.getStatus().name(),
                        t.getPrioridade().name(),
                        t.getDataCriacao().toString()
                );
                writer.write(linha);
                writer.newLine();
            }

            writer.close();
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
                if (partes.length == 4) {
                    String descricao = partes[0];
                    Status status = Status.valueOf(partes[1]);
                    Prioridade prioridade = Prioridade.valueOf(partes[2]);
                    LocalDateTime data = LocalDateTime.parse(partes[3]);

                    Tarefa t = new Tarefa(descricao, prioridade);
                    t.setStatus(status);
                    t.setDataCriacao(data);
                    tarefas.add(t);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar tarefas: " + e.getMessage());
        }

        return tarefas;
    }
}
