package com.sistemaclinico.repository.queries.paciente;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import com.sistemaclinico.filter.PacienteFilter;
import com.sistemaclinico.model.Paciente;
import com.sistemaclinico.pagination.PaginacaoUtil;

public class PacienteQueriesImpl implements PacienteQueries {

	@PersistenceContext
	private EntityManager em;

	public List<Paciente> pesquisar(PacienteFilter filtro) {
		StringBuilder queryPacientes = new StringBuilder("select distinct p from Paciente p");
		StringBuilder condicoes = new StringBuilder();
		Map<String, Object> parametros = new HashMap<>();
		preencherCondicoesEParametros(filtro, condicoes, parametros);
		if (condicoes.isEmpty()) {
			condicoes.append(" where p.status = 'ATIVO'");
		} else {
			condicoes.append(" and p.status = 'ATIVO'");
		}
		queryPacientes.append(condicoes);
		TypedQuery<Paciente> typedQuery = em.createQuery(queryPacientes.toString(), Paciente.class);
		PaginacaoUtil.preencherParametros(parametros, typedQuery);
		List<Paciente> pacientes = typedQuery.getResultList();
		return pacientes;
	}

	public Page<Paciente> pesquisar(PacienteFilter filtro, Pageable pageable) {

		StringBuilder queryPacientes = new StringBuilder("select distinct p from Paciente p");
		StringBuilder condicoes = new StringBuilder();
		Map<String, Object> parametros = new HashMap<>();

		preencherCondicoesEParametros(filtro, condicoes, parametros);

		if (condicoes.isEmpty()) {
			condicoes.append(" where p.status = 'ATIVO'");
		} else {
			condicoes.append(" and p.status = 'ATIVO'");
		}

		queryPacientes.append(condicoes);
		PaginacaoUtil.prepararOrdemJPQL(queryPacientes, "p", pageable);
		TypedQuery<Paciente> typedQuery = em.createQuery(queryPacientes.toString(), Paciente.class);
		PaginacaoUtil.prepararIntervalo(typedQuery, pageable);
		PaginacaoUtil.preencherParametros(parametros, typedQuery);
		List<Paciente> pacientes = typedQuery.getResultList();

		long totalPacientes =
				PaginacaoUtil.getTotalRegistros("Paciente", "p", condicoes, parametros, em);

		return new PageImpl<>(pacientes, pageable, totalPacientes);
	}

	public List<Paciente> pesquisarGeral(String filtro) {
		StringBuilder queryPacientes = new StringBuilder("select distinct p from Paciente p");
		StringBuilder condicoes = new StringBuilder();
		Map<String, Object> parametros = new HashMap<>();
		preencherCondicoesEParametros(filtro, condicoes, parametros);
		if (condicoes.isEmpty()) {
			condicoes.append(" where p.status = 'ATIVO'");
		} else {
			condicoes.append(" and p.status = 'ATIVO'");
		}
		queryPacientes.append(condicoes);
		TypedQuery<Paciente> typedQuery = em.createQuery(queryPacientes.toString(), Paciente.class);
		PaginacaoUtil.preencherParametros(parametros, typedQuery);
		List<Paciente> paciente = typedQuery.getResultList();
		return paciente;
	}

	private void preencherCondicoesEParametros(String filtro, StringBuilder condicoes,
			Map<String, Object> parametros) {
		boolean condicao = false;
		try {
			Long codigo = Long.parseLong(filtro);
			if (!condicao) {
				condicoes.append(" where ");
			} else {
				condicoes.append(" or ");
			}
			condicoes.append("p.codigo = :codigo");
			parametros.put("codigo", codigo);
			condicao = true;
		} catch (NumberFormatException e) {
		}
		if (!condicao) {
			condicoes.append(" where ");
		} else {
			condicoes.append(" or ");
		}
		condicoes.append("lower(p.nome) like :nome");
		parametros.put("nome", "%" + filtro.toLowerCase() + "%");
		condicao = true;
	}

	private void preencherCondicoesEParametros(PacienteFilter filtro, StringBuilder condicoes,
			Map<String, Object> parametros) {
		boolean condicao = false;
		if (filtro.getCodigo() != null) {
			PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("p.codigo = :codigo");
			parametros.put("codigo", filtro.getCodigo());
			condicao = true;
		}
		if (StringUtils.hasText(filtro.getNome())) {
			PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("lower(p.nome) like :nome");
			parametros.put("nome", "%" + filtro.getNome().toLowerCase() + "%");
			condicao = true;
		}
		if (StringUtils.hasText(filtro.getCpf())) {
			PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("lower(p.cpf) like :cpf");
			parametros.put("cpf", "%" + filtro.getCpf().toLowerCase() + "%");
			condicao = true;
		}
		if (filtro.getDataNascimentoInicial() != null) {
			PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("p.dataNascimento >= :dataNascimentoInicial");
			parametros.put("dataNascimentoInicial", filtro.getDataNascimentoInicial());
			condicao = true;
		}
		if (filtro.getDataNascimentoFinal() != null) {
			PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
			condicoes.append("p.dataNascimento >= :dataNascimentoFinal");
			parametros.put("dataNascimentoFinal", filtro.getDataNascimentoFinal());
			condicao = true;
		}
	}

}
