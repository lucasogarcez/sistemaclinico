package com.sistemaclinico.filter;

import java.time.LocalDate;
import java.time.LocalTime;

import com.sistemaclinico.model.Medico;
import com.sistemaclinico.model.Paciente;

public class ConsultaFilter {
    private Long codigo;
    private LocalDate data;
    private LocalTime horario;
    private Paciente paciente;
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
        return "ConsultaFilter [codigo=" + codigo + ", data=" + data + ", horario="
                + horario + ", paciente=" + paciente + ", medico=" + medico + "]";
    }
}
