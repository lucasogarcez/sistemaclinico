package com.sistemaclinico.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sistemaclinico.model.Paciente;
import com.sistemaclinico.repository.queries.paciente.PacienteQueries;

public interface PacienteRepository extends JpaRepository<Paciente, Long>, PacienteQueries {

    public List<Paciente> findByCpfContaining(String cpf);

    public Paciente findByCpf(String cpf);

}
