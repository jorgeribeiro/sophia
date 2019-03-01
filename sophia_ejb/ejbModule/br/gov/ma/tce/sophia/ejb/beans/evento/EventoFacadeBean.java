package br.gov.ma.tce.sophia.ejb.beans.evento;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class EventoFacadeBean {

	@PersistenceContext(unitName = "sigesco_escex")
	public EntityManager manager;

	public Evento include(Evento evento) {
		removeNaoNumeros(evento);
		manager.persist(evento);
		return evento;
	}

	public Evento update(Evento evento) {
		removeNaoNumeros(evento);
		manager.merge(evento);
		return evento;
	}

	public void delete(Integer eventoId) {
		Evento evento = findByPrimaryKey(eventoId);
		manager.remove(evento);
	}

	public Evento findByPrimaryKey(Integer eventoId) {
		Evento evento = manager.find(Evento.class, eventoId);
		if (evento != null)
			evento.getAtividades().iterator();
		return evento;
	}

	public Collection<Evento> findAll() {
		Query q = manager.createQuery("select s from " + Evento.NAME + " s order by s.nome");
		Collection<Evento> eventos = new ArrayList<Evento>();
		for (Object o : q.getResultList()) {
			eventos.add((Evento) o);
		}
		return eventos;
	}

	public Collection<Evento> findEventosByCategoria(String categoria) {
		Query q = manager
				.createQuery(
						"select s from " + Evento.NAME + " s where s.categoria=:arg0 order by s.dataInicio, s.nome")
				.setParameter("arg0", categoria);
		Collection<Evento> eventos = new ArrayList<Evento>();
		for (Object o : q.getResultList()) {
			eventos.add((Evento) o);
		}
		return eventos;
	}

	@SuppressWarnings("unchecked")
	public Collection<Evento> filtrarByNome(String filtroNome) {
		Query q = manager.createQuery("select s from " + Evento.NAME + " s where upper(s.nome) like '%"
				+ filtroNome.toUpperCase() + "%' order by s.nome");
		return q.getResultList();
	}

	public Collection<Evento> findEventosRealizados() {
		Query q = manager.createQuery(
				"select s from " + Evento.NAME + " s where s.categoria like 'REALIZADO' order by s.dataInicio desc, s.nome");
		Collection<Evento> eventos = new ArrayList<Evento>();
		for (Object o : q.getResultList()) {
			eventos.add((Evento) o);
		}
		return eventos;
	}

	@SuppressWarnings("unchecked")
	public Collection<Evento> filtrarEventosRealizadosByNome(String filtroNome) {
		Query q = manager.createQuery("select s from " + Evento.NAME + " s where upper(s.nome) like '%"
				+ filtroNome.toUpperCase() + "%' and s.categoria like 'REALIZADO' order by s.dataInicio desc, s.nome");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Collection<Evento> filtrarEventosRealizadosByData(Date dataInicio, Date dataFim) {
		Query q = manager.createQuery("select s from " + Evento.NAME
				+ " s where s.dataInicio >=:arg0 and s.dataFim <= :arg1 and s.categoria like 'REALIZADO' order by s.dataInicio desc, s.nome")
				.setParameter("arg0", dataInicio).setParameter("arg1", dataFim);

		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Collection<Evento> filtrarEventosRealizadosByNomeAndData(String filtroNome, Date dataInicio, Date dataFim) {
		Query q = manager.createQuery("select s from " + Evento.NAME + " s where upper(s.nome) like '%"
				+ filtroNome.toUpperCase()
				+ "%' and s.dataInicio >=:arg0 and s.dataFim <= :arg1 and s.categoria like 'REALIZADO' order by s.dataInicio desc, s.nome")
				.setParameter("arg0", dataInicio).setParameter("arg1", dataFim);

		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Collection<Evento> filtrarEventosRealizadosByDataInicio(Date dataInicio, String filtroNome) {
		
		StringBuilder query = new StringBuilder("select s from " + Evento.NAME + " s where ");
		if (!filtroNome.isEmpty()) {
			query.append("upper(s.nome) like '%" + filtroNome.toUpperCase() + "%' and ");
		}
		query.append("s.dataInicio >=:arg0 and s.categoria like 'REALIZADO' order by s.dataInicio desc, s.nome");
		Query q = manager.createQuery(query.toString()).setParameter("arg0", dataInicio);

		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Collection<Evento> filtrarEventosRealizadosByDataFim(Date dataFim, String filtroNome) {
		
		StringBuilder query = new StringBuilder("select s from " + Evento.NAME + " s where ");
		if (!filtroNome.isEmpty()) {
			query.append("upper(s.nome) like '%" + filtroNome.toUpperCase() + "%' and ");
		}
		query.append("s.dataFim <=:arg0 and s.categoria like 'REALIZADO' order by s.dataInicio desc, s.nome");
		Query q = manager.createQuery(query.toString()).setParameter("arg0", dataFim);

		return q.getResultList();

	}

	/*
	 * @SuppressWarnings("unchecked") public Collection<Evento>
	 * filtrarEventosRealizadosByNomeAndData(String filtroNome, String data) { Query
	 * q = manager.createQuery("select s from " + Evento.NAME +
	 * " s where upper(s.nome) like '%" + filtroNome.toUpperCase() +
	 * "%' and s.categoria like 'REALIZADO' order by s.nome"); return
	 * q.getResultList(); }
	 */

	// Remove símbolos inseridos na mask do ZK
	private void removeNaoNumeros(Evento evento) {
		evento.setCep(evento.getCep().replaceAll("\\D+", ""));
	}

}