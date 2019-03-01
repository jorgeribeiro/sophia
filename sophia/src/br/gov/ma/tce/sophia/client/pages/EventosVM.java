package br.gov.ma.tce.sophia.client.pages;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.PersistenceException;

import org.apache.commons.io.FilenameUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import br.gov.ma.tce.sophia.client.utils.ArquivoUtil;
import br.gov.ma.tce.sophia.client.utils.EmailSOPHIA;
import br.gov.ma.tce.sophia.client.utils.FTPUtil;
import br.gov.ma.tce.sophia.client.utils.LongOperation;
import br.gov.ma.tce.sophia.ejb.beans.arquivo.Arquivo;
import br.gov.ma.tce.sophia.ejb.beans.arquivo.ArquivoFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.atividade.Atividade;
import br.gov.ma.tce.sophia.ejb.beans.atividade.AtividadeFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.atividadecolaborador.AtividadeColaboradorFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.evento.Evento;
import br.gov.ma.tce.sophia.ejb.beans.evento.EventoFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscrito;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscritoFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.participanteinteresse.ParticipanteInteresse;
import br.gov.ma.tce.sophia.ejb.beans.participanteinteresse.ParticipanteInteresseFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.pessoa.Pessoa;
import br.gov.ma.tce.sophia.ejb.util.Filtro;

public class EventosVM {
	private Atividade atividadeSelecionada;
	private Pessoa participante;
	private ParticipanteInteresse participanteInteresse;
	private ParticipanteInscrito participanteInscrito;
	private Filtro filtroEvento;

	private EmailSOPHIA emailSOPHIA;
	private FTPUtil ftpUtil;
	private ArquivoUtil arqUtil;

	private boolean eventoAberto = true;
	private static final String textoRodape = "%d evento(s) encontrado(s)";
	private String filtroNomeEvento;
	private Date dataInicio, dataFim;

	private Collection<Evento> eventos;
	private Collection<Atividade> atividades;
	private Collection<ParticipanteInscrito> inscricoes;
	private Collection<Arquivo> arquivos;
	
	private AtividadeFacadeBean atividadeFacade;
	private EventoFacadeBean eventoFacade;
	private ParticipanteInteresseFacadeBean participanteInteresseFacade;
	private ParticipanteInscritoFacadeBean participanteInscritoFacade;
	private AtividadeColaboradorFacadeBean atividadeColaboradorFacade;
	private ArquivoFacadeBean arquivoFacade;

	@Wire("#tabboxEventosAbertos")
	private Tabbox tabboxEventosAbertos;

	@Wire("#tabboxEventosPrevistos")
	private Tabbox tabboxEventosPrevistos;

	@Wire("#tabboxEventosEmAndamento")
	private Tabbox tabboxEventosEmAndamento;

	@Wire("#tabboxEventosRealizados")
	private Tabbox tabboxEventosRealizados;

	@Wire("#tabboxEventosCancelados")
	private Tabbox tabboxEventosCancelados;

	@Wire("#modalLogin")
	private Window modalLogin;

	@Wire("#modalConfirmacaoPreInscricao")
	private Window modalConfirmacaoPreInscricao;

	@Wire("#modalDetalhesDaAtividade")
	private Window modalDetalhesDaAtividade;

	@Wire("#modalConfirmacaoListaDeInteresse")
	private Window modalConfirmacaoListaDeInteresse;

	@Wire("#modalVisualizarMaterial")
	private Window modalVisualizarMaterial;

	@Wire("#banner")
	private Image banner;

