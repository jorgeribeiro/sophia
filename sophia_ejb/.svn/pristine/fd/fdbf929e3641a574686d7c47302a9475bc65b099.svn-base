package br.gov.ma.tce.sophia.ejb.beans.formacaoacademica;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Stateless
public class FormacaoAcademicaFacadeBean {

	@PersistenceContext(unitName = "sigesco_escex")
	public EntityManager manager;

	public FormacaoAcademica include(FormacaoAcademica formacaoAcademica) {
		manager.persist(formacaoAcademica);
		return formacaoAcademica;
	}

	public FormacaoAcademica update(FormacaoAcademica formacaoAcademica) {
		manager.merge(formacaoAcademica);
		return formacaoAcademica;
	}

	public void delete(Integer formacaoAcademicaId) {
		FormacaoAcademica formacaoAcademica = findByPrimaryKey(formacaoAcademicaId);
		manager.remove(formacaoAcademica);
	}

	public FormacaoAcademica findByPrimaryKey(Integer formacaoAcademicaId) {
		return manager.find(FormacaoAcademica.class, formacaoAcademicaId);
	}

	public Collection<FormacaoAcademica> findAll() {
		Query q = manager.createQuery("select s from " + FormacaoAcademica.NAME + " s order by s.nome");
		Collection<FormacaoAcademica> formacoesAcademicas = new ArrayList<FormacaoAcademica>();
		for (Object o : q.getResultList()) {
			formacoesAcademicas.add((FormacaoAcademica) o);
		}
		return formacoesAcademicas;
	}

	public Collection<FormacaoAcademica> findByFormacao(int tipoFormacaoAcademica) {
		
		TypedQuery<FormacaoAcademica> q = manager.createQuery("select f from " + FormacaoAcademica.NAME + " f where f.tipoFormacaoAcademica = "
				+ tipoFormacaoAcademica,FormacaoAcademica.class);
		
		Collection<FormacaoAcademica> formacoesAcademicas = new ArrayList<FormacaoAcademica>();
		
		for (FormacaoAcademica o : q.getResultList()) {
			formacoesAcademicas.add(o);
		}
		
		return formacoesAcademicas;
	}
	
}
