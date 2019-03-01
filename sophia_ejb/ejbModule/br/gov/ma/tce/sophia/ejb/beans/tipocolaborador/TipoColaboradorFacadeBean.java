package br.gov.ma.tce.sophia.ejb.beans.tipocolaborador;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class TipoColaboradorFacadeBean {

	@PersistenceContext(unitName = "sigesco_escex")
	public EntityManager manager;

	public TipoColaborador include(TipoColaborador tipoColaborador) {
		manager.persist(tipoColaborador);
		return tipoColaborador;
	}

	public TipoColaborador update(TipoColaborador tipoColaborador) {
		manager.merge(tipoColaborador);
		return tipoColaborador;
	}

	public void delete(int tipoColaboradorId) {
		TipoColaborador tipoColaborador = findByPrimaryKey(tipoColaboradorId);
		manager.remove(tipoColaborador);
	}

	public TipoColaborador findByPrimaryKey(Integer tipoColaboradorId) {
		TipoColaborador tipoColaborador = manager.find(TipoColaborador.class, tipoColaboradorId);
		return tipoColaborador;
	}

	public Collection<TipoColaborador> findAll() {
		Query q = manager.createQuery("select s from " + TipoColaborador.NAME + " s  order by s.nome");
		Collection<TipoColaborador> tipoDeColaboradores = new ArrayList<TipoColaborador>();
		for (Object o : q.getResultList()) {
			tipoDeColaboradores.add((TipoColaborador) o);
		}
		return tipoDeColaboradores;
	}
	
}
