package com.sistemaclinico.repository.queries.consulta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import com.sistemaclinico.filter.ConsultaFilter;
import com.sistemaclinico.model.Consulta;
import com.sistemaclinico.pagination.PaginacaoUtil;

public class ConsultaQueriesImpl implements ConsultaQueries {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<Consulta> pesquisar(ConsultaFilter filtro, Pageable pageable) {

		StringBuilder queryConsultas =
				new StringBuilder("select distinct a from Consulta a inner join fetch a.paciente inner join fetch a.medico");
		StringBuilder condicoes = new StringBuilder();
		Map<String, Object> parametros = new HashMap<>();

		preencherCondicoesEParametros(filtro, condicoes, parametros);

		if (condicoes.isEmpty()) {
			condicoes.append(" where a.status = 'ATIVO'");
		} else {
			condicoes.append(" and a.status = 'ATIVO'");
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

	public List<Consulta> pesquisar(ConsultaFilter filtro) {
		StringBuilder queryConsultas =
				new StringBuilder("select distinct a from Consulta a inner join fetch a.paciente inner join fetch a.medico");
		StringBuilder condicoes = new StringBuilder();
		Map<String, Object> parametros = new HashMap<>();

		preencherCondicoesEParametros(filtro, condicoes, parametros);

		if (condicoes.isEmpty()) {
			condicoes.append(" where a.status = 'ATIVO'");
		} else {
			condicoes.append(" and a.status = 'ATIVO'");
		}

		queryConsultas.append(condicoes);
		TypedQuery<Consulta> typedQuery = em.createQuery(queryConsultas.toString(), Consulta.class);
		PaginacaoUtil.preencherParametros(parametros, typedQuery);
		List<Consulta> consultas = typedQuery.getResultList();
		
		return consultas;
	}

	public Consulta buscarCompleta(long codigo) {
		String queryConsultas = "select distinct a from Consulta a inner join fetch a.paciente inner join fetch a.medico where a.codigo = :codigo and a.status = 'ATIVO'";
		TypedQuery<Consulta> typedQuery = em.createQuery(queryConsultas.toString(), Consulta.class);
		typedQuery.setParameter("codigo", codigo);
		Consulta consulta = typedQuery.getSingleResult();
		return consulta;
	}

	private void preencherCondicoesEParametros(ConsultaFilter filtro, StringBuilder condicoes,
			Map<String, Object> parametros) {
		boolean condicao = false;

		if (filtro.getCodigo() != null) {
			PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("a.codigo = :codigo");
			parametros.put("codigo", filtro.getCodigo());
			condicao = true;
		}
		if (filtro.getData() != null) {
			PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("a.data == :data");
			parametros.put("data", filtro.getData());
			condicao = true;
		}
		if (filtro.getHorario() != null) {
			PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("a.horario == :horario");
			parametros.put("horario", filtro.getHorario());
			condicao = true;
		}
		if (filtro.getPaciente() != null) {
			PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("a.paciente.cpf like :cpf");
			parametros.put("cpf", "%" + filtro.getPaciente().getCpf() + "%");
		}
		if (filtro.getMedico() != null) {
			PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("a.medico.crm like :crm");
			parametros.put("crm", "%" + filtro.getMedico().getCrm() + "%");
		}
	}

	

}
