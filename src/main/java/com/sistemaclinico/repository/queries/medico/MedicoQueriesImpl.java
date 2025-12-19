package com.sistemaclinico.repository.queries.medico;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import com.sistemaclinico.filter.MedicoFilter;
import com.sistemaclinico.model.Medico;
import com.sistemaclinico.pagination.PaginacaoUtil;

@Repository
public class MedicoQueriesImpl implements MedicoQueries {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Medico> pesquisar(MedicoFilter filtro) {
        StringBuilder queryMedicos = new StringBuilder("select distinct p from Medico p");
        StringBuilder condicoes = new StringBuilder();
        Map<String, Object> parametros = new HashMap<>();
        
        preencherCondicoesEParametros(filtro, condicoes, parametros);
        
        if (condicoes.isEmpty()) {
            condicoes.append(" where p.status = 'ATIVO'");
        } else {
            condicoes.append(" and p.status = 'ATIVO'");
        }
        
        queryMedicos.append(condicoes);
        TypedQuery<Medico> typedQuery = em.createQuery(queryMedicos.toString(), Medico.class);
        PaginacaoUtil.preencherParametros(parametros, typedQuery);
        return typedQuery.getResultList();
    }

    @Override
    public Page<Medico> pesquisar(MedicoFilter filtro, Pageable pageable) {
        StringBuilder queryMedicos = new StringBuilder("select distinct p from Medico p");
        StringBuilder condicoes = new StringBuilder();
        Map<String, Object> parametros = new HashMap<>();

        preencherCondicoesEParametros(filtro, condicoes, parametros);

        if (condicoes.isEmpty()) {
            condicoes.append(" where p.status = 'ATIVO'");
        } else {
            condicoes.append(" and p.status = 'ATIVO'");
        }

        queryMedicos.append(condicoes);
        PaginacaoUtil.prepararOrdemJPQL(queryMedicos, "p", pageable);
        TypedQuery<Medico> typedQuery = em.createQuery(queryMedicos.toString(), Medico.class);
        PaginacaoUtil.prepararIntervalo(typedQuery, pageable);
        PaginacaoUtil.preencherParametros(parametros, typedQuery);
        
        List<Medico> medicos = typedQuery.getResultList();
        long totalMedicos = PaginacaoUtil.getTotalRegistros("Medico", "p", condicoes, parametros, em);

        return new PageImpl<>(medicos, pageable, totalMedicos);
    }

    @Override
    public List<Medico> pesquisarGeral(String filtro) {
        return pesquisar(new MedicoFilter()); 
    }

    private void preencherCondicoesEParametros(MedicoFilter filtro, StringBuilder condicoes, Map<String, Object> parametros) {
        boolean condicao = false;
        
        // Verifica Nome
        if (StringUtils.hasText(filtro.getNome())) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("lower(p.nome) like :nome");
            parametros.put("nome", "%" + filtro.getNome().toLowerCase() + "%");
            condicao = true;
        }
        
        // Verifica CRM
        if (StringUtils.hasText(filtro.getCrm())) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("lower(p.crm) like :crm"); // CORRIGIDO: Era crmf
            parametros.put("crm", "%" + filtro.getCrm().toLowerCase() + "%");
            condicao = true;
        }
        
        // Verifica Código
        if (filtro.getCodigo() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("p.codigo = :codigo");
            parametros.put("codigo", filtro.getCodigo());
        }
    }
}