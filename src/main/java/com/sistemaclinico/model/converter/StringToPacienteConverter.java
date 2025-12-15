package com.sistemaclinico.model.converter;

// import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import com.sistemaclinico.model.Paciente;
import com.sistemaclinico.repository.PacienteRepository;

@Component
public class StringToPacienteConverter implements Converter<String, Paciente> {

	private static final Logger logger = LoggerFactory.getLogger(StringToPacienteConverter.class);
	
	private PacienteRepository pacienteRepository;

	public StringToPacienteConverter(PacienteRepository pacienteRepository) {
		logger.trace(">>> Criando um StringToPacienteConverter");
		this.pacienteRepository = pacienteRepository;
	}
	
	@Override
	public Paciente convert(@NonNull String from) {
		logger.trace(">>> Convertendo a String: {} em uma Paciente", from);
		Paciente paciente = pacienteRepository.findByCpf(from);
		logger.info("Paciente buscada: {}", paciente);
		return paciente;
	}
}

