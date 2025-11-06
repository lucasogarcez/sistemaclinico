package com.sistemaclinico.clinico.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Consulta {
    private Integer id;
    private LocalDateTime dataHora;
    private String pressaoArterial;
    private BigDecimal pesoKg;
    private BigDecimal alturaM;
    private BigDecimal temperaturaCelsius;
    private String anotacoesSintomas;
    private String examesSolicitados;
    private String laudoConclusao;

    private Paciente paciente;

    private Medico medico;

    public Consulta() {
    }

    public Consulta(Integer id, LocalDateTime dataHora, Paciente paciente, Medico medico) {
        this.id = id;
        this.dataHora = dataHora;
        this.paciente = paciente;
        this.medico = medico;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getPressaoArterial() {
        return pressaoArterial;
    }

    public void setPressaoArterial(String pressaoArterial) {
        this.pressaoArterial = pressaoArterial;
    }

    public BigDecimal getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(BigDecimal pesoKg) {
        this.pesoKg = pesoKg;
    }

    public BigDecimal getAlturaM() {
        return alturaM;
    }

    public void setAlturaM(BigDecimal alturaM) {
        this.alturaM = alturaM;
    }

    public BigDecimal getTemperaturaCelsius() {
        return temperaturaCelsius;
    }

    public void setTemperaturaCelsius(BigDecimal temperaturaCelsius) {
        this.temperaturaCelsius = temperaturaCelsius;
    }

    public String getAnotacoesSintomas() {
        return anotacoesSintomas;
    }

    public void setAnotacoesSintomas(String anotacoesSintomas) {
        this.anotacoesSintomas = anotacoesSintomas;
    }

    public String getExamesSolicitados() {
        return examesSolicitados;
    }

    public void setExamesSolicitados(String examesSolicitados) {
        this.examesSolicitados = examesSolicitados;
    }

    public String getLaudoConclusao() {
        return laudoConclusao;
    }

    public void setLaudoConclusao(String laudoConclusao) {
        this.laudoConclusao = laudoConclusao;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }
}
