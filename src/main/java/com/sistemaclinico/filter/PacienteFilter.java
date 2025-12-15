package com.sistemaclinico.filter;

import java.time.LocalDate;

public class PacienteFilter {
    private Long codigo;
    private String nome;
    private String cpf;
    private LocalDate dataNascimentoInicial;
    private LocalDate dataNascimentoFinal;

    public PacienteFilter() {}

    public PacienteFilter(Long codigo, String nome, String cpf, LocalDate dataNascimentoInicial,
            LocalDate dataNascimentoFinal) {
        this.codigo = codigo;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimentoInicial = dataNascimentoInicial;
        this.dataNascimentoFinal = dataNascimentoFinal;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimentoInicial() {
        return dataNascimentoInicial;
    }

    public void setDataNascimentoInicial(LocalDate dataNascimentoInicial) {
        this.dataNascimentoInicial = dataNascimentoInicial;
    }

    public LocalDate getDataNascimentoFinal() {
        return dataNascimentoFinal;
    }

    public void setDataNascimentoFinal(LocalDate dataNascimentoFinal) {
        this.dataNascimentoFinal = dataNascimentoFinal;
    }

    @Override
    public String toString() {
        return "PacienteFilter [codigo=" + codigo + ", nome=" + nome + ", cpf=" + cpf
                + ", dataNascimentoInicial=" + dataNascimentoInicial + ", dataNascimentoFinal="
                + dataNascimentoFinal + "]";
    }
}
