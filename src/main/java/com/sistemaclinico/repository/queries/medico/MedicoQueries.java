package com.sistemaclinico.repository.queries.medico;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sistemaclinico.filter.MedicoFilter;
import com.sistemaclinico.model.Medico;

public interface MedicoQueries {

	Page<Medico> pesquisar(MedicoFilter filtro, Pageable pageable);
	
	List<Medico> pesquisar(MedicoFilter filtro);

	List<Medico> pesquisarGeral(String filtro);

}
