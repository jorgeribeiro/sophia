package br.gov.ma.tce.sophia.gerenciamento.pages;

import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Window;

import br.com.caelum.stella.inwords.InteiroSemFormato;
import br.com.caelum.stella.inwords.NumericToWordsConverter;
import br.gov.ma.tce.sophia.ejb.beans.atividade.Atividade;
import br.gov.ma.tce.sophia.ejb.beans.atividade.AtividadeFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.atividadecolaborador.AtividadeColaborador;
import br.gov.ma.tce.sophia.ejb.beans.atividadecolaborador.AtividadeColaboradorFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.evento.Evento;
import br.gov.ma.tce.sophia.ejb.beans.evento.EventoFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.fundocertificado.FundoCertificado;
import br.gov.ma.tce.sophia.ejb.beans.fundocertificado.FundoCertificadoFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.modelocertificado.ModeloCertificado;
import br.gov.ma.tce.sophia.ejb.beans.modelocertificado.ModeloCertificadoFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscrito;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscritoFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.tokencertificado.TokenCertificado;
import br.gov.ma.tce.sophia.ejb.beans.tokencertificado.TokenCertificadoFacadeBean;
import br.gov.ma.tce.sophia.ejb.util.Filtro;
import br.gov.ma.tce.sophia.gerenciamento.utils.EmailSOPHIA;
import br.gov.ma.tce.sophia.gerenciamento.utils.FTPUtil;
import br.gov.ma.tce.sophia.gerenciamento.utils.GeradorDeToken;
import br.gov.ma.tce.sophia.gerenciamento.utils.LongOperation;
import br.gov.ma.tce.sophia.gerenciamento.utils.Report;

public class CertificadosVM {
	private Atividade atividadeSelecionada;
	private Evento eventoSelecionado;
	private ModeloCertificado modeloCertificado, copiaModeloCertificado;
	private FundoCertificado fundoCertificado;
	private TokenCertificado tokenCertificado;
	private ParticipanteInscrito certificadoParticipante;
	private AtividadeColaborador certificadoColaborador;
	private Filtro filtro;

	private EmailSOPHIA emailSOPHIA;
	private FTPUtil ftpUtil;

	private File certificado;
	private boolean criandoModeloDeCertificado = false;
	private boolean vlayoutVisible = false, modeloJaExistente = false;
	private String tipo, textoPersonalizado;
	private String textoPadraoParticipante = "Certifico que [PARTICIPANTE] participou da atividade "
			+ "[ATIVIDADE], parte do evento [EVENTO], promovido "
			+ "pelo Tribunal de Contas do Estado do Maranhão, por meio da Escola "
			+ "Superior de Controle Externo - ESCEX, realizado no [PERIODO] " + "com carga horária de [CARGA].";
	private String textoPadraoColaborador = "Certifico que [COLABORADOR] colaborou como [TIPO] na atividade "
			+ "[ATIVIDADE], parte do evento [EVENTO], promovido "
			+ "pelo Tribunal de Contas do Estado do Maranhão, por meio da Escola "
			+ "Superior de Controle Externo - ESCEX, realizado no [PERIODO] " + "com carga horária de [CARGA].";
	private static final String textoRodape1 = "%d participante(s) encontrado(s)";
	private static final String textoRodape2 = "%d colaborador(es) encontrado(s)";
	private Media media;

	private Collection<Evento> eventos;
	private Collection<Atividade> atividades;
	private Collection<ParticipanteInscrito> participantesInscritos;
	private Collection<ParticipanteInscrito> participantesInscritosComAlteracao;
	private Collection<AtividadeColaborador> atividadeColaboradores;
	private Collection<AtividadeColaborador> atividadeColaboradoresComAlteracao;
	private Collection<FundoCertificado> fundosDeCertificado;

	private AtividadeFacadeBean atividadeFacade;
	private EventoFacadeBean eventoFacade;
	private ParticipanteInscritoFacadeBean participanteInscritoFacade;
	private AtividadeColaboradorFacadeBean atividadeColaboradorFacade;
	private ModeloCertificadoFacadeBean modeloCertificadoFacade;
	private FundoCertificadoFacadeBean fundoCertificadoCertificadoFacade;
	private TokenCertificadoFacadeBean tokenCertificadoFacade;

	@Wire("#comboEventos")
	private Combobox comboEventos;

	@Wire("#comboAtividades")
	private Combobox comboAtividades;

	@Wire("#comboTipos")
	private Combobox comboTipos;

	@Wire("#modalSelecionaFundoDoCertificado")
	private Window modalSelecionaFundoDoCertificado;

	@Wire("#modalConfirmacao")
	private Window modalConfirmacao;

	@Wire("#modalVisualizaFundoDoCertificado")
	private Window modalVisualizaFundoDoCertificado;

	@Wire("#modalVisualizaFundoDoCertificado #iframeCertificado")
	private Iframe frameCertificado;

	@Wire("#modalVisualizaModeloDoCertificado #frameModeloCertificado")
	private Iframe frameModeloCertificado;

	@Wire("#modalEnviarNovoTemplate")
	private Window modalEnviarNovoTemplate;

	@Wire("#modalConfirmacaoUpload")
	private Window modalConfirmacaoUpload;

	@Wire("#modalVisualizaModeloDoCertificado")
	private Window modalVisualizaModeloDoCertificado;

	@Wire("#radioTextoCertificadoParticipante")
	private Radiogroup radioTextoCertificadoParticipante;

	@Wire("#radioTextoCertificadoColaborador")
	private Radiogroup radioTextoCertificadoColaborador;

	public CertificadosVM() {
		try {
			InitialContext ctx = new InitialContext();
			atividadeFacade = (AtividadeFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/AtividadeFacadeBean!br.gov.ma.tce.sophia.ejb.beans.atividade.AtividadeFacadeBean");
			eventoFacade = (EventoFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/EventoFacadeBean!br.gov.ma.tce.sophia.ejb.beans.evento.EventoFacadeBean");
			participanteInscritoFacade = (ParticipanteInscritoFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/ParticipanteInscritoFacadeBean!br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscritoFacadeBean");
			atividadeColaboradorFacade = (AtividadeColaboradorFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/AtividadeColaboradorFacadeBean!br.gov.ma.tce.sophia.ejb.beans.atividadecolaborador.AtividadeColaboradorFacadeBean");
			modeloCertificadoFacade = (ModeloCertificadoFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/ModeloCertificadoFacadeBean!br.gov.ma.tce.sophia.ejb.beans.modelocertificado.ModeloCertificadoFacadeBean");
			fundoCertificadoCertificadoFacade = (FundoCertificadoFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/FundoCertificadoFacadeBean!br.gov.ma.tce.sophia.ejb.beans.fundocertificado.FundoCertificadoFacadeBean");
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
		participantesInscritos = new ArrayList<ParticipanteInscrito>();
		participantesInscritosComAlteracao = new ArrayList<ParticipanteInscrito>();
		atividadeColaboradores = new ArrayList<AtividadeColaborador>();
		atividadeColaboradoresComAlteracao = new ArrayList<AtividadeColaborador>();
		filtro = new Filtro();
		emailSOPHIA = new EmailSOPHIA();
	}

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}

	@Command
	@NotifyChange(".")
	public void setCriandoModelo() {
		criandoModeloDeCertificado = true;
		modeloCertificado = new ModeloCertificado();
	}

	@Command
	@NotifyChange(".")
	public void carregarEventosPorCategoria(@BindingParam("categoria") String categoria) {
		try {
			eventos = eventoFacade.findEventosByCategoria(categoria);
			eventoSelecionado = null;
			atividadeSelecionada = null;
			comboEventos.setSelectedItem(null);
			comboAtividades.setSelectedItem(null);
			comboTipos.setSelectedItem(null);
			vlayoutVisible = false;
			modeloJaExistente = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarListaPorEvento() {
		try {
			atividades = atividadeFacade.findAtividadesByEvento(eventoSelecionado);
			tipo = null;
			atividadeSelecionada = null;
			comboAtividades.setSelectedItem(null);
			comboTipos.setSelectedItem(null);
			vlayoutVisible = false;
			modeloJaExistente = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarListaPorAtividade() {
		try {
			tipo = null;
			comboTipos.setSelectedItem(null);
			vlayoutVisible = false;
			modeloJaExistente = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarListaPorTipo(@BindingParam("tipo") String tipo) {
		try {
			this.tipo = tipo;
			vlayoutVisible = false;
			modeloJaExistente = false;
			textoPersonalizado = null;
			participantesInscritos = new ArrayList<ParticipanteInscrito>();
			participantesInscritosComAlteracao = new ArrayList<ParticipanteInscrito>();
			atividadeColaboradores = new ArrayList<AtividadeColaborador>();
			atividadeColaboradoresComAlteracao = new ArrayList<AtividadeColaborador>();

			if (tipo.equals("PARTICIPANTE")) {
				participantesInscritos = participanteInscritoFacade
						.findParticipantesByAtividadeAndInscricaoAprovadaAndCertificadoDisponivel(atividadeSelecionada,
								true, true);
			} else {
				atividadeColaboradores = atividadeColaboradorFacade
						.findColaboradoresByAtividadeAndCertificadoDisponivel(atividadeSelecionada, true);
			}
			vlayoutVisible = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void verificaModeloCertificadoJaExistente(@BindingParam("tipo") String tipo) {
		try {
			this.tipo = tipo;
			vlayoutVisible = false;
			modeloJaExistente = false;
			textoPersonalizado = null;
			participantesInscritos = new ArrayList<ParticipanteInscrito>();
			participantesInscritosComAlteracao = new ArrayList<ParticipanteInscrito>();
			atividadeColaboradores = new ArrayList<AtividadeColaborador>();
			atividadeColaboradoresComAlteracao = new ArrayList<AtividadeColaborador>();

			if ((modeloCertificado = modeloCertificadoFacade
					.findModeloCertificadoByAtividadeAndTipo(atividadeSelecionada, tipo)) != null) {
				modeloJaExistente = true;
			} else if (criandoModeloDeCertificado) {
				modeloCertificado = new ModeloCertificado();
				modeloCertificado.setDataCriacao(new Date());
				modeloCertificado.setFundoCertificado(null);
				modeloCertificado.setTipo(tipo);
				modeloCertificado.setAtividade(atividadeSelecionada);
				if (tipo.equals("PARTICIPANTE")) {
					modeloCertificado.setTexto(textoPadraoParticipante);
				} else {
					modeloCertificado.setTexto(textoPadraoColaborador);
				}
				radioTextoCertificadoParticipante.setSelectedIndex(0);
				radioTextoCertificadoColaborador.setSelectedIndex(0);
				gerenciarTextoCertificado();
				vlayoutVisible = true;
			}
			if (modeloJaExistente && !criandoModeloDeCertificado) {
				if (tipo.equals("PARTICIPANTE")) {
					participantesInscritos = participanteInscritoFacade
							.findParticipantesByAtividadeAndInscricaoAprovadaAndCertificadoDisponivel(
									atividadeSelecionada, true, false);
				} else {
					atividadeColaboradores = atividadeColaboradorFacade
							.findColaboradoresByAtividadeAndCertificadoDisponivel(atividadeSelecionada, false);
				}
				vlayoutVisible = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void novoModeloCertificado() {
		try {
			modeloCertificado.setDataCriacao(new Date());
			modeloCertificado.setFundoCertificado(null);
			modeloCertificado.setTipo(tipo);
			modeloCertificado.setAtividade(atividadeSelecionada);
			gerenciarTextoCertificado();
			modeloJaExistente = false;
			vlayoutVisible = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void gerenciarTextoCertificado() {
		try {
			if (tipo.equals("PARTICIPANTE") && radioTextoCertificadoParticipante.getSelectedIndex() == 0) {
				textoPadraoParticipante = "Certifico que [PARTICIPANTE] participou da atividade "
						+ "[ATIVIDADE], parte do evento [EVENTO], promovido "
						+ "pelo Tribunal de Contas do Estado do Maranhão, por meio da Escola "
						+ "Superior de Controle Externo - ESCEX, realizado no [PERIODO] "
						+ "com carga horária de [CARGA].";
			} else if (tipo.equals("COLABORADOR") && radioTextoCertificadoColaborador.getSelectedIndex() == 0) {
				textoPadraoColaborador = "Certifico que [COLABORADOR] colaborou como [TIPO] na atividade "
						+ "[ATIVIDADE], parte do evento [EVENTO], promovido "
						+ "pelo Tribunal de Contas do Estado do Maranhão, por meio da Escola "
						+ "Superior de Controle Externo - ESCEX, realizado no [PERIODO] "
						+ "com carga horária de [CARGA].";
			} else {
				modeloCertificado.setTexto(null);
				textoPadraoParticipante = null;
				textoPadraoColaborador = null;
			}

			if (tipo.equals("PARTICIPANTE")) {
				modeloCertificado.setTexto(textoPadraoParticipante);
			} else {
				modeloCertificado.setTexto(textoPadraoColaborador);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalSelecionaFundoDoCertificado(@BindingParam("visible") Boolean visible) {
		try {
			if (visible) {
				if (fundosDeCertificado == null) {
					fundosDeCertificado = fundoCertificadoCertificadoFacade.findAll();
				}
				modalSelecionaFundoDoCertificado.setVisible(visible);
			} else {
				modalSelecionaFundoDoCertificado.setVisible(visible);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalVisualizaFundoDoCertificado(
			@BindingParam("fundoCertificado") FundoCertificado fundoCertificado,
			@BindingParam("visible") Boolean visible) {
		try {
			if (visible) {
				if (frameCertificado.getContent() == null) {
					if (ftpUtil == null) {
						ftpUtil = new FTPUtil();
					}
					InputStream in = ftpUtil.downloadArquivo2(fundoCertificado.getNomeFtp(), "fundos_certificado");
					String path = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
					Map<String, Object> parameters = new HashMap<String, Object>();
					String data = DateFormat.getDateInstance(DateFormat.LONG).format(new Date());
					parameters.put("DATA_CERTIFICADO", data);
					parameters.put("TEXTO_CERTIFICADO", null);
					parameters.put("TOKEN_VERIFICACAO", "5ad32w450cs9e5t58b");
					parameters.put("IMAGE_DIR", path + "/imagens");

					certificado = Report.getReportTemplateCertificado(in, parameters);
					frameCertificado.setContent(new AMedia(certificado, "application/pdf", null));
				}
				modalVisualizaFundoDoCertificado.setVisible(visible);
			} else {
				certificado = null;
				frameCertificado.setContent(null);
				modalVisualizaFundoDoCertificado.setVisible(visible);
			}
		} catch (Exception e) {
			Clients.showNotification("Ocorreu um erro ao realizar o download do template. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalEnviarNovoTemplate(@BindingParam("visible") Boolean visible) {
		try {
			if (visible) {
				media = null;
				fundoCertificado = new FundoCertificado();
				modalEnviarNovoTemplate.setVisible(visible);
			} else {
				modalEnviarNovoTemplate.setVisible(visible);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void uploadArquivo(@BindingParam("upEvent") UploadEvent event) {
		try {
			media = event.getMedia();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmaUpload(@BindingParam("visible") Boolean visible) {
		try {
			if (visible) {
				if ((fundoCertificado.getNome() == null || fundoCertificado.getNome().equals("")) || media == null) {
					throw new Exception("Todos os campos obrigatórios precisam ser preechidos corretamente.");
				}
				String arquivoNome = Normalizer.normalize(media.getName(), Normalizer.Form.NFD)
						.replaceAll("[^\\p{ASCII}]", "");
				fundoCertificado.setNomeFtp(arquivoNome);
				modalConfirmacaoUpload.setVisible(visible);
			} else {
				modalConfirmacaoUpload.setVisible(visible);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void uploadTemplate() {
		try {
			try {
				if (ftpUtil == null) {
					ftpUtil = new FTPUtil();
				}
				if (media != null) {
					String arquivoNome = Normalizer.normalize(media.getName(), Normalizer.Form.NFD)
							.replaceAll("[^\\p{ASCII}]", "");
					ftpUtil.upload2(media, arquivoNome, "fundos_certificado");
				}
			} catch (Exception e) {
				Clients.showNotification(
						"Ocorreu um erro inesperado ao fazer o upload do arquivo de template. Tente novamente mais tarde.",
						Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
				modalConfirmacaoUpload.setVisible(false);
				return;
			}
			fundoCertificadoCertificadoFacade.include(fundoCertificado);
			fundosDeCertificado.add(fundoCertificado);
			media = null;
			modalConfirmacaoUpload.setVisible(false);
			modalEnviarNovoTemplate.setVisible(false);
			Clients.showNotification("Upload realizado com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null, null, 3000,
					true);
		} catch (Exception e) {
			Clients.showNotification(
					"Ocorreu um erro inesperado ao enviar o arquivo de template. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void selecionarTemplateDoCertificado(@BindingParam("fundoCertificado") FundoCertificado fundoCertificado) {
		try {
			modeloCertificado.setFundoCertificado(fundoCertificado);
			modalSelecionaFundoDoCertificado.setVisible(false);
			Clients.showNotification("Template selecionado com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null, null,
					3000, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalVisualizaModeloPDF(@BindingParam("visible") Boolean visible) {
		try {
			if (visible) {
				if (tipo.equals("PARTICIPANTE") && radioTextoCertificadoParticipante.getSelectedIndex() == 1
						&& (textoPersonalizado == null || textoPersonalizado.equals(""))) {
					throw new Exception("Escreva o texto a ser inserido no certificado de PARTICIPANTE.");
				} else if (tipo.equals("COLABORADOR") && radioTextoCertificadoColaborador.getSelectedIndex() == 1
						&& (textoPersonalizado == null || textoPersonalizado.equals(""))) {
					throw new Exception("Escreva o texto a ser inserido no certificado de COLABORADOR.");
				} else if (modeloCertificado.getFundoCertificado() == null) {
					throw new Exception("Selecione um template para o certificado.");
				} else {
					substituiTags();
					if (ftpUtil == null) {
						ftpUtil = new FTPUtil();
					}
					InputStream in = ftpUtil.downloadArquivo2(modeloCertificado.getFundoCertificado().getNomeFtp(),
							"fundos_certificado");
					String path = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
					Map<String, Object> parameters = new HashMap<String, Object>();
					String data = DateFormat.getDateInstance(DateFormat.LONG).format(new Date());
					parameters.put("DATA_CERTIFICADO", data);
					parameters.put("TEXTO_CERTIFICADO", modeloCertificado.getTexto());
					parameters.put("TOKEN_VERIFICACAO", "5ad32w450cs9e5t58b");
					parameters.put("IMAGE_DIR", path + "/imagens");

					certificado = Report.getReportTemplateCertificado(in, parameters);
					AMedia pdf = new AMedia(certificado, "application/pdf", null);
					if (frameModeloCertificado.getContent() == null) {
						frameModeloCertificado.setContent(pdf);
					}
					modalVisualizaModeloDoCertificado.setVisible(visible);
				}
			} else {
				frameModeloCertificado.setContent(null);
				modalVisualizaModeloDoCertificado.setVisible(visible);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmaAlteracoes(@BindingParam("visible") Boolean visible) {
		try {
			if (visible) {
				if (criandoModeloDeCertificado) {
					if (tipo.equals("PARTICIPANTE") && radioTextoCertificadoParticipante.getSelectedIndex() == 1
							&& (textoPersonalizado == null || textoPersonalizado.equals(""))) {
						throw new Exception("Escreva o texto a ser inserido no certificado de PARTICIPANTE.");
					} else if (tipo.equals("COLABORADOR") && radioTextoCertificadoColaborador.getSelectedIndex() == 1
							&& (textoPersonalizado == null || textoPersonalizado.equals(""))) {
						throw new Exception("Escreva o texto a ser inserido no certificado de COLABORADOR.");
					} else if (modeloCertificado.getFundoCertificado() == null) {
						throw new Exception("Selecione um template para o certificado.");
					}
				}
				modalConfirmacao.setVisible(visible);
			} else {
				modalConfirmacao.setVisible(visible);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void criarModeloDeCertificado() {
		try {
			substituiTags();
			if (modeloCertificadoFacade.update(modeloCertificado) != null) {
				if (tipo.equals("PARTICIPANTE")) {
					Clients.showNotification("Modelo de certificado de PARTICIPANTE criado com sucesso!",
							Clients.NOTIFICATION_TYPE_INFO, null, null, 3000, true);
				} else {
					Clients.showNotification("Modelo de certificado de COLABORADOR criado com sucesso!",
							Clients.NOTIFICATION_TYPE_INFO, null, null, 3000, true);
				}
				vlayoutVisible = false;
				modeloJaExistente = true;
				modalConfirmacao.setVisible(false);
			}
		} catch (Exception e) {
			Clients.showNotification(
					"Ocorreu um erro ao criar o modelo de certificado para a atividade selecionada. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	private void substituiTags() {
		// Remoção de espaços duplos no texto do certificado
		if (radioTextoCertificadoParticipante.getSelectedIndex() == 1
				|| radioTextoCertificadoColaborador.getSelectedIndex() == 1) {
			modeloCertificado.setTexto(textoPersonalizado);
		}
		modeloCertificado.setTexto(modeloCertificado.getTexto().replaceAll("\\s+", " ").trim());

		// Substituição de tags no texto do certificado
		HashMap<String, String> substituicoes = new HashMap<String, String>();
		substituicoes.put("[PARTICIPANTE]", "[PARTICIPANTE]");
		substituicoes.put("[COLABORADOR]", "[COLABORADOR]");
		substituicoes.put("[TIPO]", "[TIPO]");
		substituicoes.put("[ATIVIDADE]", atividadeSelecionada.getNome());
		substituicoes.put("[EVENTO]", atividadeSelecionada.getEvento().getNome());
		substituicoes.put("[PERIODO]", atividadeSelecionada.getPeriodoParaCertificadoStr());
		substituicoes.put("[CARGA]", formataCargaHoraria());

		Pattern pattern = Pattern.compile("\\[(.+?)\\]");
		Matcher matcher = pattern.matcher(modeloCertificado.getTexto());
		StringBuilder texto = new StringBuilder();
		int i = 0;
		while (matcher.find()) {
			String paraSubstituir = substituicoes.get(matcher.group(0));
			texto.append(modeloCertificado.getTexto().substring(i, matcher.start()));
			if (paraSubstituir == null) {
				texto.append(matcher.group(1));
			} else {
				texto.append(paraSubstituir);
			}
			i = matcher.end();
		}
		texto.append(modeloCertificado.getTexto().substring(i, modeloCertificado.getTexto().length()));
		modeloCertificado.setTexto(texto.toString());
	}

	private String formataCargaHoraria() {
		String cargaHorariaStr = "";
		int quantidadeHoras = atividadeSelecionada.getCargaHorariaHorasInt();
		int quantidadeMinutos = atividadeSelecionada.getCargaHorariaMinutosInt();
		NumericToWordsConverter converter = new NumericToWordsConverter(new InteiroSemFormato());

		if (quantidadeHoras > 0) {
			if (quantidadeHoras == 1) {
				cargaHorariaStr += "1 (uma) hora ";
			} else if (quantidadeHoras == 2) {
				cargaHorariaStr += "2 (duas) horas ";
			} else {
				cargaHorariaStr += quantidadeHoras + " (" + converter.toWords(quantidadeHoras) + ") horas ";
			}
		}

		if (quantidadeMinutos > 0) {
			if (quantidadeMinutos == 1) {
				if (cargaHorariaStr.equals("")) {
					cargaHorariaStr += quantidadeMinutos + " (" + converter.toWords(quantidadeMinutos) + ") minuto";
				} else {
					cargaHorariaStr += "e " + quantidadeMinutos + " (" + converter.toWords(quantidadeMinutos)
							+ ") minuto";
				}
			} else {
				if (cargaHorariaStr.equals("")) {
					cargaHorariaStr += quantidadeMinutos + " (" + converter.toWords(quantidadeMinutos) + ") minutos";
				} else {
					cargaHorariaStr += "e " + quantidadeMinutos + " (" + converter.toWords(quantidadeMinutos)
							+ ") minutos";
				}
			}
		}

		return cargaHorariaStr.trim();
	}

	private void substituiNome(ParticipanteInscrito pi, AtividadeColaborador ac) {
		// Remoção de espaços duplos no texto do certificado
		// Utiliza a cópia pra manter a tag de nome
		copiaModeloCertificado.setTexto(copiaModeloCertificado.getTexto().replaceAll("\\s+", " ").trim());

		// Substituição de tags no texto do certificado
		HashMap<String, String> substituicoes = new HashMap<String, String>();
		if (pi != null) {
			substituicoes.put("[PARTICIPANTE]", pi.getParticipante().getNome());
		} else if (ac != null) {
			substituicoes.put("[COLABORADOR]", ac.getColaborador().getPessoa().getNome());
			substituicoes.put("[TIPO]", ac.getTipoColaborador().getNome());
		}

		Pattern pattern = Pattern.compile("\\[(.+?)\\]");
		Matcher matcher = pattern.matcher(copiaModeloCertificado.getTexto());
		StringBuilder texto = new StringBuilder();
		int i = 0;
		while (matcher.find()) {
			String paraSubstituir = substituicoes.get(matcher.group(0));
			texto.append(copiaModeloCertificado.getTexto().substring(i, matcher.start()));
			if (paraSubstituir == null) {
				texto.append(matcher.group(1));
			} else {
				texto.append(paraSubstituir);
			}
			i = matcher.end();
		}
		texto.append(copiaModeloCertificado.getTexto().substring(i, copiaModeloCertificado.getTexto().length()));
		copiaModeloCertificado.setTexto(texto.toString());
	}

	@Command
	@NotifyChange(".")
	public void disponibilizarCertificado(
			@BindingParam("participanteInscrito") ParticipanteInscrito participanteInscrito,
			@BindingParam("atividadeColaborador") AtividadeColaborador atividadeColaborador) {
		try {
			if (participanteInscrito != null) {
				participanteInscrito.setCertificadoDisponivel(!participanteInscrito.getCertificadoDisponivel());
				if (!participantesInscritosComAlteracao.contains(participanteInscrito)) {
					participantesInscritosComAlteracao.add(participanteInscrito);
				}
			} else {
				atividadeColaborador.setCertificadoDisponivel(!atividadeColaborador.getCertificadoDisponivel());
				if (!atividadeColaboradoresComAlteracao.contains(atividadeColaborador)) {
					atividadeColaboradoresComAlteracao.add(atividadeColaborador);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void disponibilizarCertificados() {
		try {
			if (!participantesInscritos.isEmpty()) {
				boolean status = participantesInscritos.iterator().next().getCertificadoDisponivel() ? false : true;
				for (ParticipanteInscrito pi : participantesInscritos) {
					if (pi.getFrequencia() >= 75.0) {
						pi.setCertificadoDisponivel(status);
						if (!participantesInscritosComAlteracao.contains(pi)) {
							participantesInscritosComAlteracao.add(pi);
						}
					}
				}
			} else if (!atividadeColaboradores.isEmpty()) {
				boolean status = atividadeColaboradores.iterator().next().getCertificadoDisponivel() ? false : true;
				for (AtividadeColaborador ac : atividadeColaboradores) {
					ac.setCertificadoDisponivel(status);
					if (!atividadeColaboradoresComAlteracao.contains(ac)) {
						atividadeColaboradoresComAlteracao.add(ac);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void confirmarAlteracoes() {
		try {
			if (!participantesInscritosComAlteracao.isEmpty()) {
				for (ParticipanteInscrito pi : participantesInscritosComAlteracao) {
					copiaModeloCertificado = ModeloCertificado.copiaModeloCertificado(modeloCertificado);
					if (pi.getCertificadoDisponivel()) {
						if (participanteInscritoFacade.update(pi) != null) {
							if (ftpUtil == null) {
								ftpUtil = new FTPUtil();
							}
							substituiNome(pi, null);
							String path = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
							Map<String, Object> parameters = new HashMap<String, Object>();
							String data = DateFormat.getDateInstance(DateFormat.LONG).format(new Date());
							parameters.put("DATA_CERTIFICADO", data);
							parameters.put("TEXTO_CERTIFICADO", copiaModeloCertificado.getTexto());
							parameters.put("TOKEN_VERIFICACAO", criaNovoToken(pi, null).getToken());
							parameters.put("IMAGE_DIR", path + "/imagens");

							InputStream in = ftpUtil.downloadArquivo2(
									modeloCertificado.getFundoCertificado().getNomeFtp(), "fundos_certificado");
							certificado = Report.getReportCertificado(in, parameters);
							String nomeArquivo = "certificado_participante_" + pi.getAtividade().getNomeFormatado()
									+ "_" + pi.getParticipante().getNomeFormatado() + ".pdf";
							String nomePasta = "certificados_participantes/atividade_id_"
									+ pi.getAtividade().getAtividadeId();
							ftpUtil.upload2(new AMedia(certificado, "application/pdf", null), nomeArquivo, nomePasta);
							tokenCertificadoFacade.include(tokenCertificado);
							participantesInscritos.remove(pi);
						}
					}
				}

				ArrayList<ParticipanteInscrito> destinatarios = new ArrayList<ParticipanteInscrito>();
				destinatarios.addAll(participantesInscritosComAlteracao);
				new LongOperation() {
					@Override
					protected void execute() throws InterruptedException {
						emailSOPHIA.emailCertificadoParticipanteDisponivel(destinatarios, " ");
					}
				}.start();

			} else if (!atividadeColaboradoresComAlteracao.isEmpty()) {
				for (AtividadeColaborador ac : atividadeColaboradoresComAlteracao) {
					copiaModeloCertificado = ModeloCertificado.copiaModeloCertificado(modeloCertificado);
					if (ac.getCertificadoDisponivel()) {
						if (atividadeColaboradorFacade.update(ac) != null) {
							if (ftpUtil == null) {
								ftpUtil = new FTPUtil();
							}
							substituiNome(null, ac);
							String path = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
							Map<String, Object> parameters = new HashMap<String, Object>();
							String data = DateFormat.getDateInstance(DateFormat.LONG).format(new Date());
							parameters.put("DATA_CERTIFICADO", data);
							parameters.put("TEXTO_CERTIFICADO", copiaModeloCertificado.getTexto());
							parameters.put("TOKEN_VERIFICACAO", criaNovoToken(null, ac).getToken());
							parameters.put("IMAGE_DIR", path + "/imagens");

							InputStream in = ftpUtil.downloadArquivo2(
									modeloCertificado.getFundoCertificado().getNomeFtp(), "fundos_certificado");
							certificado = Report.getReportCertificado(in, parameters);
							String nomeArquivo = "certificado_colaborador_" + ac.getAtividade().getNomeFormatado() + "_"
									+ ac.getColaborador().getPessoa().getNomeFormatado() + ".pdf";
							String nomePasta = "certificados_colaboradores/atividade_id_"
									+ ac.getAtividade().getAtividadeId();
							ftpUtil.upload2(new AMedia(certificado, "application/pdf", null), nomeArquivo, nomePasta);
							tokenCertificadoFacade.include(tokenCertificado);
							atividadeColaboradores.remove(ac);
						}
					}
				}

				ArrayList<AtividadeColaborador> destinatarios = new ArrayList<AtividadeColaborador>();
				destinatarios.addAll(atividadeColaboradoresComAlteracao);
				new LongOperation() {
					@Override
					protected void execute() throws InterruptedException {
						emailSOPHIA.emailCertificadoColaboradorDisponivel(destinatarios, " ");
					}
				}.start();

			}
			modalConfirmacao.setVisible(false);
			Clients.showNotification("Alterações salvas com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null, null, 3000,
					true);
		} catch (Exception e) {
			if (!participantesInscritosComAlteracao.isEmpty()) {
				for (ParticipanteInscrito pi : participantesInscritosComAlteracao) {
					pi.setCertificadoDisponivel(false);
					participanteInscritoFacade.update(pi);
					tokenCertificado = tokenCertificadoFacade.findTokenCertificadoByParticipanteInscrito(pi);
					if (tokenCertificado != null) {
						tokenCertificadoFacade.delete(tokenCertificado.getTokenCertificadoId());
					}
					if (ftpUtil == null) {
						ftpUtil = new FTPUtil();
					}
					String nomeArquivo = "certificado_participante_" + pi.getAtividade().getNomeFormatado() + "_"
							+ pi.getParticipante().getNomeFormatado() + ".pdf";
					String nomePasta = "certificados_participantes/atividade_id_" + pi.getAtividade().getAtividadeId();
					ftpUtil.removeArquivo(nomeArquivo, nomePasta);
				}
			} else if (!atividadeColaboradoresComAlteracao.isEmpty()) {
				for (AtividadeColaborador ac : atividadeColaboradoresComAlteracao) {
					ac.setCertificadoDisponivel(false);
					atividadeColaboradorFacade.update(ac);
					tokenCertificado = tokenCertificadoFacade.findTokenCertificadoByAtividadeColaborador(ac);
					if (tokenCertificado != null) {
						tokenCertificadoFacade.delete(tokenCertificado.getTokenCertificadoId());
					}
					if (ftpUtil == null) {
						ftpUtil = new FTPUtil();
					}
					String nomeArquivo = "certificado_colaborador_" + ac.getAtividade().getNomeFormatado() + "_"
							+ ac.getColaborador().getPessoa().getNomeFormatado() + ".pdf";
					String nomePasta = "certificados_colaboradores/atividade_id_" + ac.getAtividade().getAtividadeId();
					ftpUtil.removeArquivo(nomeArquivo, nomePasta);
				}
			}

			Clients.showNotification("Ocorreu um erro inesperado ao salvar as alterações. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	public TokenCertificado criaNovoToken(ParticipanteInscrito pi, AtividadeColaborador ac) {
		try {
			tokenCertificado = new TokenCertificado();
			tokenCertificado.setToken(GeradorDeToken.generateToken());
			while (tokenCertificadoFacade.findTokenCertificadoByToken(tokenCertificado.getToken()) != null) {
				tokenCertificado.setToken(GeradorDeToken.generateToken());
			}
			if (pi != null) {
				tokenCertificado.setParticipanteInscrito(pi);
			} else if (ac != null) {
				tokenCertificado.setAtividadeColaborador(ac);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tokenCertificado;
	}

	@Command
	@NotifyChange(".")
	public void baixarCertificadoParticipante(
			@BindingParam("participanteInscrito") ParticipanteInscrito participanteInscrito) {
		try {
			String nomeArquivo = "certificado_participante_" + participanteInscrito.getAtividade().getNomeFormatado()
					+ "_" + participanteInscrito.getParticipante().getNomeFormatado() + ".pdf";
			String nomePasta = "certificados_participantes/atividade_id_"
					+ participanteInscrito.getAtividade().getAtividadeId();
			Executions.getCurrent().sendRedirect(
					"ftp://escex:escex!@123@ftp.tce.ma.gov.br:10021/" + nomePasta + "/" + nomeArquivo, "_blank");
		} catch (Exception e) {
			Clients.showNotification(
					"Ocorreu um erro inesperado ao baixar este certificado. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void baixarCertificadoColaborador(
			@BindingParam("atividadeColaborador") AtividadeColaborador atividadeColaborador) {
		try {
			String nomeArquivo = "certificado_colaborador_" + atividadeColaborador.getAtividade().getNomeFormatado()
					+ "_" + atividadeColaborador.getColaborador().getPessoa().getNomeFormatado() + ".pdf";
			String nomePasta = "certificados_colaboradores/atividade_id_"
					+ atividadeColaborador.getAtividade().getAtividadeId();
			Executions.getCurrent().sendRedirect(
					"ftp://escex:escex!@123@ftp.tce.ma.gov.br:10021/" + nomePasta + "/" + nomeArquivo, "_blank");
		} catch (Exception e) {
			Clients.showNotification(
					"Ocorreu um erro inesperado ao baixar este certificado. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmaExcluirCertificado(@BindingParam("visible") Boolean visible,
			@BindingParam("certificadoParticipante") ParticipanteInscrito certificadoParticipante,
			@BindingParam("certificadoColaborador") AtividadeColaborador certificadoColaborador) {
		try {
			if (visible) {
				if (certificadoParticipante != null) {
					this.certificadoParticipante = certificadoParticipante;
				} else {
					this.certificadoColaborador = certificadoColaborador;
				}
				modalConfirmacao.setVisible(visible);
			} else {
				modalConfirmacao.setVisible(visible);
				this.certificadoParticipante = null;
				this.certificadoColaborador = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void excluirCertificadoParticipante() {
		try {
			String nomeArquivo = "certificado_participante_" + certificadoParticipante.getAtividade().getNomeFormatado()
					+ "_" + certificadoParticipante.getParticipante().getNomeFormatado() + ".pdf";
			String nomePasta = "certificados_participantes/atividade_id_"
					+ certificadoParticipante.getAtividade().getAtividadeId();

			if (ftpUtil == null) {
				ftpUtil = new FTPUtil();
			}
			ftpUtil.removeArquivo(nomeArquivo, nomePasta);
			certificadoParticipante.setCertificadoDisponivel(false);
			participanteInscritoFacade.update(certificadoParticipante);
			participantesInscritos.remove(certificadoParticipante);

			TokenCertificado token = tokenCertificadoFacade
					.findTokenCertificadoByParticipanteInscrito(certificadoParticipante);
			tokenCertificadoFacade.delete(token.getTokenCertificadoId());
			certificadoParticipante = null;

			modalConfirmacao.setVisible(false);
			Clients.showNotification("Certificado excluído com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null, null,
					3000, true);
		} catch (Exception e) {
			Clients.showNotification(
					"Ocorreu um erro inesperado ao excluir este certificado. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void excluirCertificadoColaborador() {
		try {
			String nomeArquivo = "certificado_colaborador_" + certificadoColaborador.getAtividade().getNomeFormatado()
					+ "_" + certificadoColaborador.getColaborador().getPessoa().getNomeFormatado() + ".pdf";
			String nomePasta = "certificados_colaboradores/atividade_id_"
					+ certificadoColaborador.getAtividade().getAtividadeId();

			if (ftpUtil == null) {
				ftpUtil = new FTPUtil();
			}
			ftpUtil.removeArquivo(nomeArquivo, nomePasta);
			certificadoColaborador.setCertificadoDisponivel(false);
			atividadeColaboradorFacade.update(certificadoColaborador);
			atividadeColaboradores.remove(certificadoColaborador);

			TokenCertificado token = tokenCertificadoFacade
					.findTokenCertificadoByAtividadeColaborador(certificadoColaborador);
			tokenCertificadoFacade.delete(token.getTokenCertificadoId());
			certificadoColaborador = null;

			modalConfirmacao.setVisible(false);
			Clients.showNotification("Certificado excluído com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null, null,
					3000, true);
		} catch (Exception e) {
			Clients.showNotification(
					"Ocorreu um erro inesperado ao excluir este certificado. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void aplicarFiltro(@BindingParam("certificadoDisponivel") Boolean certificadoDisponivel) {
		try {
			if (tipo.equals("PARTICIPANTE")) {
				participantesInscritos = participanteInscritoFacade
						.findInscricoesByAtividadeAndInscricaoAprovadaAndCertificadoDisponivelAndFiltro(
								atividadeSelecionada, certificadoDisponivel, filtro);
			} else {
				atividadeColaboradores = atividadeColaboradorFacade
						.findColaboradoresByAtividadeAndCertificadoDisponivelAndFiltro(atividadeSelecionada,
								certificadoDisponivel, filtro);
			}
		} catch (Exception e) {
			Clients.showNotification("Erro ao realizar busca por filtro. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void limparFiltro(@BindingParam("certificadoDisponivel") Boolean certificadoDisponivel) {
		try {
			if (!filtro.getFiltro1().isEmpty() || !filtro.getFiltro2().isEmpty()) {
				filtro.limparFiltro();
				if (tipo.equals("PARTICIPANTE")) {
					participantesInscritos = participanteInscritoFacade
							.findParticipantesByAtividadeAndInscricaoAprovadaAndCertificadoDisponivel(
									atividadeSelecionada, true, certificadoDisponivel);
				} else {
					atividadeColaboradores = atividadeColaboradorFacade
							.findColaboradoresByAtividadeAndCertificadoDisponivel(atividadeSelecionada,
									certificadoDisponivel);
				}
			}
		} catch (Exception e) {
			Clients.showNotification("Erro ao realizar busca por filtro. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	public Atividade getAtividadeSelecionada() {
		return atividadeSelecionada;
	}

	public void setAtividadeSelecionada(Atividade atividadeSelecionada) {
		this.atividadeSelecionada = atividadeSelecionada;
	}

	public Evento getEventoSelecionado() {
		return eventoSelecionado;
	}

	public void setEventoSelecionado(Evento eventoSelecionado) {
		this.eventoSelecionado = eventoSelecionado;
	}

	public ModeloCertificado getModeloCertificado() {
		return modeloCertificado;
	}

	public void setModeloCertificado(ModeloCertificado modeloCertificado) {
		this.modeloCertificado = modeloCertificado;
	}

	public FundoCertificado getFundoCertificado() {
		return fundoCertificado;
	}

	public void setFundoCertificado(FundoCertificado fundoSelecionado) {
		this.fundoCertificado = fundoSelecionado;
	}

	public ParticipanteInscrito getCertificadoParticipante() {
		return certificadoParticipante;
	}

	public void setCertificadoParticipante(ParticipanteInscrito certificadoParticipante) {
		this.certificadoParticipante = certificadoParticipante;
	}

	public AtividadeColaborador getCertificadoColaborador() {
		return certificadoColaborador;
	}

	public void setCertificadoColaborador(AtividadeColaborador certificadoColaborador) {
		this.certificadoColaborador = certificadoColaborador;
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtroParticipante) {
		this.filtro = filtroParticipante;
	}

	public boolean isCriandoModeloDeCertificado() {
		return criandoModeloDeCertificado;
	}

	public void setCriandoModeloDeCertificado(boolean criandoModeloDeCertificado) {
		this.criandoModeloDeCertificado = criandoModeloDeCertificado;
	}

	public boolean isVlayoutVisible() {
		return vlayoutVisible;
	}

	public void setVlayoutVisible(boolean vlayoutVisible) {
		this.vlayoutVisible = vlayoutVisible;
	}

	public boolean isModeloJaExistente() {
		return modeloJaExistente;
	}

	public void setModeloJaExistente(boolean modeloJaExistente) {
		this.modeloJaExistente = modeloJaExistente;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTextoPersonalizado() {
		return textoPersonalizado;
	}

	public void setTextoPersonalizado(String textoPersonalizado) {
		this.textoPersonalizado = textoPersonalizado;
	}

	public String getTextoPadraoParticipante() {
		return textoPadraoParticipante;
	}

	public void setTextoPadraoParticipante(String textoPadrao) {
		this.textoPadraoParticipante = textoPadrao;
	}

	public String getTextoPadraoColaborador() {
		return textoPadraoColaborador;
	}

	public void setTextoPadraoColaborador(String textoPadraoColaborador) {
		this.textoPadraoColaborador = textoPadraoColaborador;
	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
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

	public Collection<ParticipanteInscrito> getParticipantesInscritos() {
		return participantesInscritos;
	}

	public void setParticipantesInscritos(Collection<ParticipanteInscrito> participantesInscritos) {
		this.participantesInscritos = participantesInscritos;
	}

	public Collection<AtividadeColaborador> getAtividadeColaboradores() {
		return atividadeColaboradores;
	}

	public void setAtividadeColaboradores(Collection<AtividadeColaborador> atividadeColaboradores) {
		this.atividadeColaboradores = atividadeColaboradores;
	}

	public Collection<AtividadeColaborador> getAtividadeColaboradoresComAlteracao() {
		return atividadeColaboradoresComAlteracao;
	}

	public void setAtividadeColaboradoresComAlteracao(
			Collection<AtividadeColaborador> atividadeColaboradoresComAlteracao) {
		this.atividadeColaboradoresComAlteracao = atividadeColaboradoresComAlteracao;
	}

	public Collection<FundoCertificado> getFundosDeCertificado() {
		return fundosDeCertificado;
	}

	public void setFundosDeCertificado(Collection<FundoCertificado> fundosDeCertificado) {
		this.fundosDeCertificado = fundosDeCertificado;
	}

	public String getTextoRodape1() {
		return String.format(textoRodape1, participantesInscritos.size());
	}

	public String getTextoRodape2() {
		return String.format(textoRodape2, atividadeColaboradores.size());
	}

}