	public EventosVM() {
		try {
			InitialContext ctx = new InitialContext();
			atividadeFacade = (AtividadeFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/AtividadeFacadeBean!br.gov.ma.tce.sophia.ejb.beans.atividade.AtividadeFacadeBean");
			eventoFacade = (EventoFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/EventoFacadeBean!br.gov.ma.tce.sophia.ejb.beans.evento.EventoFacadeBean");
			participanteInteresseFacade = (ParticipanteInteresseFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/ParticipanteInteresseFacadeBean!br.gov.ma.tce.sophia.ejb.beans.participanteinteresse.ParticipanteInteresseFacadeBean");
			participanteInscritoFacade = (ParticipanteInscritoFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/ParticipanteInscritoFacadeBean!br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscritoFacadeBean");
			atividadeColaboradorFacade = (AtividadeColaboradorFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/AtividadeColaboradorFacadeBean!br.gov.ma.tce.sophia.ejb.beans.atividadecolaborador.AtividadeColaboradorFacadeBean");
			arquivoFacade = (ArquivoFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/ArquivoFacadeBean!br.gov.ma.tce.sophia.ejb.beans.arquivo.ArquivoFacadeBean");
		} catch (NamingException e) {
			Clients.showNotification("Erro de conexão interno. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
			e.printStackTrace();
		}
	}

	@Init
	public void init() {
		participante = (Pessoa) Sessions.getCurrent().getAttribute("pessoa");
		participanteInteresse = new ParticipanteInteresse();
		participanteInscrito = new ParticipanteInscrito();
		inscricoes = new ArrayList<ParticipanteInscrito>();
		atividadeSelecionada = new Atividade();
		filtroEvento = new Filtro();
		emailSOPHIA = new EmailSOPHIA();
		filtroNomeEvento = "";
	}

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}

	@Command
	@NotifyChange(".")
	public void popularEventosAbertos() {
		try {
			eventos = eventoFacade.findEventosByCategoria("ABERTO");
		} catch (PersistenceException e) {
			e.printStackTrace();
			Executions.sendRedirect("/error.zul");
		}

		// Populando tabbox de eventos abertos
		for (Evento e : eventos) {
			e.setAtividades(atividadeFacade.findAtividadesByEvento(e));
			if (!e.getAtividades().isEmpty()) {
				construtorTabboxEventos(tabboxEventosAbertos, e);
			}
		}

		// Coloca última tab como selecionada (melhora de visualização)
		if (!eventos.isEmpty()) {
			int index = tabboxEventosAbertos.getTabs().getChildren().size() - 1;
			tabboxEventosAbertos.setSelectedIndex(index);
		} else {
			Tab tab = new Tab("Sem eventos abertos");
			tab.setParent(tabboxEventosAbertos.getTabs());
			Tabpanel tabPanel = new Tabpanel();
			tabPanel.setParent(tabboxEventosAbertos.getTabpanels());
		}
	}

	@Command
	@NotifyChange(".")
	public void popularEventosPrevistos() {
		try {
			eventos = eventoFacade.findEventosByCategoria("PREVISTO");
		} catch (PersistenceException e) {
			e.printStackTrace();
			Executions.sendRedirect("/error.zul");
		}

		// Populando tabbox de eventos previstos
		for (Evento e : eventos) {
			e.setAtividades(atividadeFacade.findAtividadesByEvento(e));
			if (!e.getAtividades().isEmpty()) {
				construtorTabboxEventos(tabboxEventosPrevistos, e);
			}
		}

		// Coloca última tab como selecionada (melhora de visualização)
		if (!eventos.isEmpty()) {
			int index = tabboxEventosPrevistos.getTabs().getChildren().size() - 1;
			tabboxEventosPrevistos.setSelectedIndex(index);
		} else {
			Tab tab = new Tab("Sem eventos previstos");
			tab.setParent(tabboxEventosPrevistos.getTabs());
			Tabpanel tabPanel = new Tabpanel();
			tabPanel.setParent(tabboxEventosPrevistos.getTabpanels());
		}
	}

	@Command
	@NotifyChange(".")
	public void popularEventosEmAndamento() {
		try {
			eventos = eventoFacade.findEventosByCategoria("EM ANDAMENTO");
		} catch (PersistenceException e) {
			e.printStackTrace();
			Executions.sendRedirect("/error.zul");
		}

		// Populando tabbox de eventos em andamento
		for (Evento e : eventos) {
			e.setAtividades(atividadeFacade.findAtividadesByEvento(e));
			if (!e.getAtividades().isEmpty()) {
				construtorTabboxEventos(tabboxEventosEmAndamento, e);
			}
		}

		// Coloca última tab como selecionada (melhora de visualização)
		if (!eventos.isEmpty()) {
			int index = tabboxEventosEmAndamento.getTabs().getChildren().size() - 1;
			tabboxEventosEmAndamento.setSelectedIndex(index);
		} else {
			Tab tab = new Tab("Sem eventos em andamento");
			tab.setParent(tabboxEventosEmAndamento.getTabs());
			Tabpanel tabPanel = new Tabpanel();
			tabPanel.setParent(tabboxEventosEmAndamento.getTabpanels());
		}
	}

	@Command
	@NotifyChange(".")
	public void popularEventosRealizados(@BindingParam("click") boolean click,
			@BindingParam("listarTodos") boolean listarTodos) {
		try {
			// Limpa a tabbox
			tabboxEventosRealizados.getTabs().getChildren().clear();
			tabboxEventosRealizados.getTabpanels().getChildren().clear();

			// Busca os eventos realizados de acordo com os filtros
			eventos = buscarEventosRealizados(click, listarTodos);
		} catch (PersistenceException e) {
			e.printStackTrace();
			Executions.sendRedirect("/error.zul");
		}

		// Populando tabbox de eventos realizados
		for (Evento e : eventos) {
			e.setAtividades(atividadeFacade.findAtividadesByEvento(e));
			if (!e.getAtividades().isEmpty()) {
				construtorTabboxEventos(tabboxEventosRealizados, e);
			}
		}

		// Coloca última tab como selecionada (melhora de visualização)
		if (!eventos.isEmpty()) {
			int index = tabboxEventosRealizados.getTabs().getChildren().size() - 1;
			tabboxEventosRealizados.setSelectedIndex(index);
		} else {
			Tab tab = new Tab("Utilize os filtros acima para encontrar os eventos realizados");
			tab.setParent(tabboxEventosRealizados.getTabs());
			Tabpanel tabPanel = new Tabpanel();
			tabPanel.setParent(tabboxEventosRealizados.getTabpanels());
		}

	}

	@Command
	@NotifyChange(".")
	public void popularEventosCancelados() {
		try {
			eventos = eventoFacade.findEventosByCategoria("CANCELADO");
		} catch (PersistenceException e) {
			e.printStackTrace();
			Executions.sendRedirect("/error.zul");
		}

		// Populando tabbox de eventos realizados
		for (Evento e : eventos) {
			e.setAtividades(atividadeFacade.findAtividadesByEvento(e));
			if (!e.getAtividades().isEmpty()) {
				construtorTabboxEventos(tabboxEventosCancelados, e);
			}
		}

		// Coloca última tab como selecionada (melhora de visualização)
		if (!eventos.isEmpty()) {
			int index = tabboxEventosCancelados.getTabs().getChildren().size() - 1;
			tabboxEventosCancelados.setSelectedIndex(index);
		} else {
			Tab tab = new Tab("Sem eventos cancelados");
			tab.setParent(tabboxEventosCancelados.getTabs());
			Tabpanel tabPanel = new Tabpanel();
			tabPanel.setParent(tabboxEventosCancelados.getTabpanels());
		}
	}

	@NotifyChange
	private void construtorTabboxEventos(Tabbox tabbox, Evento evento) {
		Tab tab = new Tab(evento.getEventoStr());
		tab.setImage("/imagens/double-arrow.png");
		tab.setSclass("custom-tab-eventos");
		tab.setParent(tabbox.getTabs());
		Tabpanel tabPanel = new Tabpanel();

		Vlayout vlayout = new Vlayout();

		try {
			Image image = new Image();
			if (ftpUtil == null) {
				ftpUtil = new FTPUtil();
			}
			InputStream in = ftpUtil.downloadArquivo2("banner_evento_id_" + evento.getEventoId() + ".jpg",
					"banners_eventos/banner_evento_id_" + evento.getEventoId());
			if (in != null) {
				AImage a = new AImage("banner_" + evento.getNome() + ".jpg", in);
				image.setContent(a);
				image.setHeight("170px");
				image.setParent(vlayout);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Hlayout hlayout = new Hlayout();
		hlayout.setHflex("1");
		hlayout.setParent(vlayout);
		Label label = new Label("Descrição ");
		label.setSclass("custom-label-eventos");
		label.setParent(hlayout);
		label = new Label(evento.getDescricao());
		label.setHflex("1");
		label.setMultiline(true);
		label.setParent(hlayout);

		hlayout = new Hlayout();
		hlayout.setHflex("1");
		hlayout.setParent(vlayout);
		label = new Label("Objetivo geral ");
		label.setSclass("custom-label-eventos");
		label.setParent(hlayout);
		label = new Label(evento.getObjetivoGeral());
		label.setHflex("1");
		label.setMultiline(true);
		label.setParent(hlayout);

		if (evento.getCategoria().equals("CANCELADO")) {
			hlayout = new Hlayout();
			hlayout.setHflex("1");
			hlayout.setParent(vlayout);
			label = new Label("Motivo do cancelamento ");
			label.setSclass("custom-label-eventos");
			label.setParent(hlayout);
			label = new Label(evento.getMotivoCancelamento());
			label.setHflex("1");
			label.setMultiline(true);
			label.setParent(hlayout);
		}

		Separator separator = new Separator();
		separator.setParent(vlayout);

		Vlayout vlayoutAtividades = new Vlayout();
		vlayoutAtividades.setParent(vlayout);
		label = new Label("Atividades disponíveis nesse evento");
		label.setSclass("custom-label-eventos");
		label.setParent(vlayoutAtividades);

		Grid gridAtividades = construtorGridAtividades(evento);
		gridAtividades.setParent(vlayoutAtividades);

		vlayout.setParent(tabPanel);
		tabPanel.setParent(tabbox.getTabpanels());
	}

	@NotifyChange
	private Grid construtorGridAtividades(Evento evento) {
		Grid grid = new Grid();
		Rows rows = new Rows();
		rows.setParent(grid);

		for (Atividade a : evento.getAtividades()) {

			Row row = new Row();
			Label label = new Label();
			Vlayout vlayoutInterior;

			Hlayout hlayout1 = new Hlayout();
			Vlayout vlayout1_h1 = new Vlayout();

			Hlayout hlayout2 = new Hlayout();
			// Primeira Coluna
			Vlayout vlayout1_h2 = new Vlayout();
			// Segunda Coluna
			Vlayout vlayout2_h2 = new Vlayout();
			// Terceira Coluna
			Vlayout vlayout3_h2 = new Vlayout();

			// Primeira Linha

			// Primeira Coluna
			label.setValue(a.getNomeStr());
			label.setMultiline(true);
			label.setSclass("custom-tab-atividades-regular");
			vlayout1_h1.setWidth("500px");
			vlayout1_h1.setSpacing("10px");
			vlayout1_h1.appendChild(label);

			// Segunda linha
			hlayout2.setSpacing("15px");
			// Primeira Coluna
			if (a.getEvento().getCategoria().equals("ABERTO") || a.getEvento().getCategoria().equals("PREVISTO")) {
				vlayoutInterior = new Vlayout();
				vlayoutInterior.setSpacing("0px");
				vlayoutInterior.setParent(vlayout1_h2);
				label = new Label("Status");
				label.setSclass("custom-label-atividades-titulo");
				label.setParent(vlayoutInterior);
				if (a.getEvento().getCategoria().equals("ABERTO")) {
					if (a.getPreInscricoesEncerradas()) {
						label = new Label("PRÉ-INSCRIÇÕES ENCERRADAS");
					} else if (a.getInscricoesFechadas()) {
						label = new Label("PRÉ-INSCRIÇÕES FECHADAS");
					} else if (new Date().before(a.getPreInscricaoDataInicio())) {
						label = new Label("PRÉ-INSCRIÇÕES NÃO INICIADAS");
					} else if (a.isAtividadeCancelada()) {
						label = new Label("ATIVIDADE CANCELADA");
					} else {
						label = new Label("PRÉ-INSCRIÇÕES ABERTAS");
					}
				} else {
					if (a.isAtividadeCancelada()) {
						label = new Label("ATIVIDADE CANCELADA");
					} else {
						label = new Label("LISTA DE INTERESSE ABERTA");
					}
				}
				label.setSclass("custom-label-atividades-conteudo");
				label.setMultiline(true);
				label.setParent(vlayoutInterior);
			}

			vlayoutInterior = new Vlayout();
			vlayoutInterior.setSpacing("0px");
			vlayoutInterior.setParent(vlayout1_h2);
			label = new Label("Disponibilização de certificado");
			label.setSclass("custom-label-atividades-titulo");
			label.setParent(vlayoutInterior);
			label = new Label(a.getGeraCertificadoStr());
			label.setSclass("custom-label-atividades-conteudo");
			label.setMultiline(true);
			label.setParent(vlayoutInterior);

			// Segunda Coluna
			vlayoutInterior = new Vlayout();
			vlayoutInterior.setSpacing("0px");
			vlayoutInterior.setParent(vlayout1_h2);
			label = new Label("Período de pré-inscrição");
			label.setSclass("custom-label-atividades-titulo");
			label.setParent(vlayoutInterior);
			label = new Label(a.getPreInscricaoDataStr());
			label.setSclass("custom-label-atividades-conteudo");
			label.setMultiline(true);
			label.setParent(vlayoutInterior);

			vlayoutInterior = new Vlayout();
			vlayoutInterior.setSpacing("0px");
			vlayoutInterior.setParent(vlayout2_h2);
			label = new Label("Período de realização");
			label.setSclass("custom-label-atividades-titulo");
			label.setParent(vlayoutInterior);
			label = new Label(a.getDataStr());
			label.setSclass("custom-label-atividades-conteudo");
			label.setMultiline(true);
			label.setParent(vlayoutInterior);

			vlayoutInterior = new Vlayout();
			vlayoutInterior.setSpacing("0px");
			vlayoutInterior.setParent(vlayout2_h2);
			label = new Label("Horário");
			label.setSclass("custom-label-atividades-titulo");
			label.setParent(vlayoutInterior);
			label = new Label(a.getHorarioStr());
			label.setSclass("custom-label-atividades-conteudo");
			label.setMultiline(true);
			label.setParent(vlayoutInterior);

			vlayoutInterior = new Vlayout();
			vlayoutInterior.setSpacing("0px");
			vlayoutInterior.setParent(vlayout2_h2);
			label = new Label("Estilo");
			label.setSclass("custom-label-atividades-titulo");
			label.setParent(vlayoutInterior);
			label = new Label(a.getEstilo());
			label.setSclass("custom-label-atividades-conteudo");
			label.setMultiline(true);
			label.setParent(vlayoutInterior);

			// Terceira Coluna
			vlayoutInterior = new Vlayout();
			vlayoutInterior.setSpacing("0px");
			vlayoutInterior.setParent(vlayout3_h2);
			label = new Label("Número de vagas");
			label.setSclass("custom-label-atividades-titulo");
			label.setParent(vlayoutInterior);
			label = new Label(a.getVagas().toString());
			label.setSclass("custom-label-atividades-conteudo");
			label.setMultiline(true);
			label.setParent(vlayoutInterior);

			MyButton myButtonDetalhes = new MyButton("Mais Detalhes", a);
			vlayout3_h2.appendChild(myButtonDetalhes);

			if (a.getEvento().getCategoria().equals("ABERTO")) {
				MyButton myButton = new MyButton("Pré-inscrição", a);
				vlayout3_h2.appendChild(myButton);
			} else if (a.getEvento().getCategoria().equals("PREVISTO")) {
				MyButton myButton = new MyButton("Lista de interesse", a);
				vlayout3_h2.appendChild(myButton);
			}

			hlayout1.appendChild(vlayout1_h1);

			hlayout2.appendChild(vlayout1_h2);
			hlayout2.appendChild(vlayout2_h2);
			hlayout2.appendChild(vlayout3_h2);

			row.appendChild(hlayout1);
			row.appendChild(hlayout2);

			row.setParent(rows);
		}
		return grid;
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
			if (this.getLabel().equals("Pré-inscrição")) {
				eventoAberto = true;
				abrirModalConfirmaPreInscricao(atividade, true);
			} else if (this.getLabel().equals("Mais Detalhes")) {
				abrirModalDetalhesDaAtividade(atividade, true);
			} else {
				eventoAberto = false;
				abrirModalConfirmaListaDeInteresse(atividade, true);
			}
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarEventosPorParticipante() {
		inscricoes = participanteInscritoFacade.findInscricoesByParticipanteAndInscricaoAprovada(participante);
	}

	@Command
	@NotifyChange(".")
	public void abrirModalLogin(@BindingParam("visible") boolean visible) {
		try {
			if (!visible) {
				Sessions.getCurrent().removeAttribute("atividade");
				Sessions.getCurrent().removeAttribute("estado");
			}
			modalLogin.setVisible(visible);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalDetalhesDaAtividade(Atividade atividade, @BindingParam("visible") boolean visible) {
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
	public void abrirModalConfirmaListaDeInteresse(Atividade atividade, @BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				atividadeSelecionada = atividade;
				if (atividade.isAtividadeCancelada()) {
					throw new Exception("Atividade cancelada.");
				} else if (participante == null) {
					Sessions.getCurrent().setAttribute("evento", atividade.getEvento());
					Sessions.getCurrent().setAttribute("atividade", atividadeSelecionada);
					Sessions.getCurrent().setAttribute("vemDoIndex", true);
					abrirModalLogin(true);
				} else {
					if (participanteInteresseFacade.findByParticipanteAndAtividade(participante, atividade) != null) {
						Clients.showNotification("Você já está adicionado à lista de interesse dessa atividade.",
								Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
					} else if (!verificarParticipanteEAtividade(atividade)) {
						throw new Exception("Você não atende ao público-alvo dessa atividade.");
					} else {
						modalConfirmacaoListaDeInteresse.setVisible(visible);
					}
				}
			} else {
				modalConfirmacaoListaDeInteresse.setVisible(visible);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void listaDeInteresse() {
		try {
			participanteInteresse.setParticipante(participante);
			participanteInteresse.setAtividade(atividadeSelecionada);
			participanteInteresse.setDtInteresse(new Date());
			participanteInteresse.setInscritoNaAtividade(false);
			if (participanteInteresseFacade.include(participanteInteresse) != null) {
				ParticipanteInteresse destinatario = new ParticipanteInteresse();
				destinatario.setParticipante(participante);
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

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmaPreInscricao(Atividade atividade, @BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				atividadeSelecionada = atividade;
				if (atividade.isAtividadeCancelada()) {
					throw new Exception("Atividade cancelada.");
				} else if (new Date().before(atividade.getPreInscricaoDataInicio())) {
					throw new Exception("Aguarde o início do período de pré-inscrição.");
				} else if (atividade.getPreInscricoesEncerradas()) {
					throw new Exception("Período de pré-inscrições encerrado.");
				} else if (atividade.getInscricoesFechadas()) {
					throw new Exception("Inscrições fechadas.");
				} else if (participante == null) {

					Sessions.getCurrent().setAttribute("evento", atividade.getEvento());
					Sessions.getCurrent().setAttribute("atividade", atividadeSelecionada);
					Sessions.getCurrent().setAttribute("vemDoIndex", true);
					abrirModalLogin(true);
				} else {
					if (participanteInscritoFacade.findByParticipanteAndAtividade(participante, atividade) != null) {
						Clients.showNotification("Você já está pré-inscrito ou inscrito nessa atividade.",
								Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
					} else if (!verificarParticipanteEAtividade(atividade)) {
						throw new Exception("Você não atende ao público-alvo dessa atividade.");
					} else {
						modalConfirmacaoPreInscricao.setVisible(visible);
					}
				}
			} else {
				modalConfirmacaoPreInscricao.setVisible(visible);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void preInscricao() {
		try {
			participanteInscrito.setParticipante(participante);
			participanteInscrito.setAtividade(atividadeSelecionada);
			participanteInscrito.setDtPreInscricao(new Date());
			if (participanteInscritoFacade.include(participanteInscrito) != null) {
				ParticipanteInscrito destinatario = new ParticipanteInscrito();
				destinatario.setParticipante(participante);
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
	public Collection<Evento> buscarEventosRealizados(boolean click, boolean listarTodos) {

		if (click) {
			// Pesquisou apenas por nome
			if (!filtroNomeEvento.isEmpty() && dataInicio == null && dataFim == null) {
				eventos = eventoFacade.filtrarEventosRealizadosByNome(filtroNomeEvento);
			}
			// Pesquisou por período
			else if (filtroNomeEvento.isEmpty() && dataInicio != null && dataFim != null) {
				eventos = eventoFacade.filtrarEventosRealizadosByData(dataInicio, dataFim);
			}
			// Pesquisou por data de Início, com nome ou sem nome
			else if ((!filtroNomeEvento.isEmpty() && dataInicio != null && dataFim == null)
					|| (filtroNomeEvento.isEmpty() && dataInicio != null && dataFim == null)) {
				eventos = eventoFacade.filtrarEventosRealizadosByDataInicio(dataInicio, filtroNomeEvento);
			}
			// Pesquisou por nome e período
			else if (!filtroNomeEvento.isEmpty() && dataInicio != null && dataFim != null) {
				eventos = eventoFacade.filtrarEventosRealizadosByNomeAndData(filtroNomeEvento, dataInicio, dataFim);
			} else {
				eventos = new ArrayList<Evento>();
			}

			// Pesquisou por data Fim com nome ou sem nome
			if ((!filtroNomeEvento.isEmpty() && dataInicio == null && dataFim != null)
					|| (filtroNomeEvento.isEmpty() && dataInicio == null && dataFim != null)) {
				// eventos = eventoFacade.filtrarEventosRealizadosByDataInicio(dataFim,
				// filtroNomeEvento);
				Clients.showNotification("Informe a data de início para a realização da busca!",
						Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
				eventos = new ArrayList<Evento>();
			} else if (eventos.isEmpty()) {
				Clients.showNotification("Não foram encontrados resultados!", Clients.NOTIFICATION_TYPE_WARNING, null,
						null, 3000, true);
			}
		} else {
			if (listarTodos) {
				eventos = eventoFacade.findEventosRealizados();
				limparFiltroDeEventosRealizados();
			} else {
				eventos = new ArrayList<Evento>();
			}

		}

		return eventos;
	}

	// Verifica se a pessoa atende ao público-alvo dessa atividade.
	public boolean verificarParticipanteEAtividade(Atividade atividade) {
		// Participante é sociedade
		if ((atividade.getTipoParticipante().equals("1") || atividade.getTipoParticipante().equals("5")
				|| atividade.getTipoParticipante().equals("6")) && (participante.getTipoPessoa().equals("1"))) {

			return true;

			// Participante é servidor tce-ma
		} else if ((atividade.getTipoParticipante().equals("2") || atividade.getTipoParticipante().equals("5")
				|| atividade.getTipoParticipante().equals("4")) && (participante.getTipoPessoa().equals("2"))) {

			return true;

			// Participante é jurisdicionado
		} else if ((atividade.getTipoParticipante().equals("3") || atividade.getTipoParticipante().equals("4")
				|| atividade.getTipoParticipante().equals("6")) && (participante.getTipoPessoa().equals("3"))) {

			return true;

			// Todos
		} else if (atividade.getTipoParticipante().equals("7")) {

			return true;
		}

		return false;
	}

	@Command
	@NotifyChange(".")
	public void aplicarFiltro() {
		try {
			inscricoes = participanteInscritoFacade
					.findInscricoesByParticipanteAndInscricaoAprovadaAndFiltro(participante, filtroEvento);
		} catch (Exception e) {
			Clients.showNotification("Erro ao realizar busca por filtro. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void limparFiltro() {
		try {
			if (!filtroEvento.getFiltro1().isEmpty() || !filtroEvento.getFiltro2().isEmpty()) {
				filtroEvento.limparFiltro();
				inscricoes = participanteInscritoFacade.findInscricoesByParticipanteAndInscricaoAprovada(participante);
			}
		} catch (Exception e) {
			Clients.showNotification("Erro ao realizar busca por filtro. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void limparFiltroDeEventosRealizados() {
		try {
			this.filtroNomeEvento = "";
			this.dataInicio = null;
			this.dataFim = null;
		} catch (Exception e) {
			Clients.showNotification("Erro ao realizar busca por filtro. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalVisualizarMaterial(@BindingParam("visible") boolean visible,
			@BindingParam("atividade") Atividade atividade) {
		try {
			if (visible) {
				if(arqUtil==null) {
					arqUtil = new ArquivoUtil();
				}
				this.atividadeSelecionada = atividade;
				this.arquivos = arquivoFacade.findByAtividade(atividade);			
			}
			modalVisualizarMaterial.setVisible(visible);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange(".")
	public void downloadArquivo(@BindingParam("arquivo") Arquivo arquivo) {
		try {
			String nomePasta = "arquivos_atividades/arquivo_atividade_id_" + arquivo.getAtividade().getAtividadeId();
			InputStream in = arqUtil.downloadArquivoFTP(arquivo.getNome(), nomePasta);
			if (in == null) {
				throw new Exception("Erro ao fazer o download do arquivo.");
			} else {
				String extensaoDoArquivo = FilenameUtils.getExtension(arquivo.getNome());
				Filedownload.save(in, "application/"+extensaoDoArquivo, arquivo.getNome());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Atividade getAtividadeSelecionada() {
		return atividadeSelecionada;
	}

	public void setAtividadeSelecionada(Atividade atividadeSelecionada) {
		this.atividadeSelecionada = atividadeSelecionada;
	}

	public Pessoa getParticipante() {
		return participante;
	}

	public void setParticipante(Pessoa participante) {
		this.participante = participante;
	}

	public ParticipanteInteresse getParticipanteInteresse() {
		return participanteInteresse;
	}

	public void setParticipanteInteresse(ParticipanteInteresse participanteInteresse) {
		this.participanteInteresse = participanteInteresse;
	}

	public ParticipanteInscrito getParticipanteInscrito() {
		return participanteInscrito;
	}

	public void setParticipanteInscrito(ParticipanteInscrito participanteInscrito) {
		this.participanteInscrito = participanteInscrito;
	}

	public Filtro getFiltroEvento() {
		return filtroEvento;
	}

	public void setFiltroEvento(Filtro filtroEvento) {
		this.filtroEvento = filtroEvento;
	}

	public String getFiltroNomeEvento() {
		return filtroNomeEvento;
	}

	public void setFiltroNomeEvento(String filtroNomeEvento) {
		this.filtroNomeEvento = filtroNomeEvento;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public boolean isEventoAberto() {
		return eventoAberto;
	}

	public void setEventoAberto(boolean hlayoutVisible) {
		this.eventoAberto = hlayoutVisible;
	}

	public String getTextoRodape() {
		return String.format(textoRodape, inscricoes.size());
	}

	public Collection<Evento> getEventos() {
		return eventos;
	}

	public void setEventos(Collection<Evento> eventos) {
		this.eventos = eventos;
	}

	public Collection<Atividade> getAtividades() {
		return atividades;
	}

	public void setAtividades(Collection<Atividade> atividades) {
		this.atividades = atividades;
	}

	public Collection<ParticipanteInscrito> getInscricoes() {
		return inscricoes;
	}

	public void setInscricoes(Collection<ParticipanteInscrito> participantesInscritos) {
		this.inscricoes = participantesInscritos;
	}

	public Collection<Arquivo> getArquivos() {
		return arquivos;
	}

	public void setArquivos(Collection<Arquivo> arquivos) {
		this.arquivos = arquivos;
	}

	public ArquivoUtil getArqUtil() {
		return arqUtil;
	}

	public void setArqUtil(ArquivoUtil arqUtil) {
		this.arqUtil = arqUtil;
	}

}