package com.sistemaclinico.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sistemaclinico.model.Paciente;
import com.sistemaclinico.model.enums.StatusPessoa; // <--- Importante
import com.sistemaclinico.repository.queries.paciente.PacienteQueries;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>, PacienteQueries {

    List<Paciente> findByCpfContaining(String cpf);
    Paciente findByCpf(String cpf);

    List<Paciente> findByNomeContainingIgnoreCase(String nome);

    List<Paciente> findByNomeContainingIgnoreCaseAndStatus(String nome, StatusPessoa status);

    List<Paciente> findByStatus(StatusPessoa status);
}