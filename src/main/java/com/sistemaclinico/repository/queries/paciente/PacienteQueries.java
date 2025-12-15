package com.sistemaclinico.repository.queries.paciente;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sistemaclinico.model.Paciente;

import com.sistemaclinico.filter.PacienteFilter;

public interface PacienteQueries {

	Page<Paciente> pesquisar(PacienteFilter filtro, Pageable pageable);
	
	List<Paciente> pesquisar(PacienteFilter filtro);

	List<Paciente> pesquisarGeral(String filtro);

}
