package br.gov.ma.tce.sophia.ejb.beans.participanteinscrito;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.gov.ma.tce.sophia.ejb.beans.atividade.Atividade;
import br.gov.ma.tce.sophia.ejb.beans.evento.Evento;
import br.gov.ma.tce.sophia.ejb.beans.pessoa.Pessoa;
import br.gov.ma.tce.sophia.ejb.util.Filtro;

@Stateless
public class ParticipanteInscritoFacadeBean {

	@PersistenceContext(unitName = "sigesco_escex")
	public EntityManager manager;

	public ParticipanteInscrito include(ParticipanteInscrito participanteInscrito) {
		manager.persist(participanteInscrito);
		return participanteInscrito;
	}

	public ParticipanteInscrito update(ParticipanteInscrito participanteInscrito) {
		manager.merge(participanteInscrito);
		return participanteInscrito;
	}

	public void delete(int participanteInscritoId) {
		ParticipanteInscrito participanteInscrito = findByPrimaryKey(participanteInscritoId);
		manager.remove(participanteInscrito);
	}

	public ParticipanteInscrito findByPrimaryKey(Integer participanteInscritoId) {
		ParticipanteInscrito participanteInscrito = manager.find(ParticipanteInscrito.class, participanteInscritoId);
		participanteInscrito.getFrequencias().iterator();
		return participanteInscrito;
	}

	public Collection<ParticipanteInscrito> findAll() {
		Query q = manager
				.createQuery("select s from " + ParticipanteInscrito.NAME + " s order by s.participanteInscritoId");
		Collection<ParticipanteInscrito> participantesInscritos = new ArrayList<ParticipanteInscrito>();
		for (Object o : q.getResultList()) {
			participantesInscritos.add((ParticipanteInscrito) o);
		}
		return participantesInscritos;
	}

	public Collection<ParticipanteInscrito> findInscricoesByParticipanteAndInscricaoAprovada(Pessoa participante) {
		Query q = manager.createQuery("select s from " + ParticipanteInscrito.NAME
				+ " s where s.participante=:arg0 and s.inscricaoAprovada is not null "
				+ " order by s.atividade.evento.nome, s.atividade.nome").setParameter("arg0", participante);
		Collection<ParticipanteInscrito> participantesInscritos = new ArrayList<ParticipanteInscrito>();
		for (Object o : q.getResultList()) {
			participantesInscritos.add((ParticipanteInscrito) o);
		}
		return participantesInscritos;
	}

	public Collection<ParticipanteInscrito> findInscricoesByParticipanteAndCertificadoDisponivel(Pessoa participante) {
		Query q = manager.createQuery("select s from " + ParticipanteInscrito.NAME
				+ " s where s.participante=:arg0 and s.certificadoDisponivel=true "
				+ " order by s.atividade.evento.nome, s.atividade.nome").setParameter("arg0", participante);
		Collection<ParticipanteInscrito> participantesInscritos = new ArrayList<ParticipanteInscrito>();
		for (Object o : q.getResultList()) {
			participantesInscritos.add((ParticipanteInscrito) o);
		}
		return participantesInscritos;
	}

	public Collection<ParticipanteInscrito> findParticipantesByAtividadeAndInscricaoAprovada(Atividade atividade,
			boolean inscricaoAprovada) {
		Query q = manager
				.createQuery("select s from " + ParticipanteInscrito.NAME
						+ " s where s.atividade=:arg0 and s.inscricaoAprovada=:arg1 order by s.participante.nome")
				.setParameter("arg0", atividade).setParameter("arg1", inscricaoAprovada);
		Collection<ParticipanteInscrito> participantesInscritos = new ArrayList<ParticipanteInscrito>();
		for (Object o : q.getResultList()) {
			participantesInscritos.add((ParticipanteInscrito) o);
		}
		return participantesInscritos;
	}

	public Collection<ParticipanteInscrito> findParticipantesByAtividadeAndInscricaoAprovadaAndCertificadoDisponivel(
			Atividade atividade, boolean inscricaoAprovada, boolean certificadoDisponivel) {
		Query q = manager.createQuery("select s from " + ParticipanteInscrito.NAME
				+ " s where s.atividade=:arg0 and s.inscricaoAprovada=:arg1 and s.certificadoDisponivel=:arg2 order by s.participante.nome")
				.setParameter("arg0", atividade).setParameter("arg1", inscricaoAprovada)
				.setParameter("arg2", certificadoDisponivel);
		Collection<ParticipanteInscrito> participantesInscritos = new ArrayList<ParticipanteInscrito>();
		for (Object o : q.getResultList()) {
			participantesInscritos.add((ParticipanteInscrito) o);
		}
		return participantesInscritos;
	}

	public Collection<ParticipanteInscrito> findParticipantesByAtividade(Atividade atividade) {
		Query q = manager
				.createQuery("select s from " + ParticipanteInscrito.NAME
						+ " s where s.atividade=:arg0 order by s.dtPreInscricao, s.participante.nome")
				.setParameter("arg0", atividade);
		Collection<ParticipanteInscrito> participantesInscritos = new ArrayList<ParticipanteInscrito>();
		for (Object o : q.getResultList()) {
			participantesInscritos.add((ParticipanteInscrito) o);
		}
		return participantesInscritos;
	}

	public Collection<ParticipanteInscrito> findParticipantesByEvento(Evento evento) {
		Query q = manager
				.createQuery("select s from " + ParticipanteInscrito.NAME
						+ " s where s.atividade.evento=:arg0 order by s.participante.nome")
				.setParameter("arg0", evento);
		Collection<ParticipanteInscrito> participantesInscritos = new ArrayList<ParticipanteInscrito>();
		for (Object o : q.getResultList()) {
			participantesInscritos.add((ParticipanteInscrito) o);
		}
		return participantesInscritos;
	}
	
	public Collection<ParticipanteInscrito> findParticipantesByEventoAndFiltro(Evento evento, Filtro filtroPessoa) {		
		StringBuilder query = new StringBuilder(
				"select s from " + ParticipanteInscrito.NAME
				+ " s where s.atividade.evento = "+ evento.getEventoId());
	
		if (filtroPessoa.getFiltro1() != null && !filtroPessoa.getFiltro1().equals("")) {
			query.append(" and s.participante.nome like '%" + filtroPessoa.getFiltro1().toUpperCase().replaceAll("\'", "") + "%' ");
		}
		if (filtroPessoa.getFiltro2() != null && !filtroPessoa.getFiltro2().equals("")) {
			query.append(" and s.participante.email like '%" + filtroPessoa.getFiltro2().toLowerCase().replaceAll("\'", "") + "%' ");
		}
		
		query.append(" order by s.participante.nome");
		Query q = manager.createQuery(query.toString());
		Collection<ParticipanteInscrito> participantesInscritos = new ArrayList<ParticipanteInscrito>();
		for (Object o : q.getResultList()) {
			participantesInscritos.add((ParticipanteInscrito) o);
		}
		return participantesInscritos;
	}

	public Collection<ParticipanteInscrito> findParticipantesSemAlteracaoByAtividade(Atividade atividade) {
		Query q = manager.createQuery("select s from " + ParticipanteInscrito.NAME
				+ " s where s.atividade=:arg0 and s.inscricaoAprovada is null "
				+ " order by s.dtPreInscricao, s.participante.nome").setParameter("arg0", atividade);
		Collection<ParticipanteInscrito> participantesInscritos = new ArrayList<ParticipanteInscrito>();
		for (Object o : q.getResultList()) {
			participantesInscritos.add((ParticipanteInscrito) o);
		}
		return participantesInscritos;
	}

	public Collection<ParticipanteInscrito> findPreInscricoesByParticipante(Pessoa participante) {
		Query q = manager.createQuery("select s from " + ParticipanteInscrito.NAME
				+ " s where s.participante=:arg0 and s.inscricaoAprovada is null "
				+ " order by s.atividade.evento.nome, s.atividade.nome").setParameter("arg0", participante);
		Collection<ParticipanteInscrito> participantesInscritos = new ArrayList<ParticipanteInscrito>();
		for (Object o : q.getResultList()) {
			participantesInscritos.add((ParticipanteInscrito) o);
		}
		return participantesInscritos;
	}

	public ParticipanteInscrito findByParticipanteAndAtividade(Pessoa participante, Atividade atividade) {
		Query q = manager
				.createQuery("select s from " + ParticipanteInscrito.NAME
						+ " s where s.participante=:arg0 and s.atividade=:arg1 order by s.participante.nome")
				.setParameter("arg0", participante).setParameter("arg1", atividade);
		return q.getResultList().isEmpty() ? null : (ParticipanteInscrito) q.getResultList().get(0);
	}

	public Collection<ParticipanteInscrito> findParticipantesInscritosByNomeAndAtividade(String nome,
			Atividade atividade) {
		Query q = manager
				.createQuery("select s from " + ParticipanteInscrito.NAME + " s, " + Pessoa.NAME
						+ " p where s.participante.pessoaId = p.pessoaId and s.atividade=:arg0 and p.nome"
						+ " like :arg1 order by s.dtPreInscricao, s.participante.nome")
				.setParameter("arg0", atividade)
				.setParameter("arg1", "%" + nome.toUpperCase().replaceAll("\'", "") + "%");
		Collection<ParticipanteInscrito> participantesInscritos = new ArrayList<ParticipanteInscrito>();
		for (Object o : q.getResultList()) {
			participantesInscritos.add((ParticipanteInscrito) o);
		}
		return participantesInscritos;
	}

