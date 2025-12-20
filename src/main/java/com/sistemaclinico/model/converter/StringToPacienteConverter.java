package com.sistemaclinico.model.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.sistemaclinico.model.Paciente;
import com.sistemaclinico.repository.PacienteRepository;

@Component
public class StringToPacienteConverter implements Converter<String, Paciente> {

    @Autowired
    private PacienteRepository repository;

    @Override
    public Paciente convert(String source) {
        if (!StringUtils.hasText(source)) {
            return null;
        }

        try {
            Long id = Long.valueOf(source);
            return repository.findById(id).orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}