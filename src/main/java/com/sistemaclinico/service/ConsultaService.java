package com.sistemaclinico.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sistemaclinico.model.Consulta;
import com.sistemaclinico.repository.ConsultaRepository;

@Service
public class ConsultaService {

    private ConsultaRepository repository;

    public ConsultaService(ConsultaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void salvar(Consulta consulta) {
        repository.save(consulta);
    }

    @Transactional
    public void alterar(Consulta consulta) {
        repository.save(consulta);
    }

}
