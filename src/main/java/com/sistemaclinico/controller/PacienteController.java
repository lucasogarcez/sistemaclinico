package com.sistemaclinico.controller;

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
import com.sistemaclinico.filter.PacienteFilter;
import com.sistemaclinico.model.Paciente;
import com.sistemaclinico.model.enums.StatusPessoa;
import com.sistemaclinico.notification.NotificacaoSweetAlert2;
import com.sistemaclinico.notification.TipoNotificaoSweetAlert2;
import com.sistemaclinico.pagination.PageWrapper;
import com.sistemaclinico.repository.PacienteRepository;
import com.sistemaclinico.service.PacienteService;

@Controller
public class PacienteController {

    private static final Logger logger = LoggerFactory.getLogger(PacienteController.class);

    private PacienteRepository repository;
    private PacienteService service;

    public PacienteController(PacienteRepository repository, PacienteService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping("/paciente/abrirpesquisa")
    public String abrirPesquisa() {
        return "paciente/pesquisar :: formulario";
    }

    @GetMapping("/paciente/pesquisar")
    public String pesquisar(PacienteFilter filtro, Model model,
            @PageableDefault(size = 7) @SortDefault(sort = "codigo",
                    direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Paciente> pagina = repository.pesquisar(filtro, pageable);
        logger.info("Pacientes pesquisados: {}", pagina.getContent());
        PageWrapper<Paciente> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "paciente/mostrar :: tabela";
    }

    @GetMapping("/paciente/cadastrar")
    public String abrirCadastro(Paciente paciente) {
        return "paciente/cadastrar :: formulario";
    }

    @PostMapping("/paciente/cadastrar")
    public String cadastrar(@Valid Paciente paciente, BindingResult resultado,
            RedirectAttributes atributos) {
        if (resultado.hasErrors()) {
            logger.info("O paciente recebido para cadastrar não é válido.");
            logger.info("Erros encontrados:");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            for (ObjectError erro : resultado.getGlobalErrors()) {
                logger.info("{}", erro);
            }
            return "paciente/cadastrar :: formulario";
        } else {
            service.salvar(paciente);
            atributos.addFlashAttribute("notificacao", 
                new NotificacaoSweetAlert2("Paciente cadastrado com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
            return "redirect:/paciente/cadastrar";
        }
    }

    @GetMapping("/paciente/mostrarmensagem")
    public String mostrarMensagem() {
        return "mensagem :: texto";
    }

    @GetMapping("/paciente/alterar/{codigo}")
    public String abrirAlterar(@PathVariable("codigo") Long codigo, Model model) {
        PacienteFilter filtro = new PacienteFilter();
        filtro.setCodigo(codigo);
        List<Paciente> pacientes = repository.pesquisar(filtro);
        if (!pacientes.isEmpty()) {
            model.addAttribute("paciente", pacientes.get(0));
            return "paciente/alterar :: formulario";
        } else {
            model.addAttribute("mensagem", "Não existe um paciente com o codigo " + codigo);

            return "mensagem :: texto";
        }
    }

    @PostMapping("/paciente/alterar")
    public String alterar(@Valid Paciente paciente, BindingResult resultado,
            RedirectAttributes atributos) {
        if (resultado.hasErrors()) {
            logger.info("O paciente recebida para alterar não é válido.");
            logger.info("Erros encontrados:");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            for (ObjectError erro : resultado.getGlobalErrors()) {
                logger.info("{}", erro);
            }
            return "paciente/alterar :: formulario";
        } else {
            service.alterar(paciente);
            atributos.addFlashAttribute("notificacao", 
                  new NotificacaoSweetAlert2("Paciente alterado com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
            return "redirect:/paciente/abrirpesquisa";
        }
    }

    @GetMapping("/paciente/remover/{codigo}")
    public String remover(@PathVariable("codigo") Long codigo, Model model,
            RedirectAttributes atributos) {
        Optional<Paciente> pacienteOptional = repository.findById(codigo);
        if (pacienteOptional.isPresent()) {
            Paciente paciente = pacienteOptional.get();
            paciente.setStatus(StatusPessoa.INATIVO);
            service.alterar(paciente);
            atributos.addFlashAttribute("notificacao", 
                new NotificacaoSweetAlert2("Paciente removido com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000)); 
            return "redirect:/paciente/abrirpesquisa";
        } else {
            model.addAttribute("mensagem", "Não existe um paciente com o codigo " + codigo);
            return "mensagem :: texto";
        }
    }

}
