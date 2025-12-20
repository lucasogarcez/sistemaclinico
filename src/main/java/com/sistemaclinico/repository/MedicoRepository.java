package com.sistemaclinico.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sistemaclinico.model.Medico;
import com.sistemaclinico.model.enums.StatusPessoa; // <--- Importante verificar esse import
import com.sistemaclinico.repository.queries.medico.MedicoQueries;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long>, MedicoQueries {

    List<Medico> findByCrmContaining(String crm);
    Medico findByCrm(String crm);
    
    List<Medico> findByNomeContainingIgnoreCase(String nome);

    List<Medico> findByNomeContainingIgnoreCaseAndStatus(String nome, StatusPessoa status);

    List<Medico> findByStatus(StatusPessoa status);
}