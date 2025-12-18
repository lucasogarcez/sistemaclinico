package com.sistemaclinico.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import com.sistemaclinico.model.enums.StatusConsulta;
import com.sistemaclinico.model.enums.TipoDesfecho;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "consulta")
public class Consulta implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @SequenceGenerator(name="gerador3", sequenceName="consulta_codigo_seq", allocationSize=1)
    @GeneratedValue(generator="gerador3", strategy=GenerationType.SEQUENCE)
    private Long codigo;

    @NotNull(message = "A data é obrigatória")
    private LocalDate data;

    @NotNull(message = "O horário é obrigatório")
    private LocalTime horario;

    @Enumerated(EnumType.STRING)
    private StatusConsulta status = StatusConsulta.AGENDADO; // CORREÇÃO: Inicializa com padrão

    // CORREÇÃO: Adicionado name="peso" pois no Java é pesoKg
    @Column(name = "peso") 
    private BigDecimal pesoKg;

    // CORREÇÃO: Adicionado name="altura" pois no Java é alturaM
    @Column(name = "altura")
    private BigDecimal alturaM;

    private String pressaoArterial; // Esse funciona automático (pressaoArterial -> pressao_arterial)

    // CORREÇÃO: Adicionado name="temperatura" pois no Java é temperaturaCelsius
    @Column(name = "temperatura")
    private BigDecimal temperaturaCelsius; // Note: No SQL está VARCHAR, mas BigDecimal é melhor pra conta. Se quiser manter String, tudo bem.

    @Column(columnDefinition = "TEXT")
    private String observacoes;
    
    @Column(columnDefinition = "TEXT")
    private String receita;

    @Column(columnDefinition = "TEXT")
    private String solicitacaoExames; // Funciona automático (solicitacaoExames -> solicitacao_exames)

    @Enumerated(EnumType.STRING)
    private TipoDesfecho desfecho;

    @NotNull(message = "O paciente é obrigatório")
    @ManyToOne
    @JoinColumn(name = "codigo_paciente")
    private Paciente paciente;

    @NotNull(message = "O médico é obrigatório")
    @ManyToOne
    @JoinColumn(name = "codigo_medico")
    private Medico medico;

    // --- GETTERS E SETTERS ---

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public StatusConsulta getStatus() {
        return status;
    }

    public void setStatus(StatusConsulta status) {
        this.status = status;
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

    // Ajuste aqui se decidir manter String ou mudar para BigDecimal no banco
    public BigDecimal getTemperaturaCelsius() {
        return temperaturaCelsius;
    }

    public void setTemperaturaCelsius(BigDecimal temperaturaCelsius) {
        this.temperaturaCelsius = temperaturaCelsius;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    
    // CORREÇÃO: Adicionado Get e Set de receita que faltava
    public String getReceita() {
        return receita;
    }

    public void setReceita(String receita) {
        this.receita = receita;
    }

    public String getSolicitacaoExames() {
        return solicitacaoExames;
    }

    public void setSolicitacaoExames(String solicitacaoExames) {
        this.solicitacaoExames = solicitacaoExames;
    }

    public TipoDesfecho getDesfecho() {
        return desfecho;
    }

    public void setDesfecho(TipoDesfecho desfecho) {
        this.desfecho = desfecho;
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

    @Override
    public String toString() {
        return "Consulta [codigo=" + codigo + ", data=" + data + ", horario=" + horario + ", status=" + status + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Consulta other = (Consulta) obj;
        return Objects.equals(codigo, other.codigo);
    }
}