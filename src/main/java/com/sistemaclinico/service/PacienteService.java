package com.sistemaclinico.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sistemaclinico.model.Paciente;
import com.sistemaclinico.repository.PacienteRepository;

@Service
public class PacienteService {

    private PacienteRepository repository;

    public PacienteService(PacienteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void salvar(Paciente paciente) {
        repository.save(paciente);
    }

    @Transactional
    public void alterar(Paciente paciente) {
        repository.save(paciente);
    }

    @Transactional
    public void remover(Paciente paciente) {
        repository.delete(paciente);
    }

}
