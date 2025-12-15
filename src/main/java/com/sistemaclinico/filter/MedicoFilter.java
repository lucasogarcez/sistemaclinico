package com.sistemaclinico.filter;

public class MedicoFilter {
    private Long codigo;
    private String nome;
    private String crm;

    public MedicoFilter() {}

    public MedicoFilter(Long codigo, String nome, String crm) {
        this.codigo = codigo;
        this.nome = nome;
        this.crm = crm;
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

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    @Override
    public String toString() {
        return "MedicoFilter [codigo=" + codigo + ", nome=" + nome + ", crm=" + crm + "]";
    }
}
