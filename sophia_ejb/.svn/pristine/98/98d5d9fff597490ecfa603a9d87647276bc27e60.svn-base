package br.gov.ma.tce.sophia.ejb.old;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class AssinaturaFacadeBean {

	@PersistenceContext(unitName = "sigesco_escex")
	public EntityManager manager;

	public Assinatura include(Assinatura assinatura) {
		manager.persist(assinatura);
		return assinatura;
	}

	public Assinatura update(Assinatura assinatura) {
		manager.merge(assinatura);
		return assinatura;
	}

	public void delete(int assinaturaId) {
		Assinatura assinatura = findByPrimaryKey(assinaturaId);
		manager.remove(assinatura);
	}

	public Assinatura findByPrimaryKey(Integer assinaturaId) {
		Assinatura assinatura = manager.find(Assinatura.class, assinaturaId);
		return assinatura;
	}

	public Collection<Assinatura> findAll() {
		Query q = manager.createQuery("select s from " + Assinatura.NAME + " s  order by s.nome");
		Collection<Assinatura> assinaturas = new ArrayList<Assinatura>();
		for (Object o : q.getResultList()) {
			assinaturas.add((Assinatura) o);
		}
		return assinaturas;
	}
}
