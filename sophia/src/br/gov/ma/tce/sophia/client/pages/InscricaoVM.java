package br.gov.ma.tce.sophia.client.pages;

import java.util.Collection;
import java.util.Date;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import br.gov.ma.tce.sophia.client.utils.EmailSOPHIA;
import br.gov.ma.tce.sophia.client.utils.LongOperation;
import br.gov.ma.tce.sophia.ejb.beans.atividade.Atividade;
import br.gov.ma.tce.sophia.ejb.beans.atividadecolaborador.AtividadeColaboradorFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.evento.Evento;
import br.gov.ma.tce.sophia.ejb.beans.evento.EventoFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscrito;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscritoFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.participanteinteresse.ParticipanteInteresse;
import br.gov.ma.tce.sophia.ejb.beans.participanteinteresse.ParticipanteInteresseFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.pessoa.Pessoa;

public class InscricaoVM {
	private Pessoa pessoa;
	private Evento eventoSelecionado;
	private Atividade atividadeSelecionada, atividade;
	private ParticipanteInteresse participanteInteresse;
	private ParticipanteInscrito participanteInscrito;

	private EmailSOPHIA emailSOPHIA;

	private boolean menuUsuarioLogadoVisible, eventoAberto;
	private String caminhoInclude, id, mensagem, statusEvento, nomeEvento;

	private Collection<Atividade> atividades;

	private EventoFacadeBean eventoFacade;
	private ParticipanteInteresseFacadeBean participanteInteresseFacade;
	private ParticipanteInscritoFacadeBean participanteInscritoFacade;
	private AtividadeColaboradorFacadeBean atividadeColaboradorFacade;

	@Wire("#modalDetalhesDaAtividade")
	private Window modalDetalhesDaAtividade;

	@Wire("#modalLogin")
	private Window modalLogin;

	@Wire("#modalSair")
	private Window modalSair;

	@Wire("#modalConfirmacaoPreInscricao")
	private Window modalConfirmacaoPreInscricao;

	@Wire("#modalConfirmacaoListaDeInteresse")
	private Window modalConfirmacaoListaDeInteresse;

