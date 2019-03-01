package br.gov.ma.tce.sophia.ejb.beans.atividade;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.gov.ma.tce.sophia.ejb.beans.evento.Evento;

@Stateless
public class AtividadeFacadeBean {

	@PersistenceContext(unitName = "sigesco_escex")
	public EntityManager manager;

	public Atividade include(Atividade atividade) {
		removeNaoNumeros(atividade);
		manager.persist(atividade);
		return atividade;
	}

	public Atividade update(Atividade atividade) {
		removeNaoNumeros(atividade);
		manager.merge(atividade);
		return atividade;
	}

	public void delete(int atividadeId) {
		Atividade atividade = findByPrimaryKey(atividadeId);
		manager.remove(atividade);
	}

	public Atividade findByPrimaryKey(Integer atividadeId) {
		Atividade atividade = manager.find(Atividade.class, atividadeId);
		atividade.getInscricoes().iterator();
		atividade.getInteressados().iterator();
		return atividade;
	}

	public Collection<Atividade> findAll() {
		Query q = manager.createQuery("select s from " + Atividade.NAME + " s  order by s.nome");
		Collection<Atividade> atividades = new ArrayList<Atividade>();
		for (Object o : q.getResultList()) {
			atividades.add((Atividade) o);
		}
		return atividades;
	}

	@SuppressWarnings("unchecked")
	public Collection<Atividade> filtrarByNome(String filtro) {
		Query q = manager.createQuery("select s from " + Atividade.NAME + " s where upper(s.nome) like '%"
				+ filtro.toUpperCase() + "%'  order by s.nome");
		return q.getResultList();
	}

	public Collection<Atividade> findAtividadesByEvento(Evento evento) {
		Query q = manager.createQuery("select s from " + Atividade.NAME + " s where s.evento=:arg0 order by s.dataInicio, s.nome")
				.setParameter("arg0", evento);
		Collection<Atividade> atividades = new ArrayList<Atividade>();
		for (Object o : q.getResultList()) {
			atividades.add((Atividade) o);
		}
		return atividades;
	}

	public Collection<Atividade> findAtividadesByCategoria(String categoria) {
		Query q = manager.createQuery("select s from " + Atividade.NAME + " s where s.categoria=:arg0 order by s.nome")
				.setParameter("arg0", categoria);
		Collection<Atividade> atividades = new ArrayList<Atividade>();
		for (Object o : q.getResultList()) {
			atividades.add((Atividade) o);
		}
		return atividades;
	}

	// Remove símbolos inseridos na mask do ZK
	private void removeNaoNumeros(Atividade atividade) {
		if (atividade.getCep() != null) {
			atividade.setCep(atividade.getCep().replaceAll("\\D+", ""));
		}
	}

}
