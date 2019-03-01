package br.gov.ma.tce.sophia.client.pages;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;

import br.gov.ma.tce.sophia.ejb.beans.tokencertificado.TokenCertificado;
import br.gov.ma.tce.sophia.ejb.beans.tokencertificado.TokenCertificadoFacadeBean;

public class ValidarCertificadoVM {
	private String token;

	private TokenCertificado tokenCertificado;

	private TokenCertificadoFacadeBean tokenCertificadoFacade;

	public ValidarCertificadoVM() {
		try {
			InitialContext ctx = new InitialContext();
			tokenCertificadoFacade = (TokenCertificadoFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/TokenCertificadoFacadeBean!br.gov.ma.tce.sophia.ejb.beans.tokencertificado.TokenCertificadoFacadeBean");
		} catch (NamingException e) {
			Clients.showNotification("Erro de conexão interno. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
			e.printStackTrace();
		}
	}

	@Init
	public void init() {

	}

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}

	@Command
	@NotifyChange(".")
	public void validarCertificado() {
		try {
			if (token != null) {
				token = token.trim();
				if (tokenCertificado != null && token.equals(tokenCertificado.getToken())) {
					return;
				}
			}
			if (token == null || token.equals("")) {
				tokenCertificado = null;
				throw new Exception("Preencha o campo de token de verificação.");
			}
			tokenCertificado = tokenCertificadoFacade.findTokenCertificadoByToken(token);
			if (tokenCertificado == null) {
				throw new Exception("Não foi encontrado certificado com o token informado.");
			} else {
				Clients.showNotification("Certificado encontrado!", Clients.NOTIFICATION_TYPE_INFO, null, null, 2000,
						true);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public TokenCertificado getTokenCertificado() {
		return tokenCertificado;
	}

	public void setTokenCertificado(TokenCertificado tokenCertificado) {
		this.tokenCertificado = tokenCertificado;
	}

}
