package servico;

import modelo.Tarefa;
import java.util.*;
import java.util.stream.Collectors;

public class GerenciadorTarefas {
    private List<Tarefa> tarefas;

    public GerenciadorTarefas() {
        this.tarefas = new ArrayList<>();
    }

    public void adicionarTarefa(Tarefa tarefa) {
        tarefas.add(tarefa);
    }

    public List<Tarefa> listarTodas() {
        return tarefas;
    }

    public List<Tarefa> listarPorStatus(boolean concluidas) {
        return tarefas.stream()
                .filter(t -> concluidas ? t.getStatus().toString().equals("CONCLUIDA") : t.getStatus().toString().equals("PENDENTE"))
                .collect(Collectors.toList());
    }

    public void ordenarPorPrioridade() {
        tarefas.sort(Comparator.comparing(Tarefa::getPrioridade).reversed());
    }

    public void ordenarPorDataCriacao() {
        tarefas.sort(Comparator.comparing(Tarefa::getDataCriacao));
    }

    public void ordenarPorDescricao() {
        tarefas.sort(Comparator.comparing(Tarefa::getDescricao));
    }

    public Tarefa getTarefaPorIndice(int indice) {
        return (indice >= 0 && indice < tarefas.size()) ? tarefas.get(indice) : null;
    }
}