	public InscricaoVM() {
		try {
			InitialContext ctx = new InitialContext();
			eventoFacade = (EventoFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/EventoFacadeBean!br.gov.ma.tce.sophia.ejb.beans.evento.EventoFacadeBean");
			participanteInteresseFacade = (ParticipanteInteresseFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/ParticipanteInteresseFacadeBean!br.gov.ma.tce.sophia.ejb.beans.participanteinteresse.ParticipanteInteresseFacadeBean");
			participanteInscritoFacade = (ParticipanteInscritoFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/ParticipanteInscritoFacadeBean!br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscritoFacadeBean");
			atividadeColaboradorFacade = (AtividadeColaboradorFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/AtividadeColaboradorFacadeBean!br.gov.ma.tce.sophia.ejb.beans.atividadecolaborador.AtividadeColaboradorFacadeBean");
		} catch (NamingException e) {
			Clients.showNotification("Erro de conexão interno. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
			e.printStackTrace();
		}
	}

	@Init
	public void init() {

		try {
			pessoa = (Pessoa) Sessions.getCurrent().getAttribute("pessoa");
			if (pessoa != null) {
				menuUsuarioLogadoVisible = true;
			} else {
				menuUsuarioLogadoVisible = false;
			}

			id = Executions.getCurrent().getParameter("id");
			if (id == null || id.trim().equals("")) {
				Executions.sendRedirect("/notfound.zul");
			} else {
				eventoSelecionado = eventoFacade.findByPrimaryKey(Integer.parseInt(id));
				if (eventoSelecionado == null) {
					Executions.sendRedirect("/notfound.zul");
				} else {
					atividades = eventoSelecionado.getAtividades();
					statusEvento = eventoSelecionado.getCategoria();
					nomeEvento = eventoSelecionado.getNomeStr() + " - " + geraLegenda();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);

		atividade = (Atividade) Sessions.getCurrent().getAttribute("atividade");
		if (atividade != null) {
			Sessions.getCurrent().removeAttribute("atividade");
			if (pessoa != null) {
				verificaLoginParaAbrirModal(atividade, true);

			}
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
	public void abrirModalDetalhesDaAtividade(@BindingParam("atividade") Atividade atividade,
			@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				this.atividadeSelecionada = atividade;
				atividadeSelecionada
						.setColaboradores(this.atividadeColaboradorFacade.findColaboradoresByAtividade(atividade));
				modalDetalhesDaAtividade.setVisible(visible);
			} else {
				modalDetalhesDaAtividade.setVisible(visible);
			}
			BindUtils.postNotifyChange(null, null, this, "*");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Command
	@NotifyChange(".")
	public void redirecionaLoginIDEvento() {
		if (id != null || !id.trim().equals("")) {
			Sessions.getCurrent().setAttribute("evento", eventoSelecionado);
		}
		Executions.sendRedirect("/login.zul");
	}

	@Command
	@NotifyChange(".")
	public void redirecionaLoginIDAtividade() {
		if (id != null || !id.trim().equals("")) {
			Sessions.getCurrent().setAttribute("evento", eventoSelecionado);
			Sessions.getCurrent().setAttribute("atividade", atividadeSelecionada);
			Sessions.getCurrent().setAttribute("vemDoIndex", false);
		}
		Executions.sendRedirect("/login.zul");
	}

	@Command
	@NotifyChange(".")
	public void redirecionaCadastro() {
		if (id != null || !id.trim().equals("")) {
			Sessions.getCurrent().setAttribute("evento", eventoSelecionado);
			Sessions.getCurrent().setAttribute("atividade", atividadeSelecionada);
			Sessions.getCurrent().setAttribute("vemDoIndex", false);
		}
		Executions.sendRedirect("/cadastro.zul");
	}

	@Command
	@NotifyChange(".")
	public void verificaLoginParaAbrirModal(@BindingParam("atividade") Atividade atividade,
			@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				atividadeSelecionada = atividade;
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
			if (!menuUsuarioLogadoVisible) {
				modalLogin.setVisible(visible);
			} else {
				if (statusEvento.equals("ABERTO") || statusEvento.equals("EM ANDAMENTO")) {
					mensagem = "Confirma a pré-inscrição na atividade " + atividadeSelecionada.getNomeStr() + "?";
					modalConfirmacaoPreInscricao.setVisible(visible);
				} else {
					mensagem = "Confirma a inscrição na lista de interesse da atividade "
							+ atividadeSelecionada.getNomeStr() + "?";
					modalConfirmacaoListaDeInteresse.setVisible(visible);
				}
			}

		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@NotifyChange
	public String geraLegenda() {
		if (statusEvento.equals("ABERTO")) {
			this.setEventoAberto(true);
			return "Evento aberto";
		} else if (statusEvento.equals("PREVISTO")) {
			this.setEventoAberto(false);
			return "Evento previsto";
		} else if (statusEvento.equals("EM ANDAMENTO")) {
			return "Evento em andamento";
		} else if (statusEvento.equals("REALIZADO")) {
			return "Evento realizado";
		} else {
			return "Evento cancelado";
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

	// Classe utilizada para gerenciar clicks em botões criados nessa VM
	public class MyButton extends Button {
		private static final long serialVersionUID = 1L;
		Atividade atividade;

		public MyButton(String label, Atividade atividade) {
			super(label);
			this.atividade = atividade;
		}

		public void onClick() {
			if (this.getLabel().equals("Logar")) {
				Executions.sendRedirect("/login.zul");
			} else if (this.getLabel().equals("Mais Detalhes")) {
				abrirModalDetalhesDaAtividade(atividade, true);
			}
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
			
		//Todos
		}else if (atividadeSelecionada.getTipoParticipante().equals("7")){
			
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

	public Evento getEventoSelecionado() {
		return eventoSelecionado;
	}

	public void setEventoSelecionado(Evento eventoSelecionado) {
		this.eventoSelecionado = eventoSelecionado;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public Atividade getAtividadeSelecionada() {
		return atividadeSelecionada;
	}

	public void setAtividadeSelecionada(Atividade atividadeSelecionada) {
		this.atividadeSelecionada = atividadeSelecionada;
	}

	public String getCaminhoInclude() {
		return caminhoInclude;
	}

	public void setCaminhoInclude(String caminhoInclude) {
		this.caminhoInclude = caminhoInclude;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatusEvento() {
		return statusEvento;
	}

	public void setStatusEvento(String statusEvento) {
		this.statusEvento = statusEvento;
	}

	public String getNomeEvento() {
		return nomeEvento;
	}

	public void setNomeEvento(String nomeEvento) {
		this.nomeEvento = nomeEvento;
	}

	public Collection<Atividade> getAtividades() {
		return atividades;
	}

	public void setAtividades(Collection<Atividade> atividades) {
		this.atividades = atividades;
	}

	public boolean isMenuUsuarioLogadoVisible() {
		return menuUsuarioLogadoVisible;
	}

	public void setMenuUsuarioLogadoVisible(boolean menuUsuarioLogadoVisible) {
		this.menuUsuarioLogadoVisible = menuUsuarioLogadoVisible;
	}

	public boolean isEventoAberto() {
		return eventoAberto;
	}

	public void setEventoAberto(boolean eventoAberto) {
		this.eventoAberto = eventoAberto;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

}
