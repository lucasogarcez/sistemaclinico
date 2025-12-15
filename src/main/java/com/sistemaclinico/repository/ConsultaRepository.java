package com.sistemaclinico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sistemaclinico.model.Consulta;
import com.sistemaclinico.repository.queries.consulta.ConsultaQueries;

public interface ConsultaRepository extends JpaRepository<Consulta, Long>, ConsultaQueries {

}
