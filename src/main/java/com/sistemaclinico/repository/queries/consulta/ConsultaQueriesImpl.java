package com.sistemaclinico.repository.queries.consulta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository; // Adicionado
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import com.sistemaclinico.filter.ConsultaFilter;
import com.sistemaclinico.model.Consulta;
import com.sistemaclinico.pagination.PaginacaoUtil;

@Repository // Importante para o Spring achar essa classe
public class ConsultaQueriesImpl implements ConsultaQueries {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Consulta> pesquisar(ConsultaFilter filtro, Pageable pageable) {
        StringBuilder queryConsultas = new StringBuilder("select distinct a from Consulta a inner join fetch a.paciente inner join fetch a.medico");
        StringBuilder condicoes = new StringBuilder();
        Map<String, Object> parametros = new HashMap<>();

        preencherCondicoesEParametros(filtro, condicoes, parametros);

        if (condicoes.isEmpty()) {
            condicoes.append(" where a.status = 'AGENDADO'"); // Ajustado para bater com seu Enum (ou use ATIVO se for String pura)
        } else {
            condicoes.append(" and a.status = 'AGENDADO'");
        }

        queryConsultas.append(condicoes);
        PaginacaoUtil.prepararOrdemJPQL(queryConsultas, "a", pageable);
        TypedQuery<Consulta> typedQuery = em.createQuery(queryConsultas.toString(), Consulta.class);
        PaginacaoUtil.prepararIntervalo(typedQuery, pageable);
        PaginacaoUtil.preencherParametros(parametros, typedQuery);
        List<Consulta> consultas = typedQuery.getResultList();

        long totalConsultas = PaginacaoUtil.getTotalRegistros("Consulta", "a", condicoes, parametros, em);

        return new PageImpl<>(consultas, pageable, totalConsultas);
    }

    @Override
    public List<Consulta> pesquisar(ConsultaFilter filtro) {
        StringBuilder queryConsultas = new StringBuilder("select distinct a from Consulta a inner join fetch a.paciente inner join fetch a.medico");
        StringBuilder condicoes = new StringBuilder();
        Map<String, Object> parametros = new HashMap<>();

        preencherCondicoesEParametros(filtro, condicoes, parametros);

        if (condicoes.isEmpty()) {
            condicoes.append(" where a.status = 'AGENDADO'");
        } else {
            condicoes.append(" and a.status = 'AGENDADO'");
        }

        queryConsultas.append(condicoes);
        TypedQuery<Consulta> typedQuery = em.createQuery(queryConsultas.toString(), Consulta.class);
        PaginacaoUtil.preencherParametros(parametros, typedQuery);
        return typedQuery.getResultList();
    }

    @Override
    public Consulta buscarCompleta(long codigo) {
        String queryConsultas = "select distinct a from Consulta a inner join fetch a.paciente inner join fetch a.medico where a.codigo = :codigo";
        TypedQuery<Consulta> typedQuery = em.createQuery(queryConsultas, Consulta.class);
        typedQuery.setParameter("codigo", codigo);
        return typedQuery.getSingleResult();
    }

    private void preencherCondicoesEParametros(ConsultaFilter filtro, StringBuilder condicoes, Map<String, Object> parametros) {
        boolean condicao = false;

        if (filtro.getCodigo() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("a.codigo = :codigo");
            parametros.put("codigo", filtro.getCodigo());
            condicao = true;
        }
        if (filtro.getData() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            // CORREÇÃO: JPQL usa = e não ==
            condicoes.append("a.data = :data"); 
            parametros.put("data", filtro.getData());
            condicao = true;
        }
        if (filtro.getHorario() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            // CORREÇÃO: JPQL usa = e não ==
            condicoes.append("a.horario = :horario");
            parametros.put("horario", filtro.getHorario());
            condicao = true;
        }
        if (filtro.getPaciente() != null && filtro.getPaciente().getCpf() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("a.paciente.cpf like :cpf");
            parametros.put("cpf", "%" + filtro.getPaciente().getCpf() + "%");
        }
        if (filtro.getMedico() != null && filtro.getMedico().getCrm() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("a.medico.crm like :crm");
            parametros.put("crm", "%" + filtro.getMedico().getCrm() + "%");
        }
    }
}