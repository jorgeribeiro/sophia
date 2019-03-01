package br.gov.ma.tce.sophia.ejb.beans.modelocertificado;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.gov.ma.tce.sophia.ejb.beans.atividade.Atividade;

@Stateless
public class ModeloCertificadoFacadeBean {

	@PersistenceContext(unitName = "sigesco_escex")
	public EntityManager manager;

	public ModeloCertificado include(ModeloCertificado modeloCertificado) {
		manager.persist(modeloCertificado);
		return modeloCertificado;
	}

	public ModeloCertificado update(ModeloCertificado modeloCertificado) {
		manager.merge(modeloCertificado);
		return modeloCertificado;
	}

	public void delete(int modeloCertificadoId) {
		ModeloCertificado modeloCertificado = findByPrimaryKey(modeloCertificadoId);
		manager.remove(modeloCertificado);
	}

	public ModeloCertificado findByPrimaryKey(Integer modeloCertificadoId) {
		ModeloCertificado modeloCertificado = manager.find(ModeloCertificado.class, modeloCertificadoId);
		return modeloCertificado;
	}

	public Collection<ModeloCertificado> findAll() {
		Query q = manager.createQuery("select s from " + ModeloCertificado.NAME + " s  order by s.modeloCertificadoId");
		Collection<ModeloCertificado> modeloCertificados = new ArrayList<ModeloCertificado>();
		for (Object o : q.getResultList()) {
			modeloCertificados.add((ModeloCertificado) o);
		}
		return modeloCertificados;
	}

	public ModeloCertificado findModeloCertificadoByAtividadeAndTipo(Atividade atividade, String tipo) {
		Query q = manager
				.createQuery("select s from " + ModeloCertificado.NAME + " s where s.atividade=:arg0 and s.tipo=:arg1")
				.setParameter("arg0", atividade).setParameter("arg1", tipo);
		return q.getResultList().isEmpty() ? null : (ModeloCertificado) q.getResultList().get(0);
	}

	public ModeloCertificado findModeloCertificadoByAtividade(Atividade atividade) {
		Query q = manager.createQuery("select s from " + ModeloCertificado.NAME + " s where s.atividade=:arg0")
				.setParameter("arg0", atividade);
		return q.getResultList().isEmpty() ? null : (ModeloCertificado) q.getResultList().get(0);
	}
}
