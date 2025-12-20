package com.sistemaclinico.service;

import java.time.LocalTime; // Importante adicionar

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
        
        LocalTime horario = consulta.getHorario();
        LocalTime inicio = horario.minusMinutes(29);
        LocalTime fim = horario.plusMinutes(29);

        if (inicio.isAfter(horario)) inicio = LocalTime.MIN; 
        if (fim.isBefore(horario)) fim = LocalTime.MAX;      

        boolean horarioOcupado;

        if (consulta.getCodigo() == null) {

            horarioOcupado = repository.existsByMedicoAndDataAndStatusNotAndHorarioBetween(
                consulta.getMedico(), 
                consulta.getData(),
                StatusConsulta.CANCELADO, 
                inicio,
                fim
            );
        } else {

            horarioOcupado = repository.existsByMedicoAndDataAndStatusNotAndHorarioBetweenAndCodigoNot(
                consulta.getMedico(), 
                consulta.getData(),
                StatusConsulta.CANCELADO, 
                inicio,
                fim,
                consulta.getCodigo()
            );
        }

        if (horarioOcupado) {
            throw new IllegalArgumentException("Conflito de horário. O médico precisa de um intervalo de 30 minutos entre consultas.");
        }

        repository.save(consulta);
    }

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