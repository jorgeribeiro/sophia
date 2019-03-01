package br.gov.ma.tce.sophia.ejb.old;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class CertificadoAssinaturaFacadeBean {

	@PersistenceContext(unitName = "sigesco_escex")
	public EntityManager manager;

	public CertificadoAssinatura include(CertificadoAssinatura certificadoAssinatura) {
		manager.persist(certificadoAssinatura);
		return certificadoAssinatura;
	}

	public CertificadoAssinatura update(CertificadoAssinatura certificadoAssinatura) {
		manager.merge(certificadoAssinatura);
		return certificadoAssinatura;
	}

	public void delete(int certificadoAssinaturaId) {
		CertificadoAssinatura certificadoAssinatura = findByPrimaryKey(certificadoAssinaturaId);
		manager.remove(certificadoAssinatura);
	}

	public CertificadoAssinatura findByPrimaryKey(Integer certificadoAssinaturaId) {
		CertificadoAssinatura certificadoAssinatura = manager.find(CertificadoAssinatura.class,
				certificadoAssinaturaId);
		return certificadoAssinatura;
	}

	public Collection<CertificadoAssinatura> findAll() {
		Query q = manager.createQuery("select s from " + CertificadoAssinatura.NAME + " s order by s.atividade.nome");
		Collection<CertificadoAssinatura> certificadosAssinaturas = new ArrayList<CertificadoAssinatura>();
		for (Object o : q.getResultList()) {
			certificadosAssinaturas.add((CertificadoAssinatura) o);
		}
		return certificadosAssinaturas;
	}
}
