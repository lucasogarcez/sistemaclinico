package com.sistemaclinico.model.enums;

public enum StatusPessoa {

	ATIVO("ativo"),
	INATIVO("inativo");
	
	private String descricao;
	
	private StatusPessoa(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
}
