package com.sistemaclinico.model.enums;

public enum TipoDesfecho {
    INTERNACAO("internação"),
    CIRURGIA("cirurgia"),
    MEDICACAO("medicação"),
    EM_CASA("em casa");

    private String descricao;

    private TipoDesfecho(String descricao) {
		this.descricao = descricao;
	}

    public String getDescricao() {
        return descricao;
    }
}
