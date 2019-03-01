package br.gov.ma.tce.sophia.ejb.beans.frequencia;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.gov.ma.tce.sophia.ejb.beans.atividade.Atividade;
import br.gov.ma.tce.sophia.ejb.beans.diasatividade.DiasAtividade;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscrito;
import br.gov.ma.tce.sophia.ejb.util.Filtro;

@Stateless
public class FrequenciaFacadeBean {

	@PersistenceContext(unitName = "sigesco_escex")
	public EntityManager manager;

	public Frequencia include(Frequencia frequencia) {
		manager.persist(frequencia);
		return frequencia;
	}

	public Frequencia update(Frequencia frequencia) {
		manager.merge(frequencia);
		return frequencia;
	}

	public void delete(int frequenciaId) {
		Frequencia frequencia = findByPrimaryKey(frequenciaId);
		manager.remove(frequencia);
	}

	public Frequencia findByPrimaryKey(Integer frequenciaId) {
		Frequencia frequencia = manager.find(Frequencia.class, frequenciaId);
		return frequencia;
	}

	public Collection<Frequencia> findAll() {
		Query q = manager.createQuery("select s from " + Frequencia.NAME + " s  order by s.frequenciaId");
		Collection<Frequencia> frequencias = new ArrayList<Frequencia>();
		for (Object o : q.getResultList()) {
			frequencias.add((Frequencia) o);
		}
		return frequencias;
	}

	public Collection<Frequencia> findFrequenciasUnicasByAtividadeAndDia(Atividade atividade, Date dia) {
		Query q = manager
				.createQuery("select s from " + Frequencia.NAME
						+ " s where s.participanteInscrito.atividade=:arg0 and s.diasAtividade.dia=:arg1 "
						+ "order by s.participanteInscrito.participante.nome")
				.setParameter("arg0", atividade).setParameter("arg1", dia);
		Collection<Frequencia> frequencias = new ArrayList<Frequencia>();
		for (Object o : q.getResultList()) {
			frequencias.add((Frequencia) o);
		}
		return frequencias;
	}

	public Long countFrequenciasUnicasByAtividadeAndDia(Atividade atividade, Date dia) {
		Query q = manager.createQuery("select count(s.presenca) from " + Frequencia.NAME
				+ " s where s.participanteInscrito.atividade=:arg0 and s.diasAtividade.dia=:arg1 and s.presenca=true")
				.setParameter("arg0", atividade).setParameter("arg1", dia);
		return (Long) q.getSingleResult();
	}

	public Collection<Frequencia> findFrequenciasByParticipanteInscrito(ParticipanteInscrito participanteInscrito) {
		Query q = manager
				.createQuery("select s from " + Frequencia.NAME
						+ " s where s.participanteInscrito=:arg0 order by s.frequenciaId")
				.setParameter("arg0", participanteInscrito);
		Collection<Frequencia> frequencias = new ArrayList<Frequencia>();
		for (Object o : q.getResultList()) {
			frequencias.add((Frequencia) o);
		}
		return frequencias;
	}

	public Collection<Frequencia> findFrequenciasValidasByParticipanteInscrito(
			ParticipanteInscrito participanteInscrito) {
		Query q = manager.createQuery("select s from " + Frequencia.NAME
				+ " s where s.participanteInscrito=:arg0 and s.diasAtividade.quantidadeHoras is not null order by s.frequenciaId")
				.setParameter("arg0", participanteInscrito);
		Collection<Frequencia> frequencias = new ArrayList<Frequencia>();
		for (Object o : q.getResultList()) {
			frequencias.add((Frequencia) o);
		}
		return frequencias;
	}

	public Collection<Frequencia> findFrequenciasByDiaAndAtividade(Date dia, Atividade atividade) {
		Query q = manager.createQuery("select s from " + Frequencia.NAME
				+ " s where s.diasAtividade.dia=:arg0 and s.participanteInscrito.atividade=:arg1 order by s.frequenciaId")
				.setParameter("arg0", dia).setParameter("arg1", atividade);
		Collection<Frequencia> frequencias = new ArrayList<Frequencia>();
		for (Object o : q.getResultList()) {
			frequencias.add((Frequencia) o);
		}
		return frequencias;
	}
	
	public Collection<Frequencia> findFrequenciasByDiaAndParticipanteInscrito(DiasAtividade dia, ParticipanteInscrito participanteInscrito) {
		Query q = manager.createQuery("select s from " + Frequencia.NAME
				+ " s where s.diasAtividade=:arg0 and s.participanteInscrito=:arg1 order by s.frequenciaId")
				.setParameter("arg0", dia).setParameter("arg1", participanteInscrito);
		Collection<Frequencia> frequencias = new ArrayList<Frequencia>();
		for (Object o : q.getResultList()) {
			frequencias.add((Frequencia) o);
		}
		return frequencias;
	}

	@SuppressWarnings("unchecked")
	public Collection<Frequencia> findFrequenciasByFiltroAndAtividadeAndDia(Filtro filtro, Atividade atividade,
			Date dia) {
		StringBuilder query = new StringBuilder(
				"select s from " + Frequencia.NAME + " s where s.frequenciaId is not null ");
		if (filtro.getFiltro1() != null && !filtro.getFiltro1().equals("")) {
			query.append(" and s.participanteInscrito.participante.nome like '%"
					+ filtro.getFiltro1().toUpperCase().replaceAll("\'", "") + "%' ");
		}
		if (filtro.getFiltro2() != null && !filtro.getFiltro2().equals("")) {
			query.append(" and s.participanteInscrito.participante.email like '%"
					+ filtro.getFiltro2().toLowerCase().replaceAll("\'", "") + "%' ");
		}
		query.append(" and s.participanteInscrito.atividade = " + atividade.getAtividadeId()
				+ " and s.diasAtividade.dia =:arg0 order by s.participanteInscrito.participante.nome");

		Query q = manager.createQuery(query.toString()).setParameter("arg0", dia);
		return q.getResultList();
	}
}
