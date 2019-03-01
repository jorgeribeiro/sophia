package br.gov.ma.tce.sophia.gerenciamento.pages;

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
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Window;

import br.gov.ma.tce.gestores.server.beans.estado.Estado;
import br.gov.ma.tce.gestores.server.beans.estado.EstadoFacade;
import br.gov.ma.tce.gestores.server.beans.municipio.Municipio;
import br.gov.ma.tce.gestores.server.beans.municipio.MunicipioFacade;
import br.gov.ma.tce.sophia.ejb.beans.atividade.Atividade;
import br.gov.ma.tce.sophia.ejb.beans.atividade.AtividadeFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.diasatividade.DiasAtividade;
import br.gov.ma.tce.sophia.ejb.beans.diasatividade.DiasAtividadeFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.evento.Evento;
import br.gov.ma.tce.sophia.ejb.beans.evento.EventoFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.frequencia.Frequencia;
import br.gov.ma.tce.sophia.ejb.beans.frequencia.FrequenciaFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscrito;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscritoFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.participanteinteresse.ParticipanteInteresse;
import br.gov.ma.tce.sophia.ejb.beans.participanteinteresse.ParticipanteInteresseFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.usuario.Usuario;
import br.gov.ma.tce.sophia.ejb.util.Filtro;
import br.gov.ma.tce.sophia.ejb.util.HashUtil;
import br.gov.ma.tce.sophia.gerenciamento.utils.EmailSOPHIA;
import br.gov.ma.tce.sophia.gerenciamento.utils.FTPUtil;
import br.gov.ma.tce.sophia.gerenciamento.utils.LongOperation;

public class EventosVM {
	private Evento evento;
	private Atividade atividadeSelecionada;

	private FTPUtil ftpUtil;
	private EmailSOPHIA emailSOPHIA;

	private String categoria;
	private Media media;
	private boolean alterarEstadoVisible = false;
	private boolean eventoPrevisto = false;
	private boolean restamParticipantes = false;
	private String login;
	private String senha;
	private Filtro filtroParticipante;
	private int vagasSociedade = 0, vagasServidor = 0, vagasJurisdicionado = 0;
	private static Integer numeroParticipantes = 0, vagasTotal = 0, vagasDisponiveis = 0, inscricoesAprovadas = 0;
	private static final String textoRodape1 = "%d participante(s) encontrado(s)";
	private static final String textoRodape2 = "%d participante(s) encontrado(s) - Total de vagas: %d - Vagas disponíveis: %d";
	private static final String textoRodape3 = "%d participante(s) encontrado(s) - Inscrições aprovadas: %d";

	private Collection<Estado> estados;
	private Collection<Municipio> municipios;
	private Collection<Evento> eventos;
	private Collection<Atividade> atividades;
	private Collection<ParticipanteInteresse> participantesInteressados;
	private Collection<ParticipanteInteresse> participantesInteressadosParaInscrever;
	private Collection<ParticipanteInscrito> participantesInscritos;
	private Collection<ParticipanteInscrito> participantesInscritosParaInteresse;
	private Collection<DiasAtividade> diasAtividade;

	private EventoFacadeBean eventoFacade;
	private AtividadeFacadeBean atividadeFacade;
	private ParticipanteInteresseFacadeBean participanteInteresseFacade;
	private ParticipanteInscritoFacadeBean participanteInscritoFacade;
	private FrequenciaFacadeBean frequenciaFacade;
	private DiasAtividadeFacadeBean diasAtividadeFacade;
	private EstadoFacade estadoFacade;
	private MunicipioFacade municipioFacade;

	@Wire("#comboMunicipios")
	private Combobox comboMunicipios;

	@Wire("#modalAtualizarEvento #comboEstadosModal")
	private Combobox comboEstadosModal;

	@Wire("#modalAtualizarEvento #comboMunicipiosModal")
	private Combobox comboMunicipiosModal;

	@Wire("#modalAlterarCategoria")
	private Window modalAlterarCategoria;

	@Wire("#modalAtualizarEvento")
	private Window modalAtualizarEvento;

	@Wire("#modalCancelarEvento")
	private Window modalCancelarEvento;

	@Wire("#modalConfirmacaoAlterarCategoria")
	private Window modalConfirmacaoAlterarCategoria;

	@Wire("#modalConfirmacaoAlterarParaRealizado")
	private Window modalConfirmacaoAlterarParaRealizado;

	@Wire("#modalConfirmacaoCancelarEvento")
	private Window modalConfirmacaoCancelarEvento;

	@Wire("#modalConfirmacao")
	private Window modalConfirmacao;

	@Wire("#modalParticipantes")
	private Window modalParticipantes;

	@Wire("#modalParticipantesAlterarCategoriaPrevisto")
	private Window modalParticipantesAlterarCategoriaPrevisto;

	@Wire("#modalParticipantes #comboAtividades")
	private Combobox comboAtividades;

	@Wire("#modalParticipantesAlterarCategoriaPrevisto #comboAtividadesAlterarCategoriaPrevisto")
	private Combobox comboAtividadesAlterarCategoriaPrevisto;

	@Wire("#modalParticipantesAlterarCategoriaAberto")
	private Window modalParticipantesAlterarCategoriaAberto;

	@Wire("#modalParticipantesAlterarCategoriaAberto #comboAtividadesAlterarCategoriaAberto")
	private Combobox comboAtividadesAlterarCategoriaAberto;

	@Wire("#modalParticipantesAlterarCategoriaEmAndamento")
	private Window modalParticipantesAlterarCategoriaEmAndamento;

	@Wire("#modalParticipantesAlterarCategoriaEmAndamento #comboAtividadesAlterarCategoriaEmAndamento")
	private Combobox comboAtividadesAlterarCategoriaEmAndamento;

	public EventosVM() {
		try {
			InitialContext ctx = new InitialContext();
			eventoFacade = (EventoFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/EventoFacadeBean!br.gov.ma.tce.sophia.ejb.beans.evento.EventoFacadeBean");
			atividadeFacade = (AtividadeFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/AtividadeFacadeBean!br.gov.ma.tce.sophia.ejb.beans.atividade.AtividadeFacadeBean");
			participanteInteresseFacade = (ParticipanteInteresseFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/ParticipanteInteresseFacadeBean!br.gov.ma.tce.sophia.ejb.beans.participanteinteresse.ParticipanteInteresseFacadeBean");
			participanteInscritoFacade = (ParticipanteInscritoFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/ParticipanteInscritoFacadeBean!br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscritoFacadeBean");
			frequenciaFacade = (FrequenciaFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/FrequenciaFacadeBean!br.gov.ma.tce.sophia.ejb.beans.frequencia.FrequenciaFacadeBean");
			diasAtividadeFacade = (DiasAtividadeFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/DiasAtividadeFacadeBean!br.gov.ma.tce.sophia.ejb.beans.diasatividade.DiasAtividadeFacadeBean");
			estadoFacade = (EstadoFacade) ctx.lookup(
					"java:global/sophia_ear/gestores_server/gertrudes_EstadoFacadeBean!br.gov.ma.tce.gestores.server.beans.estado.EstadoFacade");
			municipioFacade = (MunicipioFacade) ctx.lookup(
					"java:global/sophia_ear/gestores_server/gertrudes_MunicipioFacadeBean!br.gov.ma.tce.gestores.server.beans.municipio.MunicipioFacade");
		} catch (NamingException e) {
			Clients.showNotification("Erro de conexão interno. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
			e.printStackTrace();
		}
	}

	@Init
	public void init() {
		evento = new Evento();
		participantesInteressados = new ArrayList<ParticipanteInteresse>();
		participantesInteressadosParaInscrever = new ArrayList<ParticipanteInteresse>();
		participantesInscritos = new ArrayList<ParticipanteInscrito>();
		participantesInscritosParaInteresse = new ArrayList<ParticipanteInscrito>();
		filtroParticipante = new Filtro();
		emailSOPHIA = new EmailSOPHIA();
	}

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}

	@NotifyChange(".")
	public void countVagas() {
		if (atividadeSelecionada != null) {
			if (eventoPrevisto) {
				numeroParticipantes = participantesInteressados.size();
			} else {
				numeroParticipantes = participantesInscritos.size();
			}
			inscricoesAprovadas = participanteInscritoFacade.countInscricoesByAtividade(atividadeSelecionada)
					.intValue();
			vagasTotal = atividadeSelecionada.getVagas();
			vagasDisponiveis = vagasTotal - inscricoesAprovadas;
			if (atividadeSelecionada.getVagasServidor() != null) {
				vagasSociedade = (int) (atividadeSelecionada.getVagasSociedade() - participanteInscritoFacade
						.countInscricoesByAtividadeAndTipoParticipante(atividadeSelecionada, "1"));
				vagasServidor = (int) (atividadeSelecionada.getVagasServidor() - participanteInscritoFacade
						.countInscricoesByAtividadeAndTipoParticipante(atividadeSelecionada, "2"));
				vagasJurisdicionado = (int) (atividadeSelecionada.getVagasJurisdicionado() - participanteInscritoFacade
						.countInscricoesByAtividadeAndTipoParticipante(atividadeSelecionada, "3"));
			}
		} else {
			numeroParticipantes = inscricoesAprovadas = vagasTotal = vagasDisponiveis = vagasSociedade = vagasServidor = vagasJurisdicionado = 0;
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarEstados() {
		estados = estadoFacade.findAll();
	}

	@Command
	@NotifyChange(".")
	public void uploadBanner(@BindingParam("upEvent") UploadEvent event) {
		try {
			media = event.getMedia();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmaCriarEvento(@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				consertaDados(evento);
				if ((evento.getNome() == null || evento.getNome().equals(""))
						|| (evento.getDescricao() == null || evento.getDescricao().equals(""))
						|| evento.getDataInicio() == null || evento.getDataFim() == null
						|| evento.getHoraInicio() == null || evento.getHoraFim() == null
						|| (evento.getCategoria() == null || evento.getCategoria().equals(""))
						|| (evento.getLocal() == null || evento.getLocal().equals(""))
						|| (evento.getLogradouro() == null || evento.getLogradouro().equals(""))
						|| (evento.getNumero() == null || evento.getNumero().equals("")) || evento.getEstado() == null
						|| evento.getMunicipio() == null
						|| (evento.getBairro() == null || evento.getBairro().equals("")) || evento.getCep() == null
						|| (evento.getObjetivoGeral() == null || evento.getObjetivoGeral().equals(""))) {
					throw new Exception("Todos os campos obrigatórios precisam ser preechidos corretamente.");
				} else {
					modalConfirmacao.setVisible(visible);
				}
			} else {
				modalConfirmacao.setVisible(visible);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void criarEvento() {
		try {
			if ((evento = eventoFacade.include(evento)) != null) {
				try {
					if (ftpUtil == null) {
						ftpUtil = new FTPUtil();
					}
					if (media != null) {
						String extensao = media.getName().substring(media.getName().length() - 4,
								media.getName().length());
						ftpUtil.upload2(media, "banner_evento_id_" + evento.getEventoId() + extensao,
								"banners_eventos/banner_evento_id_" + evento.getEventoId());
					}
				} catch (Exception e) {
					Clients.showNotification(
							"Ocorreu um erro inesperado ao fazer o upload do arquivo de banner. Tente novamente mais tarde.",
							Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
					modalConfirmacao.setVisible(true);
					return;
				}
				media = null;
				evento = new Evento();
				modalConfirmacao.setVisible(false);
				Clients.showNotification("Evento criado com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null, null, 3000,
						true);
			}
		} catch (Exception e) {
			Clients.showNotification("Ocorreu um erro inesperado ao criar o evento. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarListaPorEstado() {
		try {
			municipios = municipioFacade.findMunicipiosByEstado(evento.getEstado().getEstadoId());
			evento.setMunicipio(null);
			if (comboMunicipios != null) {
				comboMunicipios.setSelectedItem(null);
			}
			if (comboMunicipiosModal != null) {
				comboMunicipiosModal.setSelectedItem(null);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarEventosPorCategoria() {
		try {
			// TODO carregar eventos realizados usando um filtro de período de tempo
			eventos = eventoFacade.findEventosByCategoria(categoria);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarListaPorAtividade() {
		try {
			if (eventoPrevisto) {
				participantesInteressados = atividadeSelecionada.getInteressados();
			} else {
				participantesInscritos = atividadeSelecionada.getInscricoes();
			}
			countVagas();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalAlterarCategoria(@BindingParam("evento") Evento evento,
			@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				this.evento = evento;
				modalAlterarCategoria.setVisible(visible);
			} else {
				eventos = eventoFacade.findEventosByCategoria(this.categoria);
				modalAlterarCategoria.setVisible(visible);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalSeguinteAlterarCategoria(@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				String resultado = null;
				atividadeSelecionada = null;
				comboAtividadesAlterarCategoriaPrevisto.setSelectedItem(null);
				comboAtividadesAlterarCategoriaAberto.setSelectedItem(null);
				comboAtividadesAlterarCategoriaEmAndamento.setSelectedItem(null);
				restamParticipantes = false;
				countVagas();
				if (evento.getCategoria() == null || evento.getCategoria().equals("")) {
					throw new Exception("Escolha uma categoria.");
				} else if ((resultado = verificaAlteracaoCategoria()) != null) {
					throw new Exception(resultado);
				} else {
					eventoPrevisto = categoria.equals("PREVISTO");
					atividades = new ArrayList<Atividade>();
					for (Atividade atv : atividadeFacade.findAtividadesByEvento(evento)) {
						atv = atividadeFacade.findByPrimaryKey(atv.getAtividadeId());
						atividades.add(atv);
						if (!atv.getInteressados().isEmpty() || !atv.getInscricoes().isEmpty()) {
							if (evento.getCategoria().equals("PREVISTO")) {
								participantesInscritosParaInteresse.addAll(atv.getInscricoes());
							}
							restamParticipantes = true;
						}
					}
					if (categoria.equals("PREVISTO")) {
						participantesInteressados = new ArrayList<ParticipanteInteresse>();
						modalParticipantesAlterarCategoriaPrevisto.setVisible(visible);
					} else if (categoria.equals("ABERTO")) {
						participantesInscritos = new ArrayList<ParticipanteInscrito>();
						modalParticipantesAlterarCategoriaAberto.setVisible(visible);
					} else if (categoria.equals("EM ANDAMENTO")) {
						participantesInscritos = new ArrayList<ParticipanteInscrito>();
						modalParticipantesAlterarCategoriaEmAndamento.setVisible(visible);
					}
				}
			} else {
				if (categoria.equals("PREVISTO")) {
					for (Atividade atv : atividades) {
						Collection<ParticipanteInteresse> participantesInteressados = atv.getInteressados();
						for (ParticipanteInteresse pi : participantesInteressados) {
							if (pi.getInscritoNaAtividade()) {
								pi.setInscritoNaAtividade(false);
								participanteInteresseFacade.update(pi);
							}
						}
					}
					modalParticipantesAlterarCategoriaPrevisto.setVisible(visible);
				} else if (categoria.equals("ABERTO")) {
					modalParticipantesAlterarCategoriaAberto.setVisible(visible);
				} else if (categoria.equals("EM ANDAMENTO")) {
					modalParticipantesAlterarCategoriaEmAndamento.setVisible(visible);
				}
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 4000, true);
		}
	}

	private String verificaAlteracaoCategoria() {
		if (evento.getCategoria().equals(categoria)) {
			return "Selecione uma categoria diferente da categoria atual desse evento.";
		} else if (categoria.equals("PREVISTO") || categoria.equals("ABERTO")) {
			if (evento.getCategoria().equals("REALIZADO")) {
				return "Altere a categoria do evento para EM ANDAMENTO para depois colocá-lo como REALIZADO.";
			}
		}
		return null;
	}

	@Command
	@NotifyChange(".")
	public void inscreverParticipante(
			@BindingParam("participanteInteresse") ParticipanteInteresse participanteInteresse) {
		try {
			if (participanteInteresse.getInscritoNaAtividade()) {
				if (vagasDisponiveis < vagasTotal) {
					participanteInteresse.setInscritoNaAtividade(false);
					if (participanteInteresseFacade.update(participanteInteresse) == null) {
						participanteInteresse.setInscritoNaAtividade(!participanteInteresse.getInscritoNaAtividade());
						throw new Exception(
								"Ocorreu um erro inesperado ao cancelar essa inscrição. Tente novamente mais tarde.");
					}
					if (participanteInteresse.getParticipante().getTipoParticipanteStr().equals("SOCIEDADE")) {
						vagasSociedade++;
					} else if (participanteInteresse.getParticipante().getTipoParticipanteStr()
							.equals("SERVIDOR DO TCE-MA") || participanteInteresse.getParticipante().getTipoParticipanteStr()
							.equals("SERVIDOR TERCEIRIZADO")) {
						vagasServidor++;
					} else {
						vagasJurisdicionado++;
					}
					vagasDisponiveis++;
					participantesInteressadosParaInscrever.remove(participanteInteresse);
				}
			} else {
				if (vagasDisponiveis == 0) {
					throw new Exception(
							"Não há mais vagas disponíveis para essa atividade. Aumente o número de vagas ou remova alguma inscrição para dar lugar a essa.");
				} else {
					if (atividadeSelecionada.getVagasServidor() != null) {
						if (participanteInteresse.getParticipante().getTipoParticipanteStr().equals("SOCIEDADE")) {
							if (vagasSociedade == 0) {
								throw new Exception(
										"Não há mais vagas disponíveis para essa atividade para o público SOCIEDADE. Aumente o número de vagas ou remova alguma inscrição para dar lugar a essa.");
							} else {
								vagasSociedade--;
							}
						} else if (participanteInteresse.getParticipante().getTipoParticipanteStr()
								.equals("SERVIDOR DO TCE-MA") || participanteInteresse.getParticipante().getTipoParticipanteStr()
								.equals("SERVIDOR TERCEIRIZADO")) {
							if (vagasServidor == 0) {
								throw new Exception(
										"Não há mais vagas disponíveis para essa atividade para o público SERVIDOR DO TCE-MA. Aumente o número de vagas ou remova alguma inscrição para dar lugar a essa.");
							} else {
								vagasServidor--;
							}
						} else {
							if (vagasJurisdicionado == 0) {
								throw new Exception(
										"Não há mais vagas disponíveis para essa atividade para o público JURISDICIONADO. Aumente o número de vagas ou remova alguma inscrição para dar lugar a essa.");
							} else {
								vagasJurisdicionado--;
							}
						}
					}
					participanteInteresse.setInscritoNaAtividade(true);
					if (participanteInteresseFacade.update(participanteInteresse) == null) {
						participanteInteresse.setInscritoNaAtividade(!participanteInteresse.getInscritoNaAtividade());
						throw new Exception(
								"Ocorreu um erro inesperado ao realizar essa inscrição. Tente novamente mais tarde.");
					}
					vagasDisponiveis--;
					participantesInteressadosParaInscrever.add(participanteInteresse);
				}
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 4000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void inscreverParticipantes() {
		try {
			boolean status = participantesInteressados.iterator().next().getInscritoNaAtividade() ? false : true;
			int cont = 0;
			for (ParticipanteInteresse pi : participantesInteressados) {
				if (status) {
					if (vagasDisponiveis == 0) {
						throw new Exception("O número de inscrições ultrapassou a quantidade de vagas. "
								+ "Foram confirmadas apenas as " + cont + " inscrições disponíveis nessa atividade.");
					} else {
						pi.setInscritoNaAtividade(true);
						if (participanteInteresseFacade.update(pi) == null) {
							pi.setInscritoNaAtividade(!pi.getInscritoNaAtividade());
							throw new Exception(
									"Ocorreu um erro inesperado ao realizar as inscrições. Tente novamente mais tarde.");
						}
						cont++;
						vagasDisponiveis--;
						participantesInteressadosParaInscrever.add(pi);
					}
				} else {
					pi.setInscritoNaAtividade(false);
					if (participanteInteresseFacade.update(pi) == null) {
						pi.setInscritoNaAtividade(!pi.getInscritoNaAtividade());
						throw new Exception(
								"Ocorreu um erro inesperado ao cancelar as inscrições. Tente novamente mais tarde.");
					}
					vagasDisponiveis++;
					pi.setInscritoNaAtividade(false);
					participantesInteressadosParaInscrever.remove(pi);
				}
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 4000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmacaoAlterarCategoria(@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				if (eventoPrevisto) {
					for (Atividade atv : atividades) {
						Collection<ParticipanteInteresse> participantesInteressados = atv.getInteressados();
						for (ParticipanteInteresse pi : participantesInteressados) {
							if (!pi.getInscritoNaAtividade()) {
								restamParticipantes = true;
								break;
							} else {
								restamParticipantes = false;
							}
						}
						if (restamParticipantes) {
							break;
						}
					}
				}
				if (evento.getCategoria().equals("REALIZADO")) {
					modalConfirmacaoAlterarParaRealizado.setVisible(visible);
				} else {
					modalConfirmacaoAlterarCategoria.setVisible(visible);
				}
			} else {
				if (evento.getCategoria().equals("REALIZADO")) {
					modalConfirmacaoAlterarParaRealizado.setVisible(visible);
				} else {
					modalConfirmacaoAlterarCategoria.setVisible(visible);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void alterarParaRealizado() {
		try {
			if ((login == null || login.length() == 0) || (senha == null || senha.length() == 0)) {
				throw new Exception("Os campos Login e Senha precisam ser preenchidos.");
			}

			Usuario usuario = (Usuario) Sessions.getCurrent().getAttribute("usuario");
			if (!usuario.getPessoa().getCpf().equals(login)) {
				throw new Exception("Credenciais inválidas.");
			} else if (!HashUtil.rehashSenha(senha, usuario.getPessoa().getSenha())) {
				throw new Exception("Credenciais inválidas.");
			} else {
				alterarCategoria();
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void alterarCategoria() {
		try {
			if (categoria.equals("PREVISTO")) {
				alterarCategoriaPrevisto();
			} else if (categoria.equals("ABERTO")) {
				alterarCategoriaAberto();
			} else if (categoria.equals("EM ANDAMENTO")) {
				alterarCategoriaEmAndamento();
			}

			if (eventoFacade.update(evento) != null) {
				eventos = eventoFacade.findEventosByCategoria(this.categoria);
				modalConfirmacaoAlterarCategoria.setVisible(false);
				if (categoria.equals("PREVISTO")) {
					modalParticipantesAlterarCategoriaPrevisto.setVisible(false);
				} else if (categoria.equals("ABERTO")) {
					modalParticipantesAlterarCategoriaAberto.setVisible(false);
				} else if (categoria.equals("EM ANDAMENTO")) {
					if (evento.getCategoria().equals("REALIZADO")) {
						modalConfirmacaoAlterarParaRealizado.setVisible(false);
					}
					modalParticipantesAlterarCategoriaEmAndamento.setVisible(false);
				}
				modalAlterarCategoria.setVisible(false);
				Clients.showNotification("Categoria alterada com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null, null,
						3000, true);
			} else {
				throw new Exception("Ocorreu um erro ao alterar a categoria do evento. Tente novamente mais tarde.");
			}

		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	private void alterarCategoriaPrevisto() {
		try {
			for (ParticipanteInteresse pi : participantesInteressadosParaInscrever) {
				ParticipanteInscrito p = ParticipanteInscrito.copiarParticipanteInteresse(pi);
				if (participanteInscritoFacade.include(p) != null) {
					diasAtividade = diasAtividadeFacade.findDiasByAtividade(atividadeSelecionada);
					for (DiasAtividade da : diasAtividade) {
						Frequencia frequencia = new Frequencia();
						frequencia.setDiasAtividade(da);
						frequencia.setParticipanteInscrito(p);
						frequencia.setPresenca(false);
						if (frequenciaFacade.include(frequencia) == null) {
							// Se a inclusão de alguma frequência falhar,
							// remove as anteriores já inclusas
							Collection<Frequencia> frequencias = frequenciaFacade
									.findFrequenciasByParticipanteInscrito(p);
							for (Frequencia f : frequencias) {
								frequenciaFacade.delete(f.getFrequenciaId());
							}
							// E também anula a inscrição já aprovada
							participanteInscritoFacade.delete(p.getParticipanteInscritoId());
							pi.setInscritoNaAtividade(false);
							participanteInteresseFacade.update(pi);
							break;
						}
					}
				}
			}

			ArrayList<ParticipanteInteresse> destinatarios = new ArrayList<ParticipanteInteresse>();
			destinatarios.addAll(participantesInteressadosParaInscrever);
			new LongOperation() {
				@Override
				protected void execute() throws InterruptedException {
					for (ParticipanteInteresse pi : participantesInteressadosParaInscrever) {
						emailSOPHIA.emailAlterarCategoriaPrevisto(destinatarios, evento.getCategoria(),
								pi.getAtividade(), " ");
					}
				}
			}.start();
		} catch (Exception e) {
			Clients.showNotification(
					"Ocorreu um erro inesperado ao confirmar as alterações. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	private void alterarCategoriaAberto() {
		try {
			if (evento.getCategoria().equals("PREVISTO")) {
				for (ParticipanteInscrito pi : participantesInscritosParaInteresse) {
					ParticipanteInteresse p = participanteInteresseFacade
							.findByParticipanteAndAtividade(pi.getParticipante(), pi.getAtividade());
					if (p == null) {
						p = ParticipanteInteresse.copiarParticipanteInscrito(pi);
						p = participanteInteresseFacade.include(p);
					} else {
						p.setInscritoNaAtividade(false);
						p = participanteInteresseFacade.update(p);
					}
					if (p != null) {
						// Após inserir participante na lista de interesse, remove sua inscrição e
						// frequências
						pi = participanteInscritoFacade.findByPrimaryKey(pi.getParticipanteInscritoId());
						for (Frequencia f : pi.getFrequencias()) {
							frequenciaFacade.delete(f.getFrequenciaId());
						}
						participanteInscritoFacade.delete(pi.getParticipanteInscritoId());
					}
				}

				ArrayList<ParticipanteInscrito> destinatarios = new ArrayList<ParticipanteInscrito>();
				destinatarios.addAll(participantesInscritosParaInteresse);
				new LongOperation() {
					@Override
					protected void execute() throws InterruptedException {
						for (ParticipanteInteresse pi : participantesInteressadosParaInscrever) {
							emailSOPHIA.emailAlterarCategoriaAberto(destinatarios, pi.getAtividade(), " ");
						}
					}
				}.start();
			}
		} catch (Exception e) {
			Clients.showNotification(
					"Ocorreu um erro inesperado ao confirmar as alterações. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	private void alterarCategoriaEmAndamento() {
		try {
			if (evento.getCategoria().equals("PREVISTO")) {
				for (ParticipanteInscrito pi : participantesInscritosParaInteresse) {
					ParticipanteInteresse p = participanteInteresseFacade
							.findByParticipanteAndAtividade(pi.getParticipante(), pi.getAtividade());
					if (p == null) {
						p = ParticipanteInteresse.copiarParticipanteInscrito(pi);
						p = participanteInteresseFacade.include(p);
					} else {
						p.setInscritoNaAtividade(false);
						p = participanteInteresseFacade.update(p);
					}
					if (p != null) {
						// Após inserir participante na lista de interesse, remove sua inscrição e
						// frequências
						pi = participanteInscritoFacade.findByPrimaryKey(pi.getParticipanteInscritoId());
						for (Frequencia f : pi.getFrequencias()) {
							frequenciaFacade.delete(f.getFrequenciaId());
						}
						participanteInscritoFacade.delete(pi.getParticipanteInscritoId());
					}
				}

				ArrayList<ParticipanteInscrito> destinatarios = new ArrayList<ParticipanteInscrito>();
				destinatarios.addAll(participantesInscritosParaInteresse);
				new LongOperation() {
					@Override
					protected void execute() throws InterruptedException {
						for (ParticipanteInteresse pi : participantesInteressadosParaInscrever) {
							emailSOPHIA.emailAlterarCategoriaAberto(destinatarios, pi.getAtividade(), " ");
						}
					}
				}.start();
			}
		} catch (Exception e) {
			Clients.showNotification(
					"Ocorreu um erro inesperado ao confirmar as alterações. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalAtualizarEvento(@BindingParam("evento") Evento evento,
			@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				this.evento = evento;
				this.alterarEstadoVisible = false;
				comboEstadosModal.setSelectedItem(null);
				comboMunicipiosModal.setSelectedItem(null);
				modalAtualizarEvento.setVisible(visible);
			} else {
				eventos = eventoFacade.findEventosByCategoria(this.categoria);
				modalAtualizarEvento.setVisible(visible);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void exibirOpcoesEditarEstado() {
		alterarEstadoVisible = true;
		evento.setEstado(null);
		evento.setMunicipio(null);
		comboEstadosModal.setSelectedItem(null);
		comboMunicipiosModal.setSelectedItem(null);
		municipios = null;
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmaAtualizarEvento(@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				consertaDados(evento);
				if ((evento.getNome() == null || evento.getNome().equals(""))
						|| (evento.getDescricao() == null || evento.getDescricao().equals(""))
						|| evento.getDataInicio() == null || evento.getDataFim() == null
						|| evento.getHoraInicio() == null || evento.getHoraFim() == null
						|| (evento.getLocal() == null || evento.getLocal().equals(""))
						|| (evento.getLogradouro() == null || evento.getLogradouro().equals(""))
						|| (evento.getNumero() == null || evento.getNumero().equals("")) || evento.getEstado() == null
						|| evento.getMunicipio() == null
						|| (evento.getBairro() == null || evento.getBairro().equals("")) || evento.getCep() == null
						|| (evento.getObjetivoGeral() == null || evento.getObjetivoGeral().equals(""))) {
					throw new Exception("Todos os campos obrigatórios precisam ser preechidos corretamente.");
				} else {
					modalConfirmacao.setVisible(visible);
				}
			} else {
				modalConfirmacao.setVisible(visible);
			}
		} catch (

		Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void atualizarEvento() {
		try {
			if (eventoFacade.update(evento) != null) {
				try {
					if (ftpUtil == null) {
						ftpUtil = new FTPUtil();
					}
					if (media != null) {
						String extensao = media.getName().substring(media.getName().length() - 4,
								media.getName().length());
						ftpUtil.upload2(media, "banner_evento_id_" + evento.getEventoId() + extensao,
								"banners_eventos/banner_evento_id_" + evento.getEventoId());
					}
				} catch (Exception e) {
					Clients.showNotification(
							"Ocorreu um erro inesperado ao fazer o upload do arquivo de banner. Tente novamente mais tarde.",
							Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
					modalConfirmacao.setVisible(false);
					return;
				}
				eventos = eventoFacade.findEventosByCategoria(this.categoria);
				modalConfirmacao.setVisible(false);
				modalAtualizarEvento.setVisible(false);
				Clients.showNotification("Evento atualizado com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null, null,
						3000, true);
			}
		} catch (Exception e) {
			Clients.showNotification("Ocorreu um erro ao atualizar o evento. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalCancelarEvento(@BindingParam("evento") Evento evento,
			@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				this.evento = evento;
				this.evento.setMotivoCancelamento(null);
				login = senha = null;
				modalCancelarEvento.setVisible(visible);
			} else {
				modalCancelarEvento.setVisible(visible);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmacaoCancelarEvento(@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				if (evento.getMotivoCancelamento() == null || evento.getMotivoCancelamento().trim().equals("")) {
					throw new Exception("Preencha o motivo do cancelamento.");
				} else if ((login == null || login.length() == 0) || (senha == null || senha.length() == 0)) {
					throw new Exception("Os campos Login e Senha precisam ser preenchidos.");
				}

				Usuario usuario = (Usuario) Sessions.getCurrent().getAttribute("usuario");
				if (!usuario.getPessoa().getCpf().equals(login)) {
					throw new Exception("Credenciais inválidas.");
				} else if (!HashUtil.rehashSenha(senha, usuario.getPessoa().getSenha())) {
					throw new Exception("Credenciais inválidas.");
				} else {
					modalConfirmacaoCancelarEvento.setVisible(visible);
				}
			} else {
				modalConfirmacaoCancelarEvento.setVisible(visible);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void cancelarEvento() {
		try {
			evento.setCategoriaAnteriorCancelamento(evento.getCategoria());
			evento.setCategoria("CANCELADO");
			if (eventoFacade.update(evento) != null) {
				if (evento.getCategoriaAnteriorCancelamento().equals("PREVISTO")) {
					Collection<ParticipanteInteresse> destinatarios = participanteInteresseFacade
							.findListaDeInteresseByEvento(evento);
					new LongOperation() {
						@Override
						protected void execute() throws InterruptedException {
							emailSOPHIA.emailEventoPrevistoCancelado((ArrayList<ParticipanteInteresse>) destinatarios,
									true, " ");
						}
					}.start();
				} else {
					Collection<ParticipanteInscrito> destinatarios = participanteInscritoFacade
							.findParticipantesByEvento(evento);
					new LongOperation() {
						@Override
						protected void execute() throws InterruptedException {
							emailSOPHIA.emailEventoAbertoOuEmAndamentoCancelado(
									(ArrayList<ParticipanteInscrito>) destinatarios, true, " ");
						}
					}.start();
				}

				eventos = eventoFacade.findEventosByCategoria(this.categoria);
				modalConfirmacaoCancelarEvento.setVisible(false);
				modalCancelarEvento.setVisible(false);
				Clients.showNotification("Evento cancelado com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null, null,
						3000, true);
			} else {
				throw new Exception("Ocorreu um erro ao cancelar o evento. Tente novamente mais tarde.");
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarEventosRealizados() {
		try {
			// TODO carregar eventos usando um filtro de período de tempo
			eventos = eventoFacade.findEventosByCategoria("REALIZADO");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarEventosCancelados() {
		try {
			eventos = eventoFacade.findEventosByCategoria("CANCELADO");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalParticipantes(@BindingParam("evento") Evento evento,
			@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				atividadeSelecionada = null;
				comboAtividades.setSelectedItem(null);
				countVagas();
				eventoPrevisto = evento.getCategoria().equals("CANCELADO")
						? evento.getCategoriaAnteriorCancelamento().equals("PREVISTO")
						: false;
				participantesInteressados = new ArrayList<ParticipanteInteresse>();
				participantesInscritos = new ArrayList<ParticipanteInscrito>();
				atividades = new ArrayList<Atividade>();
				for (Atividade atv : atividadeFacade.findAtividadesByEvento(evento)) {
					atv = atividadeFacade.findByPrimaryKey(atv.getAtividadeId());
					atividades.add(atv);
				}
				modalParticipantes.setVisible(visible);
			} else {
				modalParticipantes.setVisible(visible);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void aplicarFiltro() {
		try {
			if (eventoPrevisto) {
				participantesInteressados = participanteInteresseFacade
						.findListaDeInteresseByNomeAndAtividade(filtroParticipante.getFiltro1(), atividadeSelecionada);
			} else {
				participantesInscritos = participanteInscritoFacade.findParticipantesInscritosByNomeAndAtividade(
						filtroParticipante.getFiltro1(), atividadeSelecionada);
			}
		} catch (Exception e) {
			Clients.showNotification("Erro ao realizar busca por filtro. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	/**
	 * Testa quantidade de números em uma string
	 * 
	 * @param input
	 * @param quant
	 * @return se a string contém o número de números em quant
	 */
	private Boolean testaQuantNumeros(String input, int quant) {
		if (input != null) {
			int count = 0;
			for (int i = 0; i < input.length(); i++) {
				if (Character.isDigit(input.charAt(i))) {
					count++;
				}
			}
			return count == quant;
		} else {
			return false;
		}
	}

	private void consertaDados(Evento evento) {
		if (!testaQuantNumeros(evento.getCep(), 8)) {
			evento.setCep(null);
		}

		if (evento.getComplemento() != null && evento.getComplemento().trim().equals("")) {
			evento.setComplemento(null);
		}
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public Atividade getAtividadeSelecionada() {
		return atividadeSelecionada;
	}

	public void setAtividadeSelecionada(Atividade atividadeSelecionada) {
		this.atividadeSelecionada = atividadeSelecionada;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	public boolean isAlterarEstadoVisible() {
		return alterarEstadoVisible;
	}

	public void setAlterarEstadoVisible(boolean alterarEstadoVisible) {
		this.alterarEstadoVisible = alterarEstadoVisible;
	}

	public boolean isEventoPrevisto() {
		return eventoPrevisto;
	}

	public void setEventoPrevisto(boolean eventoPrevisto) {
		this.eventoPrevisto = eventoPrevisto;
	}

	public boolean isRestamParticipantes() {
		return restamParticipantes;
	}

	public void setRestamParticipantes(boolean restamNaoInscritos) {
		this.restamParticipantes = restamNaoInscritos;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Filtro getFiltroParticipante() {
		return filtroParticipante;
	}

	public void setFiltroParticipante(Filtro filtroParticipante) {
		this.filtroParticipante = filtroParticipante;
	}

	public int getVagasSociedade() {
		return vagasSociedade;
	}

	public void setVagasSociedade(int vagasSociedade) {
		this.vagasSociedade = vagasSociedade;
	}

	public int getVagasServidor() {
		return vagasServidor;
	}

	public void setVagasServidor(int vagasServidor) {
		this.vagasServidor = vagasServidor;
	}

	public int getVagasJurisdicionado() {
		return vagasJurisdicionado;
	}

	public void setVagasJurisdicionado(int vagasJurisdicionado) {
		this.vagasJurisdicionado = vagasJurisdicionado;
	}

	public Collection<Estado> getEstados() {
		// Remove dados desnecessários presentes no banco da lista de estados
		if (estados != null) {
			estados.remove(estadoFacade.findByPrimaryKey(0));
			estados.remove(estadoFacade.findByPrimaryKey(99));
		}
		return estados;
	}

	public void setEstados(Collection<Estado> estados) {
		this.estados = estados;
	}

	public Collection<Municipio> getMunicipios() {
		return municipios;
	}

	public void setMunicipios(Collection<Municipio> municipios) {
		this.municipios = municipios;
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

	public Collection<ParticipanteInteresse> getParticipantesInteressados() {
		return participantesInteressados;
	}

	public void setParticipantesInteressados(Collection<ParticipanteInteresse> participantesInteressados) {
		this.participantesInteressados = participantesInteressados;
	}

	public Collection<ParticipanteInteresse> getParticipantesInteressadosParaInscrever() {
		return participantesInteressadosParaInscrever;
	}

	public void setParticipantesInteressadosParaInscrever(
			Collection<ParticipanteInteresse> participantesInteressadosInscritos) {
		this.participantesInteressadosParaInscrever = participantesInteressadosInscritos;
	}

	public Collection<ParticipanteInscrito> getParticipantesInscritos() {
		return participantesInscritos;
	}

	public void setParticipantesInscritos(Collection<ParticipanteInscrito> participantesInscritos) {
		this.participantesInscritos = participantesInscritos;
	}

	public Collection<ParticipanteInscrito> getParticipantesInscritosParaInteresse() {
		return participantesInscritosParaInteresse;
	}

	public void setParticipantesInscritosParaInteresse(
			Collection<ParticipanteInscrito> participantesInscritosInteressados) {
		this.participantesInscritosParaInteresse = participantesInscritosInteressados;
	}

	public String getTextoRodape1() {
		return String.format(textoRodape1, numeroParticipantes);
	}

	public String getTextoRodape2() {
		return String.format(textoRodape2, numeroParticipantes, vagasTotal, vagasDisponiveis);
	}

	public String getTextoRodape3() {
		return String.format(textoRodape3, numeroParticipantes, inscricoesAprovadas);
	}

}
