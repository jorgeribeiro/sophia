package br.gov.ma.tce.sophia.client.pages;

import java.io.InputStream;
import java.util.Date;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import br.gov.ma.tce.sophia.client.utils.EmailSOPHIA;
import br.gov.ma.tce.sophia.client.utils.FTPUtil;
import br.gov.ma.tce.sophia.client.utils.LongOperation;
import br.gov.ma.tce.sophia.ejb.beans.atividade.Atividade;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscrito;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscritoFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.participanteinteresse.ParticipanteInteresse;
import br.gov.ma.tce.sophia.ejb.beans.participanteinteresse.ParticipanteInteresseFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.pessoa.Pessoa;

public class IndexVM {
	private Pessoa pessoa;
	private ParticipanteInteresse participanteInteresse;
	private ParticipanteInscrito participanteInscrito;
	private Atividade atividadeSelecionada;

	private EmailSOPHIA emailSOPHIA;

	private String caminhoInclude, mensagem;
	private boolean menuUsuarioLogadoVisible;

	private ParticipanteInteresseFacadeBean participanteInteresseFacade;
	private ParticipanteInscritoFacadeBean participanteInscritoFacade;

	@Wire("#modalSair")
	private Window modalSair;

	@Wire("#modalConfirmacaoPreInscricao")
	private Window modalConfirmacaoPreInscricao;

	@Wire("#modalConfirmacaoListaDeInteresse")
	private Window modalConfirmacaoListaDeInteresse;

