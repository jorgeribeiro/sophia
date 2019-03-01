package br.gov.ma.tce.sophia.ejb.beans.avaliacaoreacao;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.gov.ma.tce.sophia.ejb.beans.atividade.Atividade;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscrito;

@Stateless
public class AvaliacaoReacaoFacadeBean {

	@PersistenceContext(unitName = "sigesco_escex")
	public EntityManager manager;

	public AvaliacaoReacao include(AvaliacaoReacao avaliacaoReacao) {
		manager.persist(avaliacaoReacao);
		return avaliacaoReacao;
	}

	public AvaliacaoReacao update(AvaliacaoReacao avaliacaoReacao) {
		manager.merge(avaliacaoReacao);
		return avaliacaoReacao;
	}

	public void delete(int avaliacaoReacaoId) {
		AvaliacaoReacao avaliacaoReacao = findByPrimaryKey(avaliacaoReacaoId);
		manager.remove(avaliacaoReacao);
	}

	public AvaliacaoReacao findByPrimaryKey(Integer avaliacaoReacaoId) {
		AvaliacaoReacao avaliacaoReacao = manager.find(AvaliacaoReacao.class, avaliacaoReacaoId);
		return avaliacaoReacao;
	}

	public Collection<AvaliacaoReacao> findAll() {
		Query q = manager.createQuery("select s from " + AvaliacaoReacao.NAME + " s	order by s.avaliacaoReacaoId");
		Collection<AvaliacaoReacao> avaliacoesReacao = new ArrayList<AvaliacaoReacao>();
		for (Object o : q.getResultList()) {
			avaliacoesReacao.add((AvaliacaoReacao) o);
		}
		return avaliacoesReacao;
	}
	

	public Collection<AvaliacaoReacao> findByAtividade(Atividade atividade) {
		Query q = manager.createQuery("select s from " + AvaliacaoReacao.NAME + " s where s.participanteInscrito.atividade=:arg0"
				+ "	order by s.participanteInscrito.participante.nome").setParameter("arg0", atividade);
		Collection<AvaliacaoReacao> avaliacoesReacao = new ArrayList<AvaliacaoReacao>();
		for (Object o : q.getResultList()) {
			avaliacoesReacao.add((AvaliacaoReacao) o);
		}
		return avaliacoesReacao;
	}
	
	
	public AvaliacaoReacao findByParticipanteInscrito(ParticipanteInscrito participanteInscrito) {
		Query q = manager
				.createQuery("select s from " + AvaliacaoReacao.NAME
						+ " s where s.participanteInscrito=:arg0 order by s.participanteInscrito.participante.nome")
				.setParameter("arg0", participanteInscrito);
		return q.getResultList().isEmpty() ? null : (AvaliacaoReacao) q.getResultList().get(0);
	}
}
