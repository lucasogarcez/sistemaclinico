package com.sistemaclinico.clinico.model;

import java.util.List;

public class Medico {
    private Integer id;
    private String nome;
    private String crm;

    private Usuario usuario;

    private List<Consulta> consultas;

    public Medico() {
    }

    public Medico(Integer id, String nome, String crm, Usuario usuario) {
        this.id = id;
        this.nome = nome;
        this.crm = crm;
        this.usuario = usuario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }
}