	public IndexVM() {
		try {
			InitialContext ctx = new InitialContext();
			participanteInteresseFacade = (ParticipanteInteresseFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/ParticipanteInteresseFacadeBean!br.gov.ma.tce.sophia.ejb.beans.participanteinteresse.ParticipanteInteresseFacadeBean");
			participanteInscritoFacade = (ParticipanteInscritoFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/ParticipanteInscritoFacadeBean!br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscritoFacadeBean");

		} catch (NamingException e) {
			Clients.showNotification("Erro de conexão interno. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
			e.printStackTrace();
		}
	}

	@Init
	public void init() {
		// Box inicial
		caminhoInclude = (String) Sessions.getCurrent().getAttribute("paginaatual");
		if (caminhoInclude == null) {
			caminhoInclude = "eventos/visualizareventos.zul";
		}
	}

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);

		pessoa = (Pessoa) Sessions.getCurrent().getAttribute("pessoa");
		if (pessoa != null) {
			menuUsuarioLogadoVisible = true;
			atividadeSelecionada = (Atividade) Sessions.getCurrent().getAttribute("atividade");
			if (atividadeSelecionada != null) {
				Sessions.getCurrent().removeAttribute("atividade");
				verificaLoginParaAbrirModal(true);
			}
		} else {
			menuUsuarioLogadoVisible = false;
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalSair(@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				modalSair.setVisible(true);
			} else {
				modalSair.setVisible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void sair() {
		try {
			Sessions.getCurrent().invalidate();
			Executions.sendRedirect("/login.zul");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void visualizarEventos() {
		caminhoInclude = "eventos/visualizareventos.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void eventosPrevistos() {
		caminhoInclude = "eventos/previstos.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void eventosAbertos() {
		caminhoInclude = "eventos/abertos.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void eventosEmAndamento() {
		caminhoInclude = "eventos/andamento.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void eventosRealizados() {
		caminhoInclude = "eventos/realizados.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void eventosCancelados() {
		caminhoInclude = "eventos/cancelados.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void meusEventos() {
		caminhoInclude = "eventos/meuseventos.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void minhasPreInscricoes() {
		caminhoInclude = "participante/minhaspreinscricoes.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void listaDeInteresse() {
		caminhoInclude = "participante/listadeinteresse.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void outrasCapacitacoes() {
		caminhoInclude = "participante/outrascapacitacoes.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void preCadastroColaborador() {
		caminhoInclude = "colaborador/precadastrocolaborador.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void minhasColaboracoes() {
		caminhoInclude = "colaborador/minhascolaboracoes.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void meusCertificadosParticipacao() {
		caminhoInclude = "minhaconta/meuscertificadosparticipacao.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void meusCertificadosColaboracao() {
		caminhoInclude = "minhaconta/meuscertificadoscolaboracao.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void meusDados() {
		caminhoInclude = "minhaconta/meusdados.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void trocarSenha() {
		caminhoInclude = "minhaconta/trocarsenha.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void validarCertificado() {
		caminhoInclude = "validarcertificado.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void downloadManual() {
		try {
			FTPUtil ftpUtil = new FTPUtil();
			InputStream in = ftpUtil.downloadArquivo2("Manual de Utilizacao - Sophia.pdf", "manuais_utilizacao");
			Filedownload.save(in, "application/pdf", "Manual de Utilizacao - Sophia.pdf");
		} catch (Exception e) {
			Clients.showNotification("Erro ao realizar download do manual. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000);
		}

	}

	@Command
	@NotifyChange(".")
	public void verificaLoginParaAbrirModal(@BindingParam("visible") boolean visible) {
		try {
			String statusEvento = atividadeSelecionada.getEvento().getCategoria();
			if (visible) {
				if (atividadeSelecionada.isAtividadeCancelada() || statusEvento.equals("CANCELADO")) {
					throw new Exception("Atividade foi cancelada!");
				} else if (statusEvento.equals("REALIZADO")) {
					throw new Exception("Atividade já realizada!");
				} else if (atividadeSelecionada.getPreInscricoesEncerradas()) {
					throw new Exception("Período de pré-inscrições encerrado.");
				}
				if (!statusEvento.equals("PREVISTO")) {
					if (new Date().before(atividadeSelecionada.getPreInscricaoDataInicio())) {
						throw new Exception("Aguarde o início do período de pré-inscrição.");
					} else if (atividadeSelecionada.getInscricoesFechadas()) {
						throw new Exception("Inscrições fechadas.");
					}
				}
			}
			/*
			 * Se usuário não tiver logado, abre a modal para logar, caso não, abre a modal
			 * de confirmação de inscrição
			 */
			if (statusEvento.equals("ABERTO") || statusEvento.equals("EM ANDAMENTO")) {
				mensagem = "Confirma a pré-inscrição na atividade " + atividadeSelecionada.getNomeStr() + "?";
				modalConfirmacaoPreInscricao.setVisible(visible);
			} else {
				mensagem = "Confirma a inscrição na lista de interesse da atividade "
						+ atividadeSelecionada.getNomeStr() + "?";
				modalConfirmacaoListaDeInteresse.setVisible(visible);
			}

		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void inscreverParticipanteNaPreInscricao() {
		try {

			if (participanteInscritoFacade.findByParticipanteAndAtividade(pessoa, atividadeSelecionada) != null) {
				Clients.showNotification("Você já está pré-inscrito ou inscrito nessa atividade.",
						Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
			} else if (!verificarParticipanteEAtividade()) {
				throw new Exception("Você não atende ao público-alvo dessa atividade.");
			} else {
				confirmaPreInscricao();
			}
		} catch (Exception e) {

			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}

	}

	@Command
	@NotifyChange(".")
	public void inscreverParticipanteNaListaDeInteresse() {
		try {

			if (participanteInteresseFacade.findByParticipanteAndAtividade(pessoa, atividadeSelecionada) != null) {
				Clients.showNotification("Você já está adicionado à lista de interesse dessa atividade.",
						Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
			} else if (!verificarParticipanteEAtividade()) {
				throw new Exception("Você não atende ao público-alvo dessa atividade.");
			} else {
				confirmaListaDeInteresse();
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmaPreInscricao(@BindingParam("visible") boolean visible) {
		try {
			if (!visible) {
				modalConfirmacaoPreInscricao.setVisible(visible);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmaListaDeInteresse(@BindingParam("visible") boolean visible) {
		try {
			if (!visible) {
				modalConfirmacaoListaDeInteresse.setVisible(visible);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void confirmaPreInscricao() {
		try {
			participanteInscrito = new ParticipanteInscrito();
			participanteInscrito.setParticipante(pessoa);
			participanteInscrito.setAtividade(atividadeSelecionada);
			participanteInscrito.setDtPreInscricao(new Date());
			if (participanteInscritoFacade.include(participanteInscrito) != null) {
				ParticipanteInscrito destinatario = new ParticipanteInscrito();
				destinatario.setParticipante(pessoa);
				destinatario.setAtividade(atividadeSelecionada);
				new LongOperation() {
					@Override
					protected void execute() throws InterruptedException {
						emailSOPHIA.emailPreInscricao(destinatario.getParticipante(), destinatario.getAtividade(), " ");
					}
				}.start();
				modalConfirmacaoPreInscricao.setVisible(false);
				Clients.showNotification("Pré-inscrição realizada com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null,
						null, 3000, true);
				participanteInscrito = new ParticipanteInscrito();
			}
		} catch (Exception e) {
			Clients.showNotification(
					"Ocorreu um erro inesperado ao realizar a pré-inscrição. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void confirmaListaDeInteresse() {
		try {
			participanteInteresse = new ParticipanteInteresse();
			participanteInteresse.setParticipante(pessoa);
			participanteInteresse.setAtividade(atividadeSelecionada);
			participanteInteresse.setDtInteresse(new Date());
			participanteInteresse.setInscritoNaAtividade(false);
			if (participanteInteresseFacade.include(participanteInteresse) != null) {
				ParticipanteInteresse destinatario = new ParticipanteInteresse();
				destinatario.setParticipante(pessoa);
				destinatario.setAtividade(atividadeSelecionada);
				new LongOperation() {
					@Override
					protected void execute() throws InterruptedException {
						emailSOPHIA.emailListaDeInteresse(destinatario.getParticipante(), destinatario.getAtividade(),
								" ");
					}
				}.start();
				modalConfirmacaoListaDeInteresse.setVisible(false);
				Clients.showNotification("Você foi adicionado à lista de interesse com sucesso!",
						Clients.NOTIFICATION_TYPE_INFO, null, null, 3000, true);
				participanteInteresse = new ParticipanteInteresse();
			}
		} catch (Exception e) {
			Clients.showNotification(
					"Ocorreu um erro inesperado ao entrar na lista de interesse. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	// Verifica se a pessoa atende ao público-alvo dessa atividade.
	public boolean verificarParticipanteEAtividade() {

		// Participante é sociedade
		if ((atividadeSelecionada.getTipoParticipante().equals("1")
				|| atividadeSelecionada.getTipoParticipante().equals("5")
				|| atividadeSelecionada.getTipoParticipante().equals("6")) && (pessoa.getTipoPessoa().equals("1"))) {

			return true;

			// Participante é servidor tce-ma
		} else if ((atividadeSelecionada.getTipoParticipante().equals("2")
				|| atividadeSelecionada.getTipoParticipante().equals("5")
				|| atividadeSelecionada.getTipoParticipante().equals("4")) && (pessoa.getTipoPessoa().equals("2"))) {

			return true;

			// Participante é jurisdicionado
		} else if ((atividadeSelecionada.getTipoParticipante().equals("3")
				|| atividadeSelecionada.getTipoParticipante().equals("4")
				|| atividadeSelecionada.getTipoParticipante().equals("6")) && (pessoa.getTipoPessoa().equals("3"))) {

			return true;

			// Todos
		} else if (atividadeSelecionada.getTipoParticipante().equals("7")) {

			return true;
		}

		return false;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Atividade getAtividadeSelecionada() {
		return atividadeSelecionada;
	}

	public void setAtividadeSelecionada(Atividade atividadeSelecionada) {
		this.atividadeSelecionada = atividadeSelecionada;
	}

	public ParticipanteInscrito getParticipanteInscrito() {
		return participanteInscrito;
	}

	public void setParticipanteInscrito(ParticipanteInscrito participanteInscrito) {
		this.participanteInscrito = participanteInscrito;
	}

	public ParticipanteInteresse getParticipanteInteresse() {
		return participanteInteresse;
	}

	public void setParticipanteInteresse(ParticipanteInteresse participanteInteresse) {
		this.participanteInteresse = participanteInteresse;
	}

	public String getCaminhoInclude() {
		return caminhoInclude;
	}

	public void setCaminhoInclude(String caminhoInclude) {
		this.caminhoInclude = caminhoInclude;
	}

	public boolean isMenuUsuarioLogadoVisible() {
		return menuUsuarioLogadoVisible;
	}

	public void setMenuUsuarioLogadoVisible(boolean menuUsuarioLogadoVisible) {
		this.menuUsuarioLogadoVisible = menuUsuarioLogadoVisible;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

}