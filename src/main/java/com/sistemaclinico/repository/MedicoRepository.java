package com.sistemaclinico.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sistemaclinico.model.Medico;
import com.sistemaclinico.repository.queries.medico.MedicoQueries;

public interface MedicoRepository extends JpaRepository<Medico, Long>, MedicoQueries {

    public List<Medico> findByCrmContaining(String crm);

    public Medico findByCrm(String crm);

}
