package br.gov.ma.tce.sophia.ejb.beans.diasatividade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.gov.ma.tce.sophia.ejb.beans.atividade.Atividade;

@Stateless
public class DiasAtividadeFacadeBean {

	@PersistenceContext(unitName = "sigesco_escex")
	public EntityManager manager;

	public DiasAtividade include(DiasAtividade diasAtividade) {
		manager.persist(diasAtividade);
		return diasAtividade;
	}

	public DiasAtividade update(DiasAtividade diasAtividade) {
		manager.merge(diasAtividade);
		return diasAtividade;
	}

	public void delete(int diasAtividadeId) {
		DiasAtividade diasAtividade = findByPrimaryKey(diasAtividadeId);
		manager.remove(diasAtividade);
	}

	public DiasAtividade findByPrimaryKey(Integer diasAtividadeId) {
		DiasAtividade diasAtividade = manager.find(DiasAtividade.class, diasAtividadeId);
		return diasAtividade;
	}

	public DiasAtividade findDiasByDiaAndAtividade(Date dia, Atividade atividade) {
		Query q = manager
				.createQuery("select s from " + DiasAtividade.NAME
						+ " s where s.dia=:arg0 and s.atividade=:arg1 and s.quantidadeHoras is not null")
				.setParameter("arg0", dia).setParameter("arg1", atividade);
		return q.getResultList().isEmpty() ? null : (DiasAtividade) q.getResultList().get(0);
	}

	// Método utilizado para encontrar qualquer registro
	// não importando se quantidade_horas é null ou não
	public DiasAtividade findAnyDiasByDiaAndAtividade(Date dia, Atividade atividade) {
		Query q = manager
				.createQuery("select s from " + DiasAtividade.NAME + " s where s.dia=:arg0 and s.atividade=:arg1")
				.setParameter("arg0", dia).setParameter("arg1", atividade);
		return q.getResultList().isEmpty() ? null : (DiasAtividade) q.getResultList().get(0);
	}

	public Collection<DiasAtividade> findDiasByAtividade(Atividade atividade) {
		Query q = manager
				.createQuery("select s from " + DiasAtividade.NAME
						+ " s where s.atividade=:arg0 and s.quantidadeHoras is not null order by s.dia")
				.setParameter("arg0", atividade);
		Collection<DiasAtividade> diasAtividades = new ArrayList<DiasAtividade>();
		for (Object o : q.getResultList()) {
			diasAtividades.add((DiasAtividade) o);
		}
		return diasAtividades;
	}

	public Collection<DiasAtividade> findAllDiasByAtividade(Atividade atividade) {
		Query q = manager
				.createQuery("select s from " + DiasAtividade.NAME + " s where s.atividade=:arg0 order by s.dia")
				.setParameter("arg0", atividade);
		Collection<DiasAtividade> diasAtividades = new ArrayList<DiasAtividade>();
		for (Object o : q.getResultList()) {
			diasAtividades.add((DiasAtividade) o);
		}
		return diasAtividades;
	}
}
