package com.hospital.hospital_web.model;

import java.util.List;

public class Papel {

    private Integer id;
    private String nomePapel;

    private List<Usuario> usuarios;

    public Papel() {
    }

    public Papel(Integer id, String nomePapel) {
        this.id = id;
        this.nomePapel = nomePapel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomePapel() {
        return nomePapel;
    }

    public void setNomePapel(String nomePapel) {
        this.nomePapel = nomePapel;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}