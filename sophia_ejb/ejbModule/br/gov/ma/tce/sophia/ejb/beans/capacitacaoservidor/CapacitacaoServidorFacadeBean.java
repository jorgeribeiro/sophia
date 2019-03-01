package br.gov.ma.tce.sophia.ejb.beans.capacitacaoservidor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.gov.ma.tce.sophia.ejb.beans.pessoa.Pessoa;

@Stateless
public class CapacitacaoServidorFacadeBean {

	@PersistenceContext(unitName = "sigesco_escex")
	public EntityManager manager;

	public CapacitacaoServidor include(CapacitacaoServidor capacitacaoServidor) {
		manager.persist(capacitacaoServidor);
		return capacitacaoServidor;
	}

	public CapacitacaoServidor update(CapacitacaoServidor capacitacaoServidor) {
		manager.merge(capacitacaoServidor);
		return capacitacaoServidor;
	}

	public void delete(int atividadeId) {
		CapacitacaoServidor capacitacaoServidor = findByPrimaryKey(atividadeId);
		manager.remove(capacitacaoServidor);
	}

	public CapacitacaoServidor findByPrimaryKey(Integer atividadeId) {
		CapacitacaoServidor capacitacaoServidor = manager.find(CapacitacaoServidor.class, atividadeId);
		return capacitacaoServidor;
	}

	public Collection<CapacitacaoServidor> findAll() {
		Query q = manager
				.createQuery("select s from " + CapacitacaoServidor.NAME + " s  order by s.capacitacaoServidorId");
		Collection<CapacitacaoServidor> capacitacoes = new ArrayList<CapacitacaoServidor>();
		for (Object o : q.getResultList()) {
			capacitacoes.add((CapacitacaoServidor) o);
		}
		return capacitacoes;
	}

	public Collection<CapacitacaoServidor> findCapacitacoesByParticipante(Pessoa participante) {
		Query q = manager
				.createQuery("select s from " + CapacitacaoServidor.NAME
						+ " s where s.participante=:arg0 order by s.nomeCapacitacao")
				.setParameter("arg0", participante);
		Collection<CapacitacaoServidor> capacitacoes = new ArrayList<CapacitacaoServidor>();
		for (Object o : q.getResultList()) {
			capacitacoes.add((CapacitacaoServidor) o);
		}
		return capacitacoes;
	}

	public Collection<CapacitacaoServidor> findCapacitacoesByPeriodo(String mes, String ano) {
		Calendar inicio = new GregorianCalendar(Integer.parseInt(ano), Integer.parseInt(mes), 1);
		Calendar fim = new GregorianCalendar(Integer.parseInt(ano), Integer.parseInt(mes),
				inicio.getActualMaximum(Calendar.DAY_OF_MONTH));
		Query q = manager
				.createQuery("select s from " + CapacitacaoServidor.NAME
						+ " s where s.dataInicio>=:arg0 and s.dataInicio<=:arg1 order by s.nomeCapacitacao")
				.setParameter("arg0", inicio.getTime())
				.setParameter("arg1", fim.getTime());
		Collection<CapacitacaoServidor> capacitacoes = new ArrayList<CapacitacaoServidor>();
		for (Object o : q.getResultList()) {
			capacitacoes.add((CapacitacaoServidor) o);
		}
		return capacitacoes;
	}
}
