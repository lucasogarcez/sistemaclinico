package com.sistemaclinico.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sistemaclinico.filter.ConsultaFilter;
import com.sistemaclinico.filter.MedicoFilter;
import com.sistemaclinico.filter.PacienteFilter;
import com.sistemaclinico.model.Consulta;
import com.sistemaclinico.model.Medico;
import com.sistemaclinico.model.Paciente;
import com.sistemaclinico.model.enums.StatusConsulta;
import com.sistemaclinico.notification.NotificacaoSweetAlert2;
import com.sistemaclinico.notification.TipoNotificaoSweetAlert2;
import com.sistemaclinico.pagination.PageWrapper;
import com.sistemaclinico.repository.ConsultaRepository;
import com.sistemaclinico.repository.MedicoRepository;
import com.sistemaclinico.repository.PacienteRepository;
import com.sistemaclinico.service.ConsultaService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class ConsultaController {

    private static final Logger logger = LoggerFactory.getLogger(ConsultaController.class);

    private ConsultaRepository repository;
    private ConsultaService service;
    private MedicoRepository medicoRepository;
    private PacienteRepository pacienteRepository;

    public ConsultaController(ConsultaRepository repository, ConsultaService service, 
                              MedicoRepository medicoRepository, PacienteRepository pacienteRepository) {
        this.repository = repository;
        this.service = service;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @GetMapping("/consulta/abrirpesquisa")
    public String abrirPesquisa() {
        return "consulta/pesquisar :: formulario";
    }

    @GetMapping("/consulta/pesquisar")
    public String pesquisar(ConsultaFilter filtro, Model model,
            @PageableDefault(size = 7) @SortDefault(sort = "codigo", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) {
        Page<Consulta> pagina = repository.pesquisar(filtro, pageable);
        PageWrapper<Consulta> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "consulta/mostrar :: tabela";
    }

    @GetMapping("/consulta/cadastrar")
    public String abrirCadastro(Consulta consulta) {
        return "consulta/cadastrar :: formulario";
    }

    @PostMapping("/consulta/cadastrar")
    public String cadastrar(@Valid Consulta consulta, BindingResult resultado, RedirectAttributes atributos) {
        
        if (resultado.hasErrors()) {
            return "consulta/cadastrar :: formulario";
        }
        try {
            service.salvar(consulta);
            
            atributos.addFlashAttribute("notificacao", new NotificacaoSweetAlert2("Consulta agendada com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
            return "redirect:/consulta/cadastrar";
            
        } catch (IllegalArgumentException e) {
            resultado.rejectValue("horario", "erro.regraNegocio", e.getMessage());
            
            return "consulta/cadastrar :: formulario";
        }
    }

    // Abre a tela de atendimento (Prontuário)
    @GetMapping("/consulta/{codigo}/atender")
    public String iniciarAtendimento(@PathVariable Long codigo, Model model, HttpServletRequest request) {
        Consulta consulta = service.buscarPorCodigo(codigo);
    
        // Regra para bloquear edição se cancelado
        boolean podeEditar = consulta.getStatus() != StatusConsulta.CANCELADO;
        
        model.addAttribute("consulta", consulta);
        model.addAttribute("podeEditar", podeEditar);

        if (request.getHeader("HX-Request") != null) {
            return "consulta/atender :: atendimento-form";
        }

        return "consulta/atender";
    }

    // Salva o atendimento finalizado
    @PostMapping("/consulta/finalizar") 
    public String finalizarAtendimento(Consulta consulta, RedirectAttributes atributos) {
        try {
            service.finalizarConsulta(consulta.getCodigo(), consulta);
            
            atributos.addFlashAttribute("notificacao", 
                new NotificacaoSweetAlert2("Atendimento salvo com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
            
            return "redirect:/consulta/pesquisar"; 
        } catch (Exception e) {
            atributos.addFlashAttribute("notificacao",
                new NotificacaoSweetAlert2("Erro ao salvar: " + e.getMessage(), TipoNotificaoSweetAlert2.ERROR, 4000));
            return "redirect:/consulta/" + consulta.getCodigo() + "/atender";
        }
    }

    @PostMapping("/consulta/{codigo}/cancelar")
    public String cancelar(@PathVariable("codigo") Long codigo, RedirectAttributes atributos) {
        try {
            service.cancelarConsulta(codigo);
            atributos.addFlashAttribute("notificacao", 
                new NotificacaoSweetAlert2("Consulta cancelada com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));

        } catch (Exception e) {
            atributos.addFlashAttribute("notificacao",
                new NotificacaoSweetAlert2("Erro ao cancelar: " + e.getMessage(), TipoNotificaoSweetAlert2.ERROR, 4000));
        }
        
        return "redirect:/consulta/pesquisar";
    }

    @GetMapping("/consulta/pesquisarmedico")
    public String pesquisarMedico(@RequestParam(name = "medicoBusca", required = false) String nome, Model model) {
        MedicoFilter filtro = new MedicoFilter();
        filtro.setNome(nome);
        List<Medico> medicos = medicoRepository.pesquisar(filtro);
        model.addAttribute("listaItens", medicos);
        // Reutiliza um fragmento genérico ou cria um inline
        return "consulta/fragmentos-busca :: lista-medicos";
    }

    @GetMapping("/consulta/pesquisarpaciente")
    public String pesquisarPaciente(@RequestParam(name = "pacienteBusca", required = false) String nome, Model model) {
        PacienteFilter filtro = new PacienteFilter();
        filtro.setNome(nome);
        List<Paciente> pacientes = pacienteRepository.pesquisar(filtro);
        model.addAttribute("listaItens", pacientes);
        return "consulta/fragmentos-busca :: lista-pacientes";
    }
}