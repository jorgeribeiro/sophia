package br.gov.ma.tce.sophia.ejb.beans.fundocertificado;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class FundoCertificadoFacadeBean {

	@PersistenceContext(unitName = "sigesco_escex")
	public EntityManager manager;

	public FundoCertificado include(FundoCertificado fundoCertificado) {
		manager.persist(fundoCertificado);
		return fundoCertificado;
	}

	public FundoCertificado update(FundoCertificado fundoCertificado) {
		manager.merge(fundoCertificado);
		return fundoCertificado;
	}

	public void delete(int fundoCertificadoId) {
		FundoCertificado fundoCertificado = findByPrimaryKey(fundoCertificadoId);
		manager.remove(fundoCertificado);
	}

	public FundoCertificado findByPrimaryKey(Integer fundoCertificadoId) {
		FundoCertificado fundoCertificado = manager.find(FundoCertificado.class, fundoCertificadoId);
		return fundoCertificado;
	}

	public Collection<FundoCertificado> findAll() {
		Query q = manager.createQuery("select s from " + FundoCertificado.NAME + " s  order by s.fundoCertificadoId");
		Collection<FundoCertificado> fundosCertificado = new ArrayList<FundoCertificado>();
		for (Object o : q.getResultList()) {
			fundosCertificado.add((FundoCertificado) o);
		}
		return fundosCertificado;
	}
}
