package br.gov.ma.tce.sophia.ejb.beans.tokencertificado;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.gov.ma.tce.sophia.ejb.beans.atividadecolaborador.AtividadeColaborador;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscrito;

@Stateless
public class TokenCertificadoFacadeBean {

	@PersistenceContext(unitName = "sigesco_escex")
	public EntityManager manager;

	public TokenCertificado include(TokenCertificado tokenCertificado) {
		manager.persist(tokenCertificado);
		return tokenCertificado;
	}

	public TokenCertificado update(TokenCertificado tokenCertificado) {
		manager.merge(tokenCertificado);
		return tokenCertificado;
	}

	public void delete(int tokenCertificadoId) {
		TokenCertificado tokenCertificado = findByPrimaryKey(tokenCertificadoId);
		manager.remove(tokenCertificado);
	}

	public TokenCertificado findByPrimaryKey(Integer tokenCertificadoId) {
		TokenCertificado tokenCertificado = manager.find(TokenCertificado.class, tokenCertificadoId);
		return tokenCertificado;
	}

	public Collection<TokenCertificado> findAll() {
		Query q = manager.createQuery("select s from " + TokenCertificado.NAME + " s  order by s.tokenCertificadoId");
		Collection<TokenCertificado> tokenCertificados = new ArrayList<TokenCertificado>();
		for (Object o : q.getResultList()) {
			tokenCertificados.add((TokenCertificado) o);
		}
		return tokenCertificados;
	}

	public TokenCertificado findTokenCertificadoByToken(String token) {
		token = token.replaceAll("\'", "");
		Query q = manager.createQuery("select s from " + TokenCertificado.NAME + " s where s.token=:arg0")
				.setParameter("arg0", token);
		return q.getResultList().isEmpty() ? null : (TokenCertificado) q.getResultList().get(0);
	}

	public TokenCertificado findTokenCertificadoByParticipanteInscrito(ParticipanteInscrito participanteInscrito) {
		Query q = manager
				.createQuery("select s from " + TokenCertificado.NAME + " s where s.participanteInscrito=:arg0")
				.setParameter("arg0", participanteInscrito);
		return q.getResultList().isEmpty() ? null : (TokenCertificado) q.getResultList().get(0);
	}
	
	public TokenCertificado findTokenCertificadoByAtividadeColaborador(AtividadeColaborador atividadeColaborador) {
		Query q = manager
				.createQuery("select s from " + TokenCertificado.NAME + " s where s.atividadeColaborador=:arg0")
				.setParameter("arg0", atividadeColaborador);
		return q.getResultList().isEmpty() ? null : (TokenCertificado) q.getResultList().get(0);
	}

}
