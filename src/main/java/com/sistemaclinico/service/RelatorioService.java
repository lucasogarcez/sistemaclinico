package com.sistemaclinico.service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Service
public class RelatorioService {

    @Autowired
    private DataSource dataSource;

    public byte[] gerarHistoricoPaciente(Long idPaciente) {
        try (Connection connection = dataSource.getConnection()) {
            
            String caminho = "/relatorios/historico_paciente.jrxml";
            InputStream arquivoFonte = getClass().getResourceAsStream(caminho);

            if (arquivoFonte == null) {
                throw new FileNotFoundException("Arquivo " + caminho + " não encontrado!");
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(arquivoFonte);

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("ID_PACIENTE", idPaciente);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, connection);

            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar PDF: " + e.getMessage());
        }
    }

    public byte[] gerarLaudoMedico(Long idConsulta) {
        try (Connection connection = dataSource.getConnection()) {
            
            String caminho = "/relatorios/laudo_medico.jrxml";
            InputStream arquivoFonte = getClass().getResourceAsStream(caminho);

            if (arquivoFonte == null) {
                throw new FileNotFoundException("Arquivo " + caminho + " não encontrado!");
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(arquivoFonte);

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("ID_CONSULTA", idConsulta);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, connection);

            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar Laudo: " + e.getMessage());
        }
    }
}