package br.gov.ma.tce.sophia.ejb.beans.atividadecolaborador;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.gov.ma.tce.sophia.ejb.beans.atividade.Atividade;
import br.gov.ma.tce.sophia.ejb.beans.colaborador.Colaborador;
import br.gov.ma.tce.sophia.ejb.util.Filtro;

@Stateless
public class AtividadeColaboradorFacadeBean {

	@PersistenceContext(unitName = "sigesco_escex")
	public EntityManager manager;

	public AtividadeColaborador include(AtividadeColaborador atividadeColaborador) {
		manager.persist(atividadeColaborador);
		return atividadeColaborador;
	}

	public AtividadeColaborador update(AtividadeColaborador atividadeColaborador) {
		manager.merge(atividadeColaborador);
		return atividadeColaborador;
	}

	public void delete(int atividadeColaboradorId) {
		AtividadeColaborador atividadeColaborador = findByPrimaryKey(atividadeColaboradorId);
		manager.remove(atividadeColaborador);
	}

	public AtividadeColaborador findByPrimaryKey(Integer atividadeColaboradorId) {
		AtividadeColaborador atividadeColaborador = manager.find(AtividadeColaborador.class, atividadeColaboradorId);
		return atividadeColaborador;
	}

	public Collection<AtividadeColaborador> findAll() {
		Query q = manager.createQuery("select s from " + AtividadeColaborador.NAME + " s order by s.atividade.nome");
		Collection<AtividadeColaborador> atividadesColaboradores = new ArrayList<AtividadeColaborador>();
		for (Object o : q.getResultList()) {
			atividadesColaboradores.add((AtividadeColaborador) o);
		}
		return atividadesColaboradores;
	}

	public Collection<AtividadeColaborador> findColaboradoresByAtividade(Atividade atividade) {
		Query q = manager
				.createQuery("select s from " + AtividadeColaborador.NAME
						+ " s where s.atividade=:arg0 order by s.colaborador.pessoa.nome")
				.setParameter("arg0", atividade);
		Collection<AtividadeColaborador> colaboradores = new ArrayList<AtividadeColaborador>();
		for (Object o : q.getResultList()) {
			colaboradores.add((AtividadeColaborador) o);
		}
		return colaboradores;
	}

	public Collection<AtividadeColaborador> findColaboradoresByAtividadeAndCertificadoDisponivel(Atividade atividade,
			boolean certificadoDisponivel) {
		Query q = manager.createQuery("select s from " + AtividadeColaborador.NAME
				+ " s where s.atividade=:arg0 and s.certificadoDisponivel=:arg1 order by s.colaborador.pessoa.nome")
				.setParameter("arg0", atividade).setParameter("arg1", certificadoDisponivel);
		Collection<AtividadeColaborador> colaboradores = new ArrayList<AtividadeColaborador>();
		for (Object o : q.getResultList()) {
			colaboradores.add((AtividadeColaborador) o);
		}
		return colaboradores;
	}

	public Collection<AtividadeColaborador> findAtividadesByColaborador(Colaborador colaborador) {
		Query q = manager
				.createQuery("select s from " + AtividadeColaborador.NAME
						+ " s where s.colaborador=:arg0 order by s.atividade.evento.nome, s.atividade.nome")
				.setParameter("arg0", colaborador);
		Collection<AtividadeColaborador> colaboradores = new ArrayList<AtividadeColaborador>();
		for (Object o : q.getResultList()) {
			colaboradores.add((AtividadeColaborador) o);
		}
		return colaboradores;
	}

	public Collection<AtividadeColaborador> findAtividadesByColaboradorAndCertificadoDisponivel(
			Colaborador colaborador) {
		Query q = manager.createQuery("select s from " + AtividadeColaborador.NAME
				+ " s where s.colaborador=:arg0 and s.certificadoDisponivel=true order by s.atividade.evento.nome, s.atividade.nome")
				.setParameter("arg0", colaborador);
		Collection<AtividadeColaborador> colaboradores = new ArrayList<AtividadeColaborador>();
		for (Object o : q.getResultList()) {
			colaboradores.add((AtividadeColaborador) o);
		}
		return colaboradores;
	}

	@SuppressWarnings("unchecked")
	public Collection<AtividadeColaborador> findAtividadesByColaboradorAndFiltro(Colaborador colaborador,
			Filtro filtro) {
		StringBuilder query = new StringBuilder("select s from " + AtividadeColaborador.NAME
				+ " s where s.colaborador = " + colaborador.getColaboradorId());
		if (filtro.getFiltro1() != null && !filtro.getFiltro1().equals("")) {
			query.append(" and s.atividade.evento.nome like '%" + filtro.getFiltro1().toUpperCase().replaceAll("\'", "")
					+ "%' ");
		}
		if (filtro.getFiltro2() != null && !filtro.getFiltro2().equals("")) {
			query.append(
					" and s.atividade.nome like '%" + filtro.getFiltro2().toUpperCase().replaceAll("\'", "") + "%' ");
		}
		query.append(" order by s.atividade.evento.nome, s.atividade.nome");

		Query q = manager.createQuery(query.toString());
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Collection<AtividadeColaborador> findAtividadesByColaboradorAndCertificadoDisponivelAndFiltro(
			Colaborador colaborador, Filtro filtro) {
		StringBuilder query = new StringBuilder("select s from " + AtividadeColaborador.NAME
				+ " s where s.colaborador = " + colaborador.getColaboradorId());
		if (filtro.getFiltro1() != null && !filtro.getFiltro1().equals("")) {
			query.append(" and s.atividade.evento.nome like '%" + filtro.getFiltro1().toUpperCase().replaceAll("\'", "")
					+ "%' ");
		}
		if (filtro.getFiltro2() != null && !filtro.getFiltro2().equals("")) {
			query.append(
					" and s.atividade.nome like '%" + filtro.getFiltro2().toUpperCase().replaceAll("\'", "") + "%' ");
		}
		query.append(" and s.certificadoDisponivel=true order by s.atividade.evento.nome, s.atividade.nome");

		Query q = manager.createQuery(query.toString());
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Collection<AtividadeColaborador> findColaboradoresByAtividadeAndCertificadoDisponivelAndFiltro(
			Atividade atividade, boolean certificadoDisponivel, Filtro filtro) {
		StringBuilder query = new StringBuilder(
				"select s from " + AtividadeColaborador.NAME + " s where s.atividade = " + atividade.getAtividadeId());
		if (filtro.getFiltro1() != null && !filtro.getFiltro1().equals("")) {
			query.append(" and s.colaborador.pessoa.nome like '%"
					+ filtro.getFiltro1().toUpperCase().replaceAll("\'", "") + "%' ");
		}
		if (filtro.getFiltro2() != null && !filtro.getFiltro2().equals("")) {
			query.append(" and s.colaborador.pessoa.email like '%"
					+ filtro.getFiltro2().toUpperCase().replaceAll("\'", "") + "%' ");
		}
		query.append("and s.certificadoDisponivel = " + certificadoDisponivel + " order by s.colaborador.pessoa.nome");

		Query q = manager.createQuery(query.toString());
		return q.getResultList();
	}

}
