package com.sistemaclinico.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import jakarta.persistence.Entity;
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
	private String pressaoArterial;
	private BigDecimal pesoKg;
    private BigDecimal alturaM;
    private BigDecimal temperaturaCelsius;
    private String anotacoesSintomas;
    private String examesSolicitados;
    private String laudoConclusao;
	@NotNull(message = "O paciente é obrigatório")
	@ManyToOne
	@JoinColumn(name = "codigo_paciente")
	private Paciente paciente;
	@NotNull(message = "O médico é obrigatório")
	@ManyToOne
	@JoinColumn(name = "codigo_medico")
	private Medico medico;

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

	@Override
	public String toString() {
		return "Consulta [codigo=" + codigo + ", data=" + data + ", horario=" + horario + ", pressaoArterial=" + pressaoArterial
				+ ", pesoKg=" + pesoKg + ", alturaM=" + alturaM + ", temperaturaCelsius=" + temperaturaCelsius
				+ ", anotacoesSintomas=" + anotacoesSintomas + ", examesSolicitados=" + examesSolicitados
				+ ", laudoConclusao=" + laudoConclusao + ", paciente=" + paciente + ", medico=" + medico + "]";
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
