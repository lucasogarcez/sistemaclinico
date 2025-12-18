package com.sistemaclinico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sistemaclinico.model.Consulta;
import com.sistemaclinico.repository.queries.consulta.ConsultaQueries;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long>, ConsultaQueries {
    // Agora ele herda os métodos padrões do JPA + os seus métodos 'pesquisar'
}