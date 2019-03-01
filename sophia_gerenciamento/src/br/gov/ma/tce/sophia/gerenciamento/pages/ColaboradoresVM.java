package br.gov.ma.tce.sophia.gerenciamento.pages;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Window;

import br.gov.ma.tce.sophia.ejb.beans.atividade.Atividade;
import br.gov.ma.tce.sophia.ejb.beans.atividade.AtividadeFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.atividadecolaborador.AtividadeColaborador;
import br.gov.ma.tce.sophia.ejb.beans.atividadecolaborador.AtividadeColaboradorFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.colaborador.Colaborador;
import br.gov.ma.tce.sophia.ejb.beans.colaborador.ColaboradorFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.evento.Evento;
import br.gov.ma.tce.sophia.ejb.beans.evento.EventoFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.formacaoacademica.FormacaoAcademica;
import br.gov.ma.tce.sophia.ejb.beans.formacaoacademica.FormacaoAcademicaFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.tipocolaborador.TipoColaborador;
import br.gov.ma.tce.sophia.ejb.beans.tipocolaborador.TipoColaboradorFacadeBean;
import br.gov.ma.tce.sophia.ejb.util.Filtro;
import br.gov.ma.tce.sophia.gerenciamento.utils.EmailSOPHIA;
import br.gov.ma.tce.sophia.gerenciamento.utils.EscolaridadeUtil;
import br.gov.ma.tce.sophia.gerenciamento.utils.FTPUtil;
import br.gov.ma.tce.sophia.gerenciamento.utils.LongOperation;

public class ColaboradoresVM {
	private Evento eventoSelecionado;
	private AtividadeColaborador colaboradorSelecionado;
	private AtividadeColaborador atividadeColaborador;
	private TipoColaborador tipoColaborador;
	private Colaborador colaboradorSelecionado2;

	private EmailSOPHIA emailSOPHIA;
	private FTPUtil ftpUtil;

	private boolean uploadCurriculo = false;
	private boolean formacaoVisible = false;
	private String statusCadastro;
	private Filtro filtroColaborador;
	private Media media;
	private static final String textoRodape = "%d colaborador(es) encontrado(s)";

	private Collection<Evento> eventos;
	private Collection<Atividade> atividades;
	private Collection<Colaborador> colaboradoresNaoAtribuidosAAtividade;
	private Collection<AtividadeColaborador> colaboradoresAtividade;
	private Collection<TipoColaborador> tiposColaboradores;
	private Collection<Colaborador> colaboradores;
	private Collection<FormacaoAcademica> formacoesAcademicas;

	private EscolaridadeUtil escolaridadeUtil;
	private Collection<String> escolaridade;

	private AtividadeColaboradorFacadeBean atividadeColaboradorFacade;
	private EventoFacadeBean eventoFacade;
	private AtividadeFacadeBean atividadeFacade;
	private ColaboradorFacadeBean colaboradorFacade;
	private TipoColaboradorFacadeBean tipoColaboradorFacade;
	private FormacaoAcademicaFacadeBean formacaoAcademicaFacade;

	@Wire("#comboEventos")
	private Combobox comboEventos;

	@Wire("#comboAtividades")
	private Combobox comboAtividades;

	@Wire("#modalAtualizarColaborador #comboEscolaridade")
	private Combobox comboEscolaridade;

	@Wire("#modalAtualizarColaborador #comboFormacoesAcademicas")
	private Combobox comboFormacoesAcademicas;

	@Wire("#modalAtribuirColaborador #comboTipoColaborador")
	private Combobox comboTipoColaborador;

	@Wire("#modalAdicionarNovoTipo")
	private Window modalAdicionarNovoTipo;

	@Wire("#modalAtualizarColaborador")
	private Window modalAtualizarColaborador;

	@Wire("#modalConfirmacao")
	private Window modalConfirmacao;

	@Wire("#modalAtribuirColaborador")
	private Window modalAtribuirColaborador;

	@Wire("#modalConfirmacaoAtribuirColaborador")
	private Window modalConfirmacaoAtribuirColaborador;

	@Wire("#modalConfirmacaoRemoverColaborador")
	private Window modalConfirmacaoRemoverColaborador;

	public ColaboradoresVM() {
		try {
			InitialContext ctx = new InitialContext();
			atividadeColaboradorFacade = (AtividadeColaboradorFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/AtividadeColaboradorFacadeBean!br.gov.ma.tce.sophia.ejb.beans.atividadecolaborador.AtividadeColaboradorFacadeBean");
			eventoFacade = (EventoFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/EventoFacadeBean!br.gov.ma.tce.sophia.ejb.beans.evento.EventoFacadeBean");
			atividadeFacade = (AtividadeFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/AtividadeFacadeBean!br.gov.ma.tce.sophia.ejb.beans.atividade.AtividadeFacadeBean");
			colaboradorFacade = (ColaboradorFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/ColaboradorFacadeBean!br.gov.ma.tce.sophia.ejb.beans.colaborador.ColaboradorFacadeBean");
			tipoColaboradorFacade = (TipoColaboradorFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/TipoColaboradorFacadeBean!br.gov.ma.tce.sophia.ejb.beans.tipocolaborador.TipoColaboradorFacadeBean");
			formacaoAcademicaFacade = (FormacaoAcademicaFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/FormacaoAcademicaFacadeBean!br.gov.ma.tce.sophia.ejb.beans.formacaoacademica.FormacaoAcademicaFacadeBean");
		} catch (NamingException e) {
			Clients.showNotification("Erro de conexão interno. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
			e.printStackTrace();
		}
	}

	@Init
	public void init() {
		atividadeColaborador = new AtividadeColaborador();
		tiposColaboradores = tipoColaboradorFacade.findAll();
		colaboradores = colaboradorFacade.findAll();
		formacoesAcademicas = formacaoAcademicaFacade.findAll();
		filtroColaborador = new Filtro();
		emailSOPHIA = new EmailSOPHIA();
		escolaridadeUtil = new EscolaridadeUtil();
		escolaridade = escolaridadeUtil.getEscolaridades();
	}

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}

	@Command
	@NotifyChange(".")
	public void carregarEventosPorCategoria(@BindingParam("categoria") String categoria) {
		try {
			colaboradoresNaoAtribuidosAAtividade = new ArrayList<Colaborador>();
			colaboradoresAtividade = new ArrayList<AtividadeColaborador>();
			eventos = eventoFacade.findEventosByCategoria(categoria);
			eventoSelecionado = null;
			comboEventos.setSelectedItem(null);
			atividades = null;
			atividadeColaborador.setAtividade(null);
			comboAtividades.setSelectedItem(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarListaPorEvento() {
		try {
			colaboradoresNaoAtribuidosAAtividade = new ArrayList<Colaborador>();
			colaboradoresAtividade = new ArrayList<AtividadeColaborador>();
			atividades = atividadeFacade.findAtividadesByEvento(eventoSelecionado);
			atividadeColaborador.setAtividade(null);
			comboAtividades.setSelectedItem(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarListaPorAtividade() {
		try {
			colaboradoresNaoAtribuidosAAtividade = colaboradorFacade.findColaboradoresByCadastroConfirmado();
			colaboradoresAtividade = atividadeColaboradorFacade
					.findColaboradoresByAtividade(atividadeColaborador.getAtividade());
			for (AtividadeColaborador ac : colaboradoresAtividade) {
				colaboradoresNaoAtribuidosAAtividade.remove(ac.getColaborador());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalAtribuirColaborador(@BindingParam("colaborador") Colaborador colaborador,
			@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				colaboradorSelecionado2 = colaborador;
				modalAtribuirColaborador.setVisible(visible);
			} else {
				atividadeColaborador.setTipoColaborador(null);
				comboTipoColaborador.setSelectedItem(null);
				modalAtribuirColaborador.setVisible(visible);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmaAtribuirColaborador(@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				atividadeColaborador.setColaborador(colaboradorSelecionado2);
				if (atividadeColaborador.getAtividade() == null || atividadeColaborador.getColaborador() == null
						|| atividadeColaborador.getTipoColaborador() == null) {
					throw new Exception("Selecione um tipo de colaborador antes de fazer a atribuição.");
				}
				modalConfirmacaoAtribuirColaborador.setVisible(visible);
			} else {
				modalConfirmacaoAtribuirColaborador.setVisible(visible);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmaRemoverColaborador(
			@BindingParam("atividadeColaborador") AtividadeColaborador atividadeColaborador,
			@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				colaboradorSelecionado = atividadeColaborador;
				modalConfirmacaoRemoverColaborador.setVisible(visible);
			} else {
				modalConfirmacaoRemoverColaborador.setVisible(visible);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void atribuirColaborador() {
		try {
			if (atividadeColaboradorFacade.include(atividadeColaborador) != null) {
				AtividadeColaborador destinatario = new AtividadeColaborador();
				destinatario.setColaborador(atividadeColaborador.getColaborador());
				destinatario.setAtividade(atividadeColaborador.getAtividade());
				destinatario.setTipoColaborador(atividadeColaborador.getTipoColaborador());
				new LongOperation() {
					@Override
					protected void execute() throws InterruptedException {
						emailSOPHIA.emailAtribuirColaborador(destinatario.getColaborador().getPessoa(),
								destinatario.getAtividade(), destinatario.getTipoColaborador(), " ");
					}
				}.start();
				carregarListaPorAtividade();
				atividadeColaborador.setAtividadeColaboradorId(null);
				atividadeColaborador.setColaborador(null);
				atividadeColaborador.setTipoColaborador(null);
				comboTipoColaborador.setSelectedItem(null);
				modalConfirmacaoAtribuirColaborador.setVisible(false);
				modalAtribuirColaborador.setVisible(false);
				Clients.showNotification("Colaborador atribuído com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null,
						null, 3000, true);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void removerColaborador() {
		try {
			atividadeColaboradorFacade.delete(colaboradorSelecionado.getAtividadeColaboradorId());
			carregarListaPorAtividade();
			modalConfirmacaoRemoverColaborador.setVisible(false);
			Clients.showNotification("Colaborador removido com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null, null,
					3000, true);
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalAdicionarNovoTipo(@BindingParam("visible") boolean visible) {
		if (visible) {
			this.tipoColaborador = new TipoColaborador();
			modalAdicionarNovoTipo.setVisible(visible);
		} else {
			modalAdicionarNovoTipo.setVisible(visible);
		}
	}

	@Command
	@NotifyChange(".")
	public void adicionarNovoTipo() {
		try {
			if (tipoColaborador.getNome() == null || tipoColaborador.getNome().equals("")) {
				throw new Exception("Informe o novo tipo de colaborador.");
			}
			if (tipoColaboradorFacade.include(tipoColaborador) != null) {
				atividadeColaborador.setTipoColaborador(tipoColaborador);
				tiposColaboradores.add(tipoColaborador);
				modalAdicionarNovoTipo.setVisible(false);
				Clients.showNotification("Novo tipo de colaborador adicionado com sucesso!",
						Clients.NOTIFICATION_TYPE_INFO, null, null, 3000, true);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void aplicarFiltro() {
		try {
			if (atividadeColaborador.getAtividade() != null) {
				colaboradoresNaoAtribuidosAAtividade = colaboradorFacade.findColaboradoresByFiltro(filtroColaborador);
				colaboradoresAtividade = atividadeColaboradorFacade
						.findColaboradoresByAtividade(atividadeColaborador.getAtividade());
				for (AtividadeColaborador ac : colaboradoresAtividade) {
					colaboradoresNaoAtribuidosAAtividade.remove(ac.getColaborador());
				}
			} else {
				colaboradores = colaboradorFacade.findColaboradoresByFiltro(filtroColaborador);
			}
		} catch (Exception e) {
			Clients.showNotification("Erro ao realizar busca por filtro. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void limparFiltro() {
		try {
			if (!filtroColaborador.getFiltro1().isEmpty() || !filtroColaborador.getFiltro2().isEmpty()) {
				filtroColaborador.limparFiltro();
				if (atividadeColaborador.getAtividade() != null) {
					colaboradoresNaoAtribuidosAAtividade = colaboradorFacade.findAll();
					colaboradoresAtividade = atividadeColaboradorFacade
							.findColaboradoresByAtividade(atividadeColaborador.getAtividade());
					for (AtividadeColaborador ac : colaboradoresAtividade) {
						colaboradoresNaoAtribuidosAAtividade.remove(ac.getColaborador());
					}
				} else {
					colaboradores = colaboradorFacade.findAll();
				}
			}
		} catch (Exception e) {
			Clients.showNotification("Erro ao realizar busca por filtro. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void downloadCurriculo(@BindingParam("colaborador") Colaborador colaborador) {
		try {
			if (ftpUtil == null) {
				ftpUtil = new FTPUtil();
			}
			String nomeArquivo = "curriculo_colaborador_id_" + colaborador.getColaboradorId() + ".pdf";
			String nomePasta = "curriculos_colaboradores/curriculo_colaborador_id_" + colaborador.getColaboradorId();
			InputStream in = ftpUtil.downloadArquivo2(nomeArquivo, nomePasta);
			if (in == null) {
				throw new Exception("Colaborador não realizou upload de currículo.");
			} else {
				Executions.getCurrent().sendRedirect(
						"ftp://escex:escex!@123@ftp.tce.ma.gov.br:10021/" + nomePasta + "/" + nomeArquivo, "_blank");
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void uploadCurriculo(@BindingParam("upEvent") UploadEvent event) {
		try {
			media = event.getMedia();
			uploadCurriculo = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalAtualizarColaborador(@BindingParam("colaborador") Colaborador colaborador,
			@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				colaboradorSelecionado2 = colaborador;
				statusCadastro = colaboradorSelecionado2.getStatusCadastro();
				if (escolaridadeUtil.showFormacaoAcademica(this.colaboradorSelecionado2.getEscolaridade())) {
					this.formacaoVisible = true;
				} else {
					this.formacaoVisible = false;
				}
				modalAtualizarColaborador.setVisible(visible);
			} else {
				modalAtualizarColaborador.setVisible(visible);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmaAtualizarColaborador(@BindingParam("visible") boolean visible) {
		try {
			modalConfirmacao.setVisible(visible);
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	// Inserido por Francisco
	// Verificar necessidade
	@Command
	@NotifyChange(".")
	public void carregarListaPorEscolaridade() {
		try {
			comboFormacoesAcademicas.setSelectedIndex(-1);
			if (escolaridadeUtil.showFormacaoAcademica(this.colaboradorSelecionado2.getEscolaridade())) {
				formacoesAcademicas = escolaridadeUtil
						.getFormacoesAcademicas(this.colaboradorSelecionado2.getEscolaridade());
				System.out.println("TESTE");

				this.formacaoVisible = true;
			} else {
				this.formacaoVisible = false;
			}
		} catch (Exception e) {
			System.out.println("Não foi possível carregar as formacoes academicas");
		}
	}

	@Command
	@NotifyChange(".")
	public void atualizarColaborador() {
		try {
			if (colaboradorSelecionado2.getEscolaridade().equals("ENSINO FUNDAMENTAL")
					|| colaboradorSelecionado2.getEscolaridade().equals("ENSINO MÉDIO")) {
				colaboradorSelecionado2.setFormacaoAcademica(null);
			}
			if (colaboradorFacade.update(colaboradorSelecionado2) != null) {
				try {
					if (ftpUtil == null) {
						ftpUtil = new FTPUtil();
					}
					if (media != null) {
						ftpUtil.upload2(media,
								"curriculo_colaborador_id_" + colaboradorSelecionado2.getColaboradorId() + ".pdf",
								"curriculos_colaboradores/curriculo_colaborador_id_"
										+ colaboradorSelecionado2.getColaboradorId());
					}
				} catch (Exception e) {
					Clients.showNotification(
							"Ocorreu um erro inesperado ao fazer o upload do arquivo de currículo. Tente novamente mais tarde.",
							Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
				}

				// Envia e-mail caso o status do colaborador tenha sido alterado
				if (!statusCadastro.equals(colaboradorSelecionado2.getStatusCadastro())) {
					Colaborador destinatario = new Colaborador();
					destinatario.setPessoa(colaboradorSelecionado2.getPessoa());
					if (colaboradorSelecionado2.getStatusCadastro().equals("2")) {
						new LongOperation() {
							@Override
							protected void execute() throws InterruptedException {
								emailSOPHIA.emailConfirmarCadastroColaborador(destinatario.getPessoa(), " ");
							}
						}.start();
					} else {
						new LongOperation() {
							@Override
							protected void execute() throws InterruptedException {
								emailSOPHIA.emailDesativarCadastroColaborador(destinatario.getPessoa(), " ");
							}
						}.start();
					}
				}

				media = null;
				modalConfirmacao.setVisible(false);
				modalAtualizarColaborador.setVisible(false);
				Clients.showNotification("Colaborador atualizado com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null,
						null, 3000, true);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	public Evento getEventoSelecionado() {
		return eventoSelecionado;
	}

	public void setEventoSelecionado(Evento eventoSelecionado) {
		this.eventoSelecionado = eventoSelecionado;
	}

	public AtividadeColaborador getColaboradorSelecionado() {
		return colaboradorSelecionado;
	}

	public void setColaboradorSelecionado(AtividadeColaborador colaboradorSelecionado) {
		this.colaboradorSelecionado = colaboradorSelecionado;
	}

	public AtividadeColaborador getAtividadeColaborador() {
		return atividadeColaborador;
	}

	public void setAtividadeColaborador(AtividadeColaborador atividadeColaborador) {
		this.atividadeColaborador = atividadeColaborador;
	}

	public TipoColaborador getTipoColaborador() {
		return tipoColaborador;
	}

	public void setTipoColaborador(TipoColaborador tipoColaborador) {
		this.tipoColaborador = tipoColaborador;
	}

	public Colaborador getColaboradorSelecionado2() {
		return colaboradorSelecionado2;
	}

	public void setColaboradorSelecionado2(Colaborador colaboradorSelecionado2) {
		this.colaboradorSelecionado2 = colaboradorSelecionado2;
	}

	public boolean isUploadCurriculo() {
		return uploadCurriculo;
	}

	public void setUploadCurriculo(boolean uploadCurriculo) {
		this.uploadCurriculo = uploadCurriculo;
	}

	public boolean isFormacaoVisible() {
		return formacaoVisible;
	}

	public void setFormacaoVisible(boolean formacaoVisible) {
		this.formacaoVisible = formacaoVisible;
	}

	public Filtro getFiltroColaborador() {
		return filtroColaborador;
	}

	public void setFiltroColaborador(Filtro filtroColaborador) {
		this.filtroColaborador = filtroColaborador;
	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	public String getTextoRodape() {
		return String.format(textoRodape, colaboradores.size());
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

	public Collection<Colaborador> getColaboradoresNaoAtribuidosAAtividade() {
		return colaboradoresNaoAtribuidosAAtividade;
	}

	public void setColaboradoresNaoAtribuidosAAtividade(Collection<Colaborador> colaboradoresNaoAtribuidosAAtividade) {
		this.colaboradoresNaoAtribuidosAAtividade = colaboradoresNaoAtribuidosAAtividade;
	}

	public Collection<AtividadeColaborador> getColaboradoresAtividade() {
		return colaboradoresAtividade;
	}

	public void setColaboradoresAtividade(Collection<AtividadeColaborador> colaboradoresAtividade) {
		this.colaboradoresAtividade = colaboradoresAtividade;
	}

	public Collection<TipoColaborador> getTiposColaboradores() {
		return tiposColaboradores;
	}

	public void setTiposColaboradores(Collection<TipoColaborador> tiposColaboradores) {
		this.tiposColaboradores = tiposColaboradores;
	}

	public Collection<Colaborador> getColaboradores() {
		return colaboradores;
	}

	public void setColaboradores(Collection<Colaborador> colaboradores) {
		this.colaboradores = colaboradores;
	}

	public Collection<FormacaoAcademica> getFormacoesAcademicas() {
		return formacoesAcademicas;
	}

	public void setFormacoesAcademicas(Collection<FormacaoAcademica> formacoesAcademicas) {
		this.formacoesAcademicas = formacoesAcademicas;
	}

	public Collection<String> getEscolaridade() {
		return escolaridade;
	}

	public void setEscolaridade(Collection<String> escolaridade) {
		this.escolaridade = escolaridade;
	}
}