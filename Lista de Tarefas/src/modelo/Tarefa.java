package modelo;

import java.time.LocalDateTime;
import main.Main.ExportarComoPDF;

@ExportarComoPDF
public class Tarefa {
    private final String titulo;
    private final String descricao;
    private Status status;
    private final Prioridade prioridade;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataConclusao;

    public Tarefa(String titulo, String descricao, Prioridade prioridade) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.status = Status.PENDENTE;
        this.dataCriacao = LocalDateTime.now();
    }

    public String getTitulo() {
        return titulo;
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

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    @Override
    public String toString() {
        return "[" + status + "] "
                + descricao + " (Prioridade: " + prioridade
                + ", Criada em: " + dataCriacao.toString() + ")";
    }
}

