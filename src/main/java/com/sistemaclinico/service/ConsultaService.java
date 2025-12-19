package com.sistemaclinico.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistemaclinico.model.Consulta;
import com.sistemaclinico.model.enums.StatusConsulta;
import com.sistemaclinico.repository.ConsultaRepository;

@Service
public class ConsultaService {

    private ConsultaRepository repository;

    public ConsultaService(ConsultaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void salvar(Consulta consulta) {
        if (consulta.getCodigo() == null) {
            consulta.setStatus(StatusConsulta.AGENDADO);
        }

        // Verifica se existe agendamento para este médico, nesta hora, que NÃO esteja cancelado
        boolean horarioOcupado = repository.existsByMedicoAndDataAndHorarioAndStatusNot(
            consulta.getMedico(), 
            consulta.getData(),
            consulta.getHorario(), 
            StatusConsulta.CANCELADO
        );

        if (horarioOcupado) {
            throw new IllegalArgumentException("Este médico já possui agendamento neste horário.");
        }

        repository.save(consulta);
    }

    // Busca para preencher a tela de atendimento
    public Consulta buscarPorCodigo(Long codigo) {
        return repository.findById(codigo)
            .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada"));
    }

    @Transactional
    public void finalizarConsulta(Long codigo, Consulta dadosFormulario) {
        Consulta consultaBanco = repository.findById(codigo)
            .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada"));
        
        consultaBanco.setPesoKg(dadosFormulario.getPesoKg());
        consultaBanco.setPressaoArterial(dadosFormulario.getPressaoArterial());
        consultaBanco.setTemperaturaCelsius(dadosFormulario.getTemperaturaCelsius());
        consultaBanco.setObservacoes(dadosFormulario.getObservacoes());
        consultaBanco.setReceita(dadosFormulario.getReceita());
        consultaBanco.setSolicitacaoExames(dadosFormulario.getSolicitacaoExames());
        consultaBanco.setDesfecho(dadosFormulario.getDesfecho());
        
        consultaBanco.setStatus(StatusConsulta.FINALIZADO);
        
        repository.save(consultaBanco);
    }

    @Transactional
    public void cancelarConsulta(Long codigo) {
        Consulta consulta = repository.findById(codigo).get();
        consulta.setStatus(StatusConsulta.CANCELADO);
        repository.save(consulta);
    }
}