package br.gov.ma.tce.sophia.ejb.beans.impedimento;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ImpedimentoFacadeBean {
	
	@PersistenceContext(unitName = "sigesco_escex")
	public EntityManager manager;
	
	public Impedimento include(Impedimento impedimento) {
		manager.persist(impedimento);
		return impedimento;
	}

	public Impedimento update(Impedimento impedimento) {		
		manager.merge(impedimento);
		return impedimento;
	}

	public void delete(Integer impedimentoId) {
		Impedimento impedimento = findByPrimaryKey(impedimentoId);
		manager.remove(impedimento);
	}

	public Impedimento findByPrimaryKey(Integer impedimentoId) {
		return manager.find(Impedimento.class, impedimentoId);
	}
	
	public Collection<Impedimento> findAll() {
		Query q = manager.createQuery("select s from " + Impedimento.NAME + " s order by s.impedimentoId");
		Collection<Impedimento> impedimentos = new ArrayList<Impedimento>();
		for (Object o : q.getResultList()) {
			impedimentos.add((Impedimento) o);
		}
		return impedimentos;
	}

}
