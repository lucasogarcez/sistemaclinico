package com.sistemaclinico.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sistemaclinico.filter.PacienteFilter;
import com.sistemaclinico.model.Paciente;
import com.sistemaclinico.model.enums.StatusPessoa;
import com.sistemaclinico.notification.NotificacaoSweetAlert2;
import com.sistemaclinico.notification.TipoNotificaoSweetAlert2;
import com.sistemaclinico.pagination.PageWrapper;
import com.sistemaclinico.repository.PacienteRepository;
import com.sistemaclinico.service.PacienteService;
import com.sistemaclinico.service.RelatorioService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class PacienteController {

    private PacienteRepository repository;
    private PacienteService service;

    @Autowired
    private RelatorioService relatorioService;

    public PacienteController(PacienteRepository repository, PacienteService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping("/paciente/abrirpesquisa")
    public String abrirPesquisa(@RequestParam(required = false) String destino, Model model) {
        model.addAttribute("destino", destino);
        return "paciente/pesquisar :: formulario";
    }

    @GetMapping("/paciente/pesquisar")
    public String pesquisar(PacienteFilter filtro, @RequestParam(required = false) String destino, Model model,
            @PageableDefault(size = 7) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {

        Page<Paciente> pagina = repository.pesquisar(filtro, pageable);

        if ("historico".equals(destino) && pagina.getTotalElements() == 1) {
            Long codigoUnico = pagina.getContent().get(0).getCodigo();
            // Redireciona direto para o endpoint que gera o PDF
            return "redirect:/paciente/" + codigoUnico + "/historico-pdf";
        }

        PageWrapper<Paciente> paginaWrapper = new PageWrapper<>(pagina, request);

        model.addAttribute("pagina", paginaWrapper);
        model.addAttribute("destino", destino);

        return "paciente/mostrar :: tabela";
    }

    @GetMapping("/paciente/cadastrar")
    public String abrirCadastro(Paciente paciente, HttpServletRequest request) {
        if (request.getHeader("HX-Request") != null) {
            return "paciente/cadastrar :: formulario";
        }
        return "paciente/cadastrar";
    }

    @PostMapping("/paciente/cadastrar")
    public String cadastrar(@Valid Paciente paciente, BindingResult resultado, RedirectAttributes atributos) {
        
        if (paciente.getCpf() != null) {
            Paciente pacienteExistente = service.buscarPorCpf(paciente.getCpf());
            
            if (pacienteExistente != null) {
                resultado.rejectValue("cpf", "cpf.existente", "Já existe um paciente cadastrado com este CPF");
            }
        }

        if (resultado.hasErrors()) {
            return "paciente/cadastrar :: formulario";
        }

        service.salvar(paciente);
        atributos.addFlashAttribute("notificacao", new NotificacaoSweetAlert2("Paciente cadastrado com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
        return "redirect:/paciente/cadastrar";
    }

    @GetMapping("/paciente/alterar/{codigo}")
    public String abrirAlterar(@PathVariable("codigo") Long codigo, Model model) {
        Optional<Paciente> paciente = repository.findById(codigo);
        if (paciente.isPresent()) {
            model.addAttribute("paciente", paciente.get());
            return "paciente/alterar :: formulario";
        }
        model.addAttribute("mensagem", "Paciente não encontrado");
        return "mensagem :: texto";
    }

    @PostMapping("/paciente/alterar")
    public String alterar(@Valid Paciente paciente, BindingResult resultado, RedirectAttributes atributos, HttpServletRequest request, HttpServletResponse response) {

        if (paciente.getCpf() != null) {
            String cpfLimpo = paciente.getCpf().replaceAll("\\D", ""); 
            
            Paciente pacienteExistente = service.buscarPorCpf(cpfLimpo);

            if (pacienteExistente != null && !pacienteExistente.getCodigo().equals(paciente.getCodigo())) {
                resultado.rejectValue("cpf", "cpf.existente", "Este CPF já pertence a outro paciente");
            }
        }

        if (resultado.hasErrors()) {
            return "paciente/alterar :: formulario";
        }

        paciente.setCpf(paciente.getCpf().replaceAll("\\D", "")); 

        service.alterar(paciente);
        
        atributos.addFlashAttribute("notificacao", new NotificacaoSweetAlert2("Paciente alterado com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
        
        if (request.getHeader("HX-Request") != null) {
            response.setHeader("HX-Redirect", "/paciente/abrirpesquisa");
        }
        
        return "redirect:/paciente/abrirpesquisa";
    }
    
    @GetMapping("/paciente/remover/{codigo}")
    public String remover(@PathVariable("codigo") Long codigo, Model model, RedirectAttributes atributos, HttpServletRequest request, HttpServletResponse response) {
        Optional<Paciente> pacienteOptional = repository.findById(codigo);
        if (pacienteOptional.isPresent()) {
            Paciente paciente = pacienteOptional.get();
            paciente.setStatus(StatusPessoa.INATIVO); 
            service.alterar(paciente);
            
            atributos.addFlashAttribute("notificacao", new NotificacaoSweetAlert2("Paciente removido com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000)); 
            
            if (request.getHeader("HX-Request") != null) {
                response.setHeader("HX-Redirect", "/paciente/abrirpesquisa");
            }

            return "redirect:/paciente/abrirpesquisa";
        }
        return "mensagem :: texto";
    }

    @GetMapping("/paciente/{codigo}/historico-pdf")
    public ResponseEntity<byte[]> baixarHistorico(@PathVariable("codigo") Long codigo) {
        
        byte[] relatorio = relatorioService.gerarHistoricoPaciente(codigo);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=historico_paciente_" + codigo + ".pdf")
                .body(relatorio);
    }
}