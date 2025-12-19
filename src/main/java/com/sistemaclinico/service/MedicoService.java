package com.sistemaclinico.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistemaclinico.model.Medico;
import com.sistemaclinico.repository.MedicoRepository;

@Service
public class MedicoService {

    private MedicoRepository repository;

    public Medico buscarPorCrm(String crm) {
        return repository.findByCrm(crm);
    }

    public MedicoService(MedicoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void salvar(Medico medico) {
        repository.save(medico);
    }

    @Transactional
    public void alterar(Medico medico) {
        repository.save(medico);
    }

    @Transactional
    public void remover(Medico medico) {
        repository.delete(medico);
    }

}
