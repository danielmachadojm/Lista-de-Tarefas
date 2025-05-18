package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Tarefa {
    private String descricao;
    private Status status;
    private Prioridade prioridade;
    private LocalDateTime dataCriacao;

    public Tarefa(String descricao, Prioridade prioridade) {
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.status = Status.PENDENTE;
        this.dataCriacao = LocalDateTime.now();
    }

    public String getDescricao() {
        return descricao;
    }

    public Status getStatus() {
        return status;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Override
    public String toString() {
        return "[" + status + "] "
                + descricao + " (Prioridade: " + prioridade
                + ", Criada em: " + dataCriacao.toString() + ")";
    }
}

