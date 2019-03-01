package br.gov.ma.tce.sophia.ejb.beans.participanteinteresse;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.gov.ma.tce.sophia.ejb.beans.atividade.Atividade;
import br.gov.ma.tce.sophia.ejb.beans.evento.Evento;
import br.gov.ma.tce.sophia.ejb.beans.pessoa.Pessoa;
import br.gov.ma.tce.sophia.ejb.util.Filtro;

@Stateless
public class ParticipanteInteresseFacadeBean {

	@PersistenceContext(unitName = "sigesco_escex")
	public EntityManager manager;

	public ParticipanteInteresse include(ParticipanteInteresse participanteInteresse) {
		manager.persist(participanteInteresse);
		return participanteInteresse;
	}

	public ParticipanteInteresse update(ParticipanteInteresse participanteInteresse) {
		manager.merge(participanteInteresse);
		return participanteInteresse;
	}

	public void delete(int participanteInteresseId) {
		ParticipanteInteresse participanteInteresse = findByPrimaryKey(participanteInteresseId);
		manager.remove(participanteInteresse);
	}

	public ParticipanteInteresse findByPrimaryKey(Integer participanteInteresseId) {
		ParticipanteInteresse participanteInteresse = manager.find(ParticipanteInteresse.class,
				participanteInteresseId);
		return participanteInteresse;
	}

	public Collection<ParticipanteInteresse> findAll() {
		Query q = manager
				.createQuery("select s from " + ParticipanteInteresse.NAME + " s order by s.participanteInteresseId");
		Collection<ParticipanteInteresse> participantesInteressados = new ArrayList<ParticipanteInteresse>();
		for (Object o : q.getResultList()) {
			participantesInteressados.add((ParticipanteInteresse) o);
		}
		return participantesInteressados;
	}

	public ParticipanteInteresse findByParticipanteAndAtividade(Pessoa participante, Atividade atividade) {
		Query q = manager
				.createQuery("select s from " + ParticipanteInteresse.NAME
						+ " s where s.participante=:arg0 and s.atividade=:arg1 order by s.participante.nome")
				.setParameter("arg0", participante).setParameter("arg1", atividade);
		return q.getResultList().isEmpty() ? null : (ParticipanteInteresse) q.getResultList().get(0);
	}

	public Collection<ParticipanteInteresse> findListaDeInteresseByParticipante(Pessoa participante) {
		Query q = manager.createQuery("select s from " + ParticipanteInteresse.NAME
				+ " s where s.participante=:arg0 and s.inscritoNaAtividade=false order by s.atividade.evento.nome, s.atividade.nome")
				.setParameter("arg0", participante);
		Collection<ParticipanteInteresse> participantesInteressados = new ArrayList<ParticipanteInteresse>();
		for (Object o : q.getResultList()) {
			participantesInteressados.add((ParticipanteInteresse) o);
		}
		return participantesInteressados;
	}

	public Collection<ParticipanteInteresse> findListaDeInteresseByAtividade(Atividade atividade) {
		Query q = manager
				.createQuery("select s from " + ParticipanteInteresse.NAME
						+ " s where s.atividade=:arg0 order by s.dtInteresse, s.participante.nome")
				.setParameter("arg0", atividade);
		Collection<ParticipanteInteresse> participantesInteressados = new ArrayList<ParticipanteInteresse>();
		for (Object o : q.getResultList()) {
			participantesInteressados.add((ParticipanteInteresse) o);
		}
		return participantesInteressados;
	}

	public Collection<ParticipanteInteresse> findListaDeInteresseByEvento(Evento evento) {
		Query q = manager
				.createQuery("select s from " + ParticipanteInteresse.NAME
						+ " s where s.atividade.evento=:arg0 order by s.participante.nome")
				.setParameter("arg0", evento);
		Collection<ParticipanteInteresse> participantesInteressados = new ArrayList<ParticipanteInteresse>();
		for (Object o : q.getResultList()) {
			participantesInteressados.add((ParticipanteInteresse) o);
		}
		return participantesInteressados;
	}
	
	public Collection<ParticipanteInteresse> findListaDeInteresseByEventoAndFiltro(Evento evento, Filtro filtroPessoa) {		
		StringBuilder query = new StringBuilder(
				"select s from " + ParticipanteInteresse.NAME
				+ " s where s.atividade.evento= "+evento.getEventoId());
		
		if (filtroPessoa.getFiltro1() != null && !filtroPessoa.getFiltro1().equals("")) {
			query.append(" and s.participante.nome like '%" + filtroPessoa.getFiltro1().toUpperCase().replaceAll("\'", "") + "%' ");
		}
		if (filtroPessoa.getFiltro2() != null && !filtroPessoa.getFiltro2().equals("")) {
			query.append(" and s.participante.email like '%" + filtroPessoa.getFiltro2().toLowerCase().replaceAll("\'", "") + "%' ");
		}
		
		query.append(" order by s.participante.nome");
		Query q = manager.createQuery(query.toString());
		Collection<ParticipanteInteresse> participantesInteressados = new ArrayList<ParticipanteInteresse>();
		for (Object o : q.getResultList()) {
			participantesInteressados.add((ParticipanteInteresse) o);
		}
		return participantesInteressados;
	}

	public Collection<ParticipanteInteresse> findListaDeInteresseByNomeAndAtividade(String nome, Atividade atividade) {
		Query q = manager
				.createQuery("select s from " + ParticipanteInteresse.NAME + " s, " + Pessoa.NAME
						+ " p where s.participante.pessoaId = p.pessoaId and s.atividade=:arg0 and p.nome"
						+ " like :arg1 order by s.dtInteresse, s.participante.nome")
				.setParameter("arg0", atividade)
				.setParameter("arg1", "%" + nome.toUpperCase().replaceAll("\'", "") + "%");
		Collection<ParticipanteInteresse> participantesInteressados = new ArrayList<ParticipanteInteresse>();
		for (Object o : q.getResultList()) {
			participantesInteressados.add((ParticipanteInteresse) o);
		}
		return participantesInteressados;
	}

	@SuppressWarnings("unchecked")
	public Collection<ParticipanteInteresse> findListaDeInteresseByParticipanteAndFiltro(Pessoa participante,
			Filtro filtro) {
		StringBuilder query = new StringBuilder("select s from " + ParticipanteInteresse.NAME
				+ " s where s.participante = " + participante.getPessoaId());
		if (filtro.getFiltro1() != null && !filtro.getFiltro1().equals("")) {
			query.append(" and s.atividade.evento.nome like '%" + filtro.getFiltro1().toUpperCase().replaceAll("\'", "")
					+ "%' ");
		}
		if (filtro.getFiltro2() != null && !filtro.getFiltro2().equals("")) {
			query.append(
					" and s.atividade.nome like '%" + filtro.getFiltro2().toUpperCase().replaceAll("\'", "") + "%' ");
		}
		query.append(" and s.inscritoNaAtividade=false order by s.atividade.evento.nome, s.atividade.nome");

		Query q = manager.createQuery(query.toString());
		return q.getResultList();
	}
}
