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

import com.sistemaclinico.filter.MedicoFilter;
import com.sistemaclinico.model.Medico;
import com.sistemaclinico.model.enums.StatusPessoa;
import com.sistemaclinico.notification.NotificacaoSweetAlert2;
import com.sistemaclinico.notification.TipoNotificaoSweetAlert2;
import com.sistemaclinico.pagination.PageWrapper;
import com.sistemaclinico.repository.MedicoRepository;
import com.sistemaclinico.service.MedicoService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class MedicoController {

    private static final Logger logger = LoggerFactory.getLogger(MedicoController.class);

    private MedicoRepository repository;
    private MedicoService service;

    public MedicoController(MedicoRepository repository, MedicoService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping("/medico/abrirpesquisa")
    public String abrirPesquisa() {
        return "medico/pesquisar :: formulario";
    }

    @GetMapping("/medico/pesquisar")
    public String pesquisar(MedicoFilter filtro, Model model,
            @PageableDefault(size = 7) @SortDefault(sort = "codigo",
                    direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Medico> pagina = repository.pesquisar(filtro, pageable);
        logger.info("Médicos pesquisados: {}", pagina.getContent());
        PageWrapper<Medico> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "medico/mostrar :: tabela";
    }

    @GetMapping("/medico/cadastrar")
    public String abrirCadastro(Medico medico) {
        return "medico/cadastrar :: formulario";
    }

    @PostMapping("/medico/cadastrar")
    public String cadastrar(@Valid Medico medico, BindingResult resultado,
            RedirectAttributes atributos) {
        if (resultado.hasErrors()) {
            logger.info("O medico recebido para cadastrar não é válido.");
            logger.info("Erros encontrados:");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            for (ObjectError erro : resultado.getGlobalErrors()) {
                logger.info("{}", erro);
            }
            return "medico/cadastrar :: formulario";
        } else {
            service.salvar(medico);
            atributos.addFlashAttribute("notificacao", 
                new NotificacaoSweetAlert2("Médico cadastrado com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
            return "redirect:/medico/cadastrar";
        }
    }

    @GetMapping("/medico/mostrarmensagem")
    public String mostrarMensagem() {
        return "mensagem :: texto";
    }

    @GetMapping("/medico/alterar/{codigo}")
    public String abrirAlterar(@PathVariable("codigo") Long codigo, Model model) {
        MedicoFilter filtro = new MedicoFilter();
        filtro.setCodigo(codigo);
        List<Medico> medicos = repository.pesquisar(filtro);
        if (!medicos.isEmpty()) {
            model.addAttribute("medico", medicos.get(0));
            return "medico/alterar :: formulario";
        } else {
            model.addAttribute("mensagem", "Não existe um medico com o codigo " + codigo);

            return "mensagem :: texto";
        }
    }

    @PostMapping("/medico/alterar")
    public String alterar(@Valid Medico medico, BindingResult resultado,
            RedirectAttributes atributos) {
        if (resultado.hasErrors()) {
            logger.info("O medico recebida para alterar não é válido.");
            logger.info("Erros encontrados:");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            for (ObjectError erro : resultado.getGlobalErrors()) {
                logger.info("{}", erro);
            }
            return "medico/alterar :: formulario";
        } else {
            service.alterar(medico);
            atributos.addFlashAttribute("notificacao", 
                  new NotificacaoSweetAlert2("Médico alterado com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
            return "redirect:/medico/abrirpesquisa";
        }
    }

    @GetMapping("/medico/remover/{codigo}")
    public String remover(@PathVariable("codigo") Long codigo, Model model,
            RedirectAttributes atributos) {
        Optional<Medico> medicoOptional = repository.findById(codigo);
        if (medicoOptional.isPresent()) {
            Medico medico = medicoOptional.get();
            medico.setStatus(StatusPessoa.INATIVO);
            service.alterar(medico);
            atributos.addFlashAttribute("notificacao", 
                new NotificacaoSweetAlert2("Médico removido com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000)); 
            return "redirect:/medico/abrirpesquisa";
        } else {
            model.addAttribute("mensagem", "Não existe um medico com o codigo " + codigo);
            return "mensagem :: texto";
        }
    }

}
