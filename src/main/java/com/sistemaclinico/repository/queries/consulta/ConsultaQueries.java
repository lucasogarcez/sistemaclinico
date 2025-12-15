package com.sistemaclinico.repository.queries.consulta;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.sistemaclinico.filter.ConsultaFilter;
import com.sistemaclinico.model.Consulta;

public interface ConsultaQueries {

	Page<Consulta> pesquisar(ConsultaFilter filtro, Pageable pageable);

	List<Consulta> pesquisar(ConsultaFilter filtro);
	
	Consulta buscarCompleta(long codigo);
}
