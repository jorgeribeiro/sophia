package br.gov.ma.tce.sophia.ejb.old;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.gov.ma.tce.sophia.ejb.util.Filtro;

@Stateless
public class ParticipanteFacadeBean {

	@PersistenceContext(unitName = "sigesco_escex")
	public EntityManager manager;

	public Participante include(Participante participante) {
		manager.persist(participante);
		return participante;
	}

	public Participante update(Participante participante) {
		manager.merge(participante);
		return participante;
	}

	public void delete(int participanteId) {
		Participante participante = findByPrimaryKey(participanteId);
		manager.remove(participante);
	}

	public Participante findByPrimaryKey(Integer participanteId) {
		Participante participante = manager.find(Participante.class, participanteId);
		return participante;
	}

	@SuppressWarnings("unchecked")
	public Collection<Participante> findAll() {
		Query q = manager.createQuery("select s from " + Participante.NAME + " s  order by s.pessoa.nome");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Collection<Participante> filtrarByNome(String filtro) {
		Query q = manager.createQuery("select s from " + Participante.NAME + " s where upper(s.pessoa.nome) like '%"
				+ filtro.toUpperCase() + "%'  order by s.pessoa.nome");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Collection<Participante> findParticipantesByCpf(String cpf) {
		Query q = manager
				.createQuery("select s from " + Participante.NAME + " s  where s.cpf=:arg0 order by s.pessoa.nome")
				.setParameter("arg0", cpf);
		return q.getResultList();
	}

	public Participante findByCpf(String cpf) {
		Query q = manager.createQuery("select s from " + Participante.NAME + " s where s.cpf=:arg0")
				.setParameter("arg0", cpf);
		return q.getResultList().isEmpty() ? null : (Participante) q.getResultList().get(0);
	}

	/**
	 * Método para evitar inclusão de CPFs repetidos
	 * 
	 * @param participanteId
	 * @param cpf
	 * @return
	 */
	public Participante findByPrimaryKeyAndCpf(Integer participanteId, String cpf) {
		Query q = manager
				.createQuery("select s from " + Participante.NAME + " s where s.participanteId<>:arg0 and s.cpf=:arg1")
				.setParameter("arg0", participanteId).setParameter("arg1", cpf);
		return q.getResultList().isEmpty() ? null : (Participante) q.getResultList().get(0);
	}

	public Participante findByEmail(String email) {
		Query q = manager.createQuery("select s from " + Participante.NAME + " s where s.pessoa.email=:arg0")
				.setParameter("arg0", email);
		return q.getResultList().isEmpty() ? null : (Participante) q.getResultList().get(0);
	}

	/**
	 * Método para enviar inclusão de emails repetidos
	 * 
	 * @param participanteId
	 * @param email
	 * @return
	 */
	public Participante findByPrimaryKeyAndEmail(Integer participanteId, String email) {
		Query q = manager
				.createQuery("select s from " + Participante.NAME
						+ " s where s.participanteId<>:arg0 and s.pessoa.email=:arg1")
				.setParameter("arg0", participanteId).setParameter("arg1", email);
		return q.getResultList().isEmpty() ? null : (Participante) q.getResultList().get(0);
	}

	@SuppressWarnings("unchecked")
	public Collection<Participante> findParticipantesByFiltro(Filtro filtroParticipante) {
		StringBuilder query = new StringBuilder(
				"select distinct s from " + Participante.NAME + " s where s.participanteId is not null ");
		if (filtroParticipante.getFiltro1() != null && !filtroParticipante.getFiltro1().equals("")) {
			query.append(" and s.pessoa.nome like '%"
					+ filtroParticipante.getFiltro1().toUpperCase().replaceAll("\'", "") + "%' ");
		}
		if (filtroParticipante.getFiltro2() != null && !filtroParticipante.getFiltro2().equals("")) {
			query.append(" and s.pessoa.email like '%" + filtroParticipante.getFiltro2().replaceAll("\'", "") + "%' ");
		}
		query.append(" order by s.pessoa.nome");

		Query q = manager.createQuery(query.toString());
		return q.getResultList();
	}

}
