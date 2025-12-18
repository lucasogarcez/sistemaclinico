package com.sistemaclinico.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sistemaclinico.model.Paciente;
import com.sistemaclinico.repository.queries.paciente.PacienteQueries;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>, PacienteQueries {

    // Métodos automáticos do Spring Data
    List<Paciente> findByCpfContaining(String cpf);
    Paciente findByCpf(String cpf);
}