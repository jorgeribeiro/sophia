package br.gov.ma.tce.sophia.ejb.beans.arquivo;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.gov.ma.tce.sophia.ejb.beans.atividade.Atividade;
import br.gov.ma.tce.sophia.ejb.beans.arquivo.Arquivo;

@Stateless
public class ArquivoFacadeBean {
	@PersistenceContext(unitName = "sigesco_escex")
	public EntityManager manager;

	public Arquivo include(Arquivo arquivo) {
		manager.persist(arquivo);
		return arquivo;
	}

	public Arquivo update(Arquivo arquivo) {
		manager.merge(arquivo);
		return arquivo;
	}

	public void delete(int arquivoId) {
		Arquivo arquivo = findByPrimaryKey(arquivoId);
		manager.remove(arquivo);
	}

	public Arquivo findByPrimaryKey(Integer arquivoId) {
		Arquivo arquivo = manager.find(Arquivo.class, arquivoId);
		return arquivo;
	}

	public Collection<Arquivo> findAll() {
		Query q = manager.createQuery("select s from " + Arquivo.NAME + " s	order by s.arquivoId");
		Collection<Arquivo> arquivos = new ArrayList<Arquivo>();
		for (Object o : q.getResultList()) {
			arquivos.add((Arquivo) o);
		}
		return arquivos;
	}

	public Collection<Arquivo> findByAtividade(Atividade atividade) {
		Query q = manager.createQuery("select s from " + Arquivo.NAME + " s where s.atividade=:arg0")
				.setParameter("arg0", atividade);
		Collection<Arquivo> arquivos = new ArrayList<Arquivo>();
		for (Object o : q.getResultList()) {
			arquivos.add((Arquivo) o);
		}
		return arquivos;
	}
}
