package com.sistemaclinico.repository;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistemaclinico.filter.ConsultaFilter;
import com.sistemaclinico.model.Consulta;
import com.sistemaclinico.model.Medico;
import com.sistemaclinico.model.enums.StatusConsulta;
import com.sistemaclinico.repository.queries.consulta.ConsultaQueries;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long>, ConsultaQueries {
    
    Page<Consulta> pesquisar(ConsultaFilter filtro, Pageable pageable);

    boolean existsByMedicoAndDataAndHorarioAndStatusNot(Medico medico, LocalDate data, LocalTime horario, StatusConsulta status);
<<<<<<< HEAD
=======

    boolean existsByMedicoAndDataAndHorarioAndStatusNotAndCodigoNot(Medico medico, LocalDate data, LocalTime horario, StatusConsulta status, Long codigo);
>>>>>>> bfb3025 (Correção visuais e validações)
}