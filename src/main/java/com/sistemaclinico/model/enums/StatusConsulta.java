package com.sistemaclinico.model.enums;

public enum StatusConsulta {
    AGENDADO("agendado"),
    CANCELADO("cancelado"),
    FINALIZADO("finalizado");

    private String descricao;
	
	private StatusConsulta(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
}
