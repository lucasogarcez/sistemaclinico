package com.sistemaclinico.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import com.sistemaclinico.filter.ConsultaFilter;
import com.sistemaclinico.model.Consulta;
import com.sistemaclinico.model.Paciente;
import com.sistemaclinico.model.Medico;
import com.sistemaclinico.notification.NotificacaoSweetAlert2;
import com.sistemaclinico.notification.TipoNotificaoSweetAlert2;
import com.sistemaclinico.pagination.PageWrapper;
import com.sistemaclinico.repository.ConsultaRepository;
import com.sistemaclinico.repository.PacienteRepository;
import com.sistemaclinico.repository.MedicoRepository;
import com.sistemaclinico.service.ConsultaService;

@Controller
public class ConsultaController {

    private static final Logger logger = LoggerFactory.getLogger(ConsultaController.class);

    private ConsultaService consultaService;
    private PacienteRepository pacienteRepository;
    private MedicoRepository medicoRepository;
    private ConsultaRepository consultaRepository;

    public ConsultaController(ConsultaService consultaService,
            PacienteRepository pacienteRepository, 
            MedicoRepository medicoRepository,
            ConsultaRepository consultaRepository) {
        this.consultaService = consultaService;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.consultaRepository = consultaRepository;
    }

    @GetMapping("/consulta/cadastrar")
    public String abrirCadastro(Consulta consulta) {
        return "consulta/cadastrar :: formulario";
    }

    @GetMapping("/consulta/pesquisarpaciente")
    public String pesquisarPaciente(String pacienteBusca, Model model) {
        List<Paciente> pacientes = new ArrayList<>();
        if (pacienteBusca != null) {
            pacientes = pacienteRepository.findByCpfContaining(pacienteBusca);
            logger.debug("Pacientes buscados: {}", pacientes);
        }
        model.addAttribute("pacientes", pacientes);
        return "paciente/listar :: lista";
    }

    @GetMapping("/consulta/pesquisarmedico")
    public String pesquisarMedico(String medicoBusca, Model model) {
        List<Medico> medicos = new ArrayList<>();
        if (medicoBusca != null) {
            medicos = medicoRepository.findByCrmContaining(medicoBusca);
            logger.debug("Médicos buscados: {}", medicos);
        }
        model.addAttribute("medicos", medicos);
        return "medico/listar :: lista";
    }

    @PostMapping("/consulta/cadastrar")
    public String cadastrar(@Valid Consulta consulta, BindingResult resultado,
            RedirectAttributes atributos) {
        logger.debug("Paciente da consulta {}", consulta.getPaciente());
        logger.debug("Médico da consulta {}", consulta.getMedico());
        logger.debug("Data da consulta {}", consulta.getData());
        logger.debug("Horário da consulta {}", consulta.getHorario());
        if (resultado.hasErrors()) {
            logger.info("A consulta recebida para cadastrar não é válida.");
            logger.info("Erros encontrados:");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            for (ObjectError erro : resultado.getGlobalErrors()) {
                logger.info("{}", erro);
            }
            return "consulta/cadastrar :: formulario";
        } else {
            consultaService.salvar(consulta);
            atributos.addFlashAttribute("notificacao",
                    new NotificacaoSweetAlert2("Consulta cadastrada com sucesso!",
                            TipoNotificaoSweetAlert2.SUCCESS, 4000));
            return "redirect:/consulta/cadastrar";
        }
    }

    @GetMapping("/consulta/abrirpesquisa")
    public String abrirConsulta() {
        return "consulta/pesquisar :: formulario";
    }

    @GetMapping("/consulta/pesquisar")
    public String pesquisar(ConsultaFilter filtro, Model model,
            @PageableDefault(size = 8) @SortDefault(sort = "codigo",
                    direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Consulta> pagina = consultaRepository.pesquisar(filtro, pageable);
        logger.debug("Consultas pesquisadas: {}", pagina.getContent());
        PageWrapper<Consulta> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "consulta/mostrar :: tabela";
    }

    @GetMapping("/consulta/remover/{codigo}")
    public String remover(@PathVariable("codigo") Long codigo, RedirectAttributes atributos, Model model) {
        Optional<Consulta> optconsulta = consultaRepository.findById(codigo);
        if (optconsulta.isPresent()) {
            Consulta consulta = optconsulta.get();
            consultaService.alterar(consulta);
            atributos.addFlashAttribute("notificacao", new NotificacaoSweetAlert2("Consulta removida com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
            return "redirect:/consulta/abrirpesquisa";
        } else {
            model.addAttribute("mensagem", "Não foi encontrada uma consulta com esse código");
            return "mensagem :: texto";
        }
    }

    @GetMapping("/consulta/alterar/{codigo}")
    public String abrirAlterar(@PathVariable("codigo") Long codigo, Model model) {
        ConsultaFilter consultaFilter = new ConsultaFilter();
        consultaFilter.setCodigo(codigo);
        List<Consulta> consultas = consultaRepository.pesquisar(consultaFilter);
        if (!consultas.isEmpty()) {
            model.addAttribute("consulta", consultas.get(0));
            return "consulta/alterar :: formulario";
        } else {
            model.addAttribute("mensagem", "Não foi encontrada uma consulta com esse código");
            return "mensagem :: texto";
        }
    }

    @PostMapping("/consulta/alterar")
    public String alterar(@Valid Consulta consulta,  BindingResult resultado, RedirectAttributes atributos) {
        Paciente paciente = consulta.getPaciente();
        consulta.setPaciente(pacienteRepository.findByCpf(paciente.getCpf()));
        logger.debug("Paciente da consulta: {}", consulta.getPaciente());
        if (resultado.hasErrors()) {
            logger.info("A consulta recebida para alterar não é válida.");
            logger.info("Erros encontrados:");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            for (ObjectError erro : resultado.getGlobalErrors()) {
                logger.info("{}", erro);
            }
            return "consulta/alterar :: formulario";
        } else {
            // Consulta consultaAntiga = consultaRepository.buscarCompleta(aplicacao.getCodigo());
            // if (!aplicacao.getLote().equals(aplicacaoAntiga.getLote())) {
            //     Lote loteAntigo = aplicacaoAntiga.getLote();
            //     Lote loteNovo = aplicacao.getLote();
            //     loteAntigo.setNroDosesAtual(loteAntigo.getNroDosesAtual() + 1);
            //     loteNovo.setNroDosesAtual(loteNovo.getNroDosesAtual() - 1);
            //     loteService.alterar(loteAntigo);
            //     loteService.alterar(loteNovo);
            // }
            // aplicacaoService.alterar(aplicacao);
            atributos.addFlashAttribute("notificacao", new NotificacaoSweetAlert2("Consulta alterada com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
            return "redirect:/consulta/abrirpesquisa";
        }
    }
}