	public Collection<ParticipanteInscrito> findParticipantesInscritosByPeriodo(String mes, String ano) {
		Calendar inicio = new GregorianCalendar(Integer.parseInt(ano), Integer.parseInt(mes) - 1, 1);
		Calendar fim = new GregorianCalendar(Integer.parseInt(ano), Integer.parseInt(mes) - 1,
				inicio.getActualMaximum(Calendar.DAY_OF_MONTH));
		Query q = manager.createQuery("select distinct s from " + ParticipanteInscrito.NAME + " s, " + Pessoa.NAME
				+ " p where s.atividade.dataInicio>=:arg0 and s.atividade.dataInicio<=:arg1 "
				+ "and s.certificadoDisponivel=true "
				+ "and s.participante.servidorMatricula is not null")
				.setParameter("arg0", inicio.getTime())
				.setParameter("arg1", fim.getTime());
		Collection<ParticipanteInscrito> participantesInscritos = new ArrayList<ParticipanteInscrito>();
		for (Object o : q.getResultList()) {
			participantesInscritos.add((ParticipanteInscrito) o);
		}
		return participantesInscritos;
	}

	@SuppressWarnings("unchecked")
	public Collection<ParticipanteInscrito> findInscricoesByParticipanteAndInscricaoAprovadaAndFiltro(
			Pessoa participante, Filtro filtro) {
		StringBuilder query = new StringBuilder("select s from " + ParticipanteInscrito.NAME
				+ " s where s.participante = " + participante.getPessoaId());
		if (filtro.getFiltro1() != null && !filtro.getFiltro1().equals("")) {
			query.append(" and s.atividade.evento.nome like '%" + filtro.getFiltro1().toUpperCase().replaceAll("\'", "")
					+ "%' ");
		}
		if (filtro.getFiltro2() != null && !filtro.getFiltro2().equals("")) {
			query.append(
					" and s.atividade.nome like '%" + filtro.getFiltro2().toUpperCase().replaceAll("\'", "") + "%' ");
		}
		query.append(" and s.inscricaoAprovada is not null order by s.atividade.evento.nome, s.atividade.nome");
		Query q = manager.createQuery(query.toString());
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Collection<ParticipanteInscrito> findInscricoesByParticipanteAndCertificadoDisponivelAndFiltro(
			Pessoa participante, Filtro filtro) {
		StringBuilder query = new StringBuilder("select s from " + ParticipanteInscrito.NAME
				+ " s where s.participante = " + participante.getPessoaId());
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
	public Collection<ParticipanteInscrito> findInscricoesByAtividadeAndInscricaoAprovadaAndCertificadoDisponivelAndFiltro(
			Atividade atividade, boolean certificadoDisponivel, Filtro filtro) {
		StringBuilder query = new StringBuilder(
				"select s from " + ParticipanteInscrito.NAME + " s where s.atividade = " + atividade.getAtividadeId());
		if (filtro.getFiltro1() != null && !filtro.getFiltro1().equals("")) {
			query.append(" and s.participante.nome like '%" + filtro.getFiltro1().toUpperCase().replaceAll("\'", "")
					+ "%' ");
		}
		if (filtro.getFiltro2() != null && !filtro.getFiltro2().equals("")) {
			query.append(" and s.participante.email like '%" + filtro.getFiltro2().toLowerCase().replaceAll("\'", "")
					+ "%' ");
		}
		query.append(" and s.inscricaoAprovada=true and s.certificadoDisponivel=" + certificadoDisponivel
				+ " order by s.frequencia, s.participante.nome");
		Query q = manager.createQuery(query.toString());
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Collection<ParticipanteInscrito> findPreInscricoesByParticipanteAndFiltro(Pessoa participante,
			Filtro filtro) {
		StringBuilder query = new StringBuilder("select s from " + ParticipanteInscrito.NAME
				+ " s where s.participante = " + participante.getPessoaId());
		if (filtro.getFiltro1() != null && !filtro.getFiltro1().equals("")) {
			query.append(" and s.atividade.evento.nome like '%" + filtro.getFiltro1().toUpperCase().replaceAll("\'", "")
					+ "%' ");
		}
		if (filtro.getFiltro2() != null && !filtro.getFiltro2().equals("")) {
			query.append(
					" and s.atividade.nome like '%" + filtro.getFiltro2().toUpperCase().replaceAll("\'", "") + "%' ");
		}
		query.append(" and s.inscricaoAprovada is null " + "order by s.atividade.evento.nome, s.atividade.nome");

		Query q = manager.createQuery(query.toString());
		return q.getResultList();
	}

	public Long countInscricoesByAtividade(Atividade atividade) {
		Query q = manager.createQuery("select count(s.inscricaoAprovada) from " + ParticipanteInscrito.NAME
				+ " s where s.atividade=:arg0 and s.inscricaoAprovada=true").setParameter("arg0", atividade);
		return (Long) q.getSingleResult();
	}

	public Long countInscricoesByAtividadeAndTipoParticipante(Atividade atividade, String tipoPessoa) {
		Query q = manager
				.createQuery("select count(s.inscricaoAprovada) from " + ParticipanteInscrito.NAME
						+ " s where s.atividade=:arg0 and s.participante.tipoPessoa=:arg1 and s.inscricaoAprovada=true")
				.setParameter("arg0", atividade).setParameter("arg1", tipoPessoa);
		return (Long) q.getSingleResult();
	}
}
