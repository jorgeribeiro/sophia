package br.gov.ma.tce.sophia.ejb.beans.tipoatividade;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class TipoAtividadeFacadeBean {

	@PersistenceContext(unitName = "sigesco_escex")
	public EntityManager manager;

	public TipoAtividade include(TipoAtividade tipoAtividade) {
		manager.persist(tipoAtividade);
		return tipoAtividade;
	}

	public TipoAtividade update(TipoAtividade tipoAtividade) {
		manager.merge(tipoAtividade);
		return tipoAtividade;
	}

	public void delete(int tipoAtividadeId) {
		TipoAtividade tipoAtividade = findByPrimaryKey(tipoAtividadeId);
		manager.remove(tipoAtividade);
	}

	public TipoAtividade findByPrimaryKey(Integer tipoAtividadeId) {
		TipoAtividade tipoAtividade = manager.find(TipoAtividade.class, tipoAtividadeId);
		return tipoAtividade;
	}

	public Collection<TipoAtividade> findAll() {
		Query q = manager.createQuery("select s from " + TipoAtividade.NAME + " s  order by s.nome");
		Collection<TipoAtividade> tipoDeAtividades = new ArrayList<TipoAtividade>();
		for (Object o : q.getResultList()) {
			tipoDeAtividades.add((TipoAtividade) o);
		}
		return tipoDeAtividades;
	}

}
