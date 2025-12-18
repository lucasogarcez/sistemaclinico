package com.sistemaclinico.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sistemaclinico.model.Medico;
import com.sistemaclinico.repository.queries.medico.MedicoQueries;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long>, MedicoQueries {

    // Métodos automáticos do Spring Data
    List<Medico> findByCrmContaining(String crm);
    Medico findByCrm(String crm);
}