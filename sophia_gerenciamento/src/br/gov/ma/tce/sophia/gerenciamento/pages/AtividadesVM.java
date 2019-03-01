package br.gov.ma.tce.sophia.gerenciamento.pages;

import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.io.FilenameUtils;
import org.zkoss.bind.BindContext;
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
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Window;

import br.gov.ma.tce.gestores.server.beans.estado.Estado;
import br.gov.ma.tce.gestores.server.beans.estado.EstadoFacade;
import br.gov.ma.tce.gestores.server.beans.municipio.Municipio;
import br.gov.ma.tce.gestores.server.beans.municipio.MunicipioFacade;
import br.gov.ma.tce.sophia.ejb.beans.arquivo.Arquivo;
import br.gov.ma.tce.sophia.ejb.beans.arquivo.ArquivoFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.atividade.Atividade;
import br.gov.ma.tce.sophia.ejb.beans.atividade.AtividadeFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.atividadecolaborador.AtividadeColaboradorFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.avaliacaoreacao.AvaliacaoReacao;
import br.gov.ma.tce.sophia.ejb.beans.avaliacaoreacao.AvaliacaoReacaoFacadeBean;
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
import br.gov.ma.tce.sophia.ejb.beans.pessoa.Pessoa;
import br.gov.ma.tce.sophia.ejb.beans.pessoa.PessoaFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.tipoatividade.TipoAtividade;
import br.gov.ma.tce.sophia.ejb.beans.tipoatividade.TipoAtividadeFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.usuario.Usuario;
import br.gov.ma.tce.sophia.ejb.util.Filtro;
import br.gov.ma.tce.sophia.ejb.util.HashUtil;
import br.gov.ma.tce.sophia.ejb.vo.AvaliacaoReacaoVO;
import br.gov.ma.tce.sophia.ejb.vo.ListaPresencaVO;
import br.gov.ma.tce.sophia.gerenciamento.utils.ArquivoUtil;
import br.gov.ma.tce.sophia.gerenciamento.utils.Chart;
import br.gov.ma.tce.sophia.gerenciamento.utils.DataUtil;
import br.gov.ma.tce.sophia.gerenciamento.utils.EmailSOPHIA;
import br.gov.ma.tce.sophia.gerenciamento.utils.LongOperation;
import br.gov.ma.tce.sophia.gerenciamento.utils.Report;

public class AtividadesVM {
	private Atividade atividade, atividadeSelecionada, atividadeAvaliada;
	private Evento eventoSelecionado;
	private TipoAtividade tipoAtividade;
	private ParticipanteInscrito participanteInscrito;
	private Filtro filtroParticipante;
	private AvaliacaoReacao avaliacaoReacao;
	private Arquivo arquivo;

	private EmailSOPHIA emailSOPHIA;

	private DataUtil diaSelecionado;

	private File lista;
	private String categoria;
	private boolean gerenciarVagasVisible = false;
	private boolean adicionarTipoVisible = false;
	private boolean alterarEstadoVisible = false;
	private boolean atividadeEAD = false;
	private boolean diaOuHoraAlterados = false;
	private boolean distribuirHorasAutomatico = true;
	private String login;
	private String senha;
	private int espacosEmBranco = 0;
	private int vagasSociedade = 0, vagasServidor = 0, vagasJurisdicionado = 0;
	private static Integer vagasTotal = 0, vagasDisponiveis = 0, presentes = 0, ausentes = 0;
	private static final String textoRodape1 = "%d participante(s) encontrado(s) - Presentes: %d - Ausentes: %d";
	private static final String textoRodape2 = "%d participante(s) encontrado(s) - Total de vagas: %d - Vagas disponíveis: %d";
	private String legenda;
	private Media media;

	private Collection<Evento> eventos, eventosRealizados;
	private Collection<Atividade> atividades;
	private Collection<TipoAtividade> tipoDeAtividades;
	private Collection<Pessoa> participantes;
	private Collection<AvaliacaoReacao> avaliacoesReacao;
	private Collection<ParticipanteInscrito> participantesInscritos;
	private Collection<Estado> estados;
	private Collection<Municipio> municipios;
	private Collection<Frequencia> frequencias;
	private Collection<Frequencia> frequenciasComAlteracao;
	private Collection<DiasAtividade> diasAtividade;
	private Collection<Arquivo> arquivos;
	
	private Collection<DataUtil> dias;

	private AtividadeFacadeBean atividadeFacade;
	private EventoFacadeBean eventoFacade;
	private TipoAtividadeFacadeBean tipoAtividadeFacade;
	private PessoaFacadeBean pessoaFacade;
	private ParticipanteInteresseFacadeBean participanteInteresseFacade;
	private ParticipanteInscritoFacadeBean participanteInscritoFacade;
	private FrequenciaFacadeBean frequenciaFacade;
	private DiasAtividadeFacadeBean diasAtividadeFacade;
	private EstadoFacade estadoFacade;
	private MunicipioFacade municipioFacade;
	private AvaliacaoReacaoFacadeBean avaliacaoReacaoFacade;
	private AtividadeColaboradorFacadeBean atividadeColaboradorFacade;
	private ArquivoFacadeBean arquivoFacade;

	@Wire("#comboEventos")
	private Combobox comboEventos;

	@Wire("#comboAtividades")
	private Combobox comboAtividades;

	@Wire("#comboDias")
	private Combobox comboDias;

	@Wire("#comboMunicipios")
	private Combobox comboMunicipios;

	@Wire("#modalAtualizarAtividade #comboEstadosModal")
	private Combobox comboEstadosModal;

	@Wire("#modalAtualizarAtividade #comboMunicipiosModal")
	private Combobox comboMunicipiosModal;

	@Wire("#modalAtualizarAtividade")
	private Window modalAtualizarAtividade;

	@Wire("#modalCancelarAtividade")
	private Window modalCancelarAtividade;

	@Wire("#modalConfirmacaoCancelarAtividade")
	private Window modalConfirmacaoCancelarAtividade;

	@Wire("#modalConfirmacaoVagas")
	private Window modalConfirmacaoVagas;

	@Wire("#modalPresencas")
	private Window modalPresencas;

	@Wire("#modalConfirmacao")
	private Window modalConfirmacao;

	@Wire("#modalGerarPresenca")
	private Window modalGerarPresenca;

	@Wire("#modalConfirmacaoAbrirFecharInscricoes")
	private Window modalConfirmacaoAbrirFecharInscricoes;

	@Wire("#modalGerarPresenca #comboDiasGerarPresenca")
	private Combobox comboDiasGerarPresenca;

	@Wire("#modalVisualizaLista")
	private Window modalVisualizaLista;

	@Wire("#modalVisualizaLista #iframeLista")
	private Iframe iframeLista;

	@Wire("#modalAvaliacaoDeReacao")
	private Window modalAvaliacaoDeReacao;

	@Wire("#modalAvaliacaoDeReacaoIndividual")
	private Window modalAvaliacaoDeReacaoIndividual;

	@Wire("#modalVisualizaRelatorioAvaliacaoDeReacao")
	private Window modalVisualizaRelatorioAvaliacaoDeReacao;

	@Wire("#modalVisualizaRelatorioAvaliacaoDeReacao #iframeAvaliacaoReacao")
	private Iframe iframeAvaliacaoReacao;

	@Wire("#modalUploadArquivo")
	private Window modalUploadArquivo;

	@Wire("#modalVisualizarArquivos")
	private Window modalVisualizarArquivos;

	@Wire("#modalRemoverArquivo")
	private Window modalRemoverArquivo;

	public AtividadesVM() {
		try {
			InitialContext ctx = new InitialContext();
			atividadeFacade = (AtividadeFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/AtividadeFacadeBean!br.gov.ma.tce.sophia.ejb.beans.atividade.AtividadeFacadeBean");
			eventoFacade = (EventoFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/EventoFacadeBean!br.gov.ma.tce.sophia.ejb.beans.evento.EventoFacadeBean");
			tipoAtividadeFacade = (TipoAtividadeFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/TipoAtividadeFacadeBean!br.gov.ma.tce.sophia.ejb.beans.tipoatividade.TipoAtividadeFacadeBean");
			pessoaFacade = (PessoaFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/PessoaFacadeBean!br.gov.ma.tce.sophia.ejb.beans.pessoa.PessoaFacadeBean");
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
			avaliacaoReacaoFacade = (AvaliacaoReacaoFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/AvaliacaoReacaoFacadeBean!br.gov.ma.tce.sophia.ejb.beans.avaliacaoreacao.AvaliacaoReacaoFacadeBean");
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
		atividade = new Atividade();
		atividade.setModalidade("PRESENCIAL");
		atividade.setEstilo("GRATUITO");
		atividade.setGeraCertificado(true);

		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		atividade.setCargaHoraria(calendar.getTime());

		tipoDeAtividades = tipoAtividadeFacade.findAll();
		estados = estadoFacade.findAll();
		filtroParticipante = new Filtro();
		participantes = new ArrayList<Pessoa>();
		eventosRealizados = eventoFacade.findEventosByCategoria("REALIZADO");
		frequencias = new ArrayList<Frequencia>();
		frequenciasComAlteracao = new ArrayList<Frequencia>();
		emailSOPHIA = new EmailSOPHIA();
		countVagas();
	}

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}

	@Command
	@NotifyChange(".")
	private void countVagas() {
		if (atividadeSelecionada != null) {
			vagasTotal = atividadeSelecionada.getVagas();
			vagasDisponiveis = (int) (vagasTotal
					- participanteInscritoFacade.countInscricoesByAtividade(atividadeSelecionada));
			if (atividadeSelecionada.getVagasServidor() != null) {
				vagasSociedade = (int) (atividadeSelecionada.getVagasSociedade() - participanteInscritoFacade
						.countInscricoesByAtividadeAndTipoParticipante(atividadeSelecionada, "1"));
				vagasServidor = (int) (atividadeSelecionada.getVagasServidor() - participanteInscritoFacade
						.countInscricoesByAtividadeAndTipoParticipante(atividadeSelecionada, "2"));
				vagasJurisdicionado = (int) (atividadeSelecionada.getVagasJurisdicionado() - participanteInscritoFacade
						.countInscricoesByAtividadeAndTipoParticipante(atividadeSelecionada, "3"));
			}
		} else {
			vagasTotal = vagasDisponiveis = presentes = ausentes = vagasSociedade = vagasServidor = vagasJurisdicionado = 0;
		}
	}

	@Command
	@NotifyChange(".")
	public void modalidadeEAD() {
		atividadeEAD = (atividade.getModalidade().equals("A DISTÂNCIA (EAD)")) ? true : false;
	}

	@Command
	@NotifyChange(".")
	public void carregarListaPorEstado() {
		try {
			municipios = municipioFacade.findMunicipiosByEstado(atividade.getEstado().getEstadoId());
			atividade.setMunicipio(null);
			if (comboMunicipios != null) {
				comboMunicipios.setSelectedItem(null);
			}
			if (comboMunicipiosModal != null) {
				comboMunicipiosModal.setSelectedItem(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarEventosPorCategoria(@BindingParam("categoria") String categoria) {
		try {
			frequencias = new ArrayList<Frequencia>();
			atividades = null;
			eventos = eventoFacade.findEventosByCategoria(categoria);
			atividade.setEvento(null);
			eventoSelecionado = null;
			atividadeSelecionada = null;
			comboEventos.setSelectedItem(null);
			if (comboAtividades != null) {
				comboAtividades.setSelectedItem(null);
			}
			diaSelecionado = null;
			if (comboDias != null) {
				comboDias.setSelectedItem(null);
			}
			countVagas();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarListaPorEvento() {
		try {
			frequencias = new ArrayList<Frequencia>();
			atividades = atividadeFacade.findAtividadesByEvento(eventoSelecionado);
			atividadeSelecionada = null;
			diaSelecionado = null;
			if (comboDias != null) {
				comboDias.setSelectedItem(null);
			}
			dias = new ArrayList<DataUtil>();
			if (comboAtividades != null) {
				comboAtividades.setSelectedItem(null);
			}
			countVagas();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarDadosEvento() {
		try {
			Evento evento = atividade.getEvento();
			atividade.setDataInicio(evento.getDataInicio());
			atividade.setDataFim(evento.getDataFim());
			atividade.setHoraInicio(evento.getHoraInicio());
			atividade.setHoraFim(evento.getHoraFim());
			atividade.setLocal(evento.getLocal());
			atividade.setLogradouro(evento.getLogradouro());
			atividade.setBairro(evento.getBairro());
			atividade.setNumero(evento.getNumero());
			atividade.setComplemento(evento.getComplemento());
			atividade.setCep(evento.getCep());
			atividade.setEstado(evento.getEstado());
			carregarListaPorEstado();
			carregarListaDeDias();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarListaPorAtividade() {
		try {
			if (comboDias == null && comboDiasGerarPresenca == null) {
				countVagas();
			} else {
				presentes = ausentes = 0;
				frequencias = new ArrayList<Frequencia>();
				dias = new ArrayList<DataUtil>();
				diasAtividade = diasAtividadeFacade.findDiasByAtividade(atividadeSelecionada);
				for (DiasAtividade da : diasAtividade) {
					DataUtil dataUtil = new DataUtil(da.getDia(), da.getQuantidadeHoras(), true);
					dias.add(dataUtil);
				}

				diaSelecionado = null;
				if (comboDias != null) {
					comboDias.setSelectedItem(null);
				}
				if (comboDiasGerarPresenca != null) {
					comboDiasGerarPresenca.setSelectedItem(null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarListaDeDias() {
		try {
			dias = new ArrayList<DataUtil>();
			if (atividade.getDataInicio() != null && atividade.getDataFim() != null) {
				Calendar inicio = Calendar.getInstance();
				Calendar fim = Calendar.getInstance();
				inicio.setTime(atividade.getDataInicio());
				fim.setTime(atividade.getDataFim());
				for (Date data = inicio.getTime(); !inicio.after(fim); inicio.add(Calendar.DATE,
						1), data = inicio.getTime()) {
					DataUtil dataUtil = new DataUtil(data, null, true);
					dias.add(dataUtil);
				}

				carregarIntervalosPorDia();
				diaOuHoraAlterados = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void distribuirHorasAutomatico(@BindingParam("zerarIntervalos") boolean zerarIntervalos) {
		try {
			if (zerarIntervalos) {
				distribuirHorasAutomatico = !distribuirHorasAutomatico;
				if (distribuirHorasAutomatico) {
					carregarIntervalosPorDia();
				} else {
					if (dias != null && !dias.isEmpty()) {
						for (DataUtil du : dias) {
							du.setQuantidadeHoras(null);
						}
					}
				}
			} else {
				distribuirHorasAutomatico = false;
			}
			diaOuHoraAlterados = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarIntervalosPorDia() {
		try {
			// Divide a carga horária em intervalos iguais pra cada dia de atividade
			if (distribuirHorasAutomatico && atividade.getCargaHoraria() != null && dias != null && !dias.isEmpty()) {
				int diasSelecionados = 0;
				for (DataUtil du : dias) {
					if (du.getSelecionado()) {
						diasSelecionados++;
					}
				}
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(atividade.getCargaHoraria());

				int minutosPorDia, horas, minutos;
				if (calendar.get(Calendar.HOUR_OF_DAY) >= 1) {
					minutosPorDia = (int) Math
							.round(((calendar.get(Calendar.HOUR_OF_DAY) * 60) + calendar.get(Calendar.MINUTE))
									/ (double) diasSelecionados);
					horas = minutosPorDia / 60;
					minutos = minutosPorDia % 60;
				} else {
					horas = 0;
					minutos = (int) Math.round(calendar.get(Calendar.MINUTE) / (double) diasSelecionados);
				}

				calendar.clear();
				calendar.set(Calendar.HOUR_OF_DAY, horas);
				calendar.set(Calendar.MINUTE, minutos);
				for (DataUtil du : dias) {
					if (du.getSelecionado()) {
						du.setQuantidadeHoras(calendar.getTime());
					} else {
						du.setQuantidadeHoras(null);
					}
				}
				diaOuHoraAlterados = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void selecionaDia(@BindingParam("dia") DataUtil dia) {
		try {
			dia.setSelecionado(!dia.getSelecionado());
			carregarIntervalosPorDia();
			diaOuHoraAlterados = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarListaPorDia() {
		try {
			frequencias = frequenciaFacade.findFrequenciasUnicasByAtividadeAndDia(atividadeSelecionada,
					diaSelecionado.getData());
			presentes = Math.toIntExact(frequenciaFacade.countFrequenciasUnicasByAtividadeAndDia(atividadeSelecionada,
					diaSelecionado.getData()));
			ausentes = frequencias.size() - presentes;
			frequenciasComAlteracao = new ArrayList<Frequencia>();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmaGerenciarVagas(@BindingParam("visible") boolean visible) {
		gerenciarVagasVisible = false;
		if (atividade.getTipoParticipante().equals("7") || atividade.getTipoParticipante().equals("4")
				|| atividade.getTipoParticipante().equals("5") || atividade.getTipoParticipante().equals("6")) {
			if (visible) {
				modalConfirmacaoVagas.setVisible(visible);
			} else {
				atividade.setVagasSociedade(null);
				atividade.setVagasServidor(null);
				atividade.setVagasJurisdicionado(null);
				modalConfirmacaoVagas.setVisible(visible);
			}
		} else {
			atividade.setVagasSociedade(null);
			atividade.setVagasServidor(null);
			atividade.setVagasJurisdicionado(null);
		}
	}

	@Command
	@NotifyChange(".")
	public void gerenciarQuantidadeDeVagas() {
		atividade.setVagasJurisdicionado(0);
		atividade.setVagasServidor(0);
		atividade.setVagasSociedade(0);
		gerenciarVagasVisible = true;
		modalConfirmacaoVagas.setVisible(false);
	}

	/**
	 * Tipos de participante em uma atividade e seus significados: 1 = TODOS, 2 =
	 * apenas SERVIDOR DO TCE-MA, 3 = apenas JURIDISCIONADO, 4 = SERVIDOR DO TCE-MA
	 * e JURIDISCIONADO, 5 = SERVIDOR DO TCE-MA e SOCIEDADE, 6=JURISDICIONADO e
	 * SOCIEDADE, 7 = apenas SOCIEDADE
	 */
	@Command
	@NotifyChange(".")
	public void abrirModalConfirmaCriarAtividade(@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				String verificaIntervalos = null;
				consertaDados(atividade);
				if ((atividade.getEvento() == null || (atividade.getNome() == null || atividade.getNome().equals(""))
						|| (atividade.getDescricao() == null || atividade.getDescricao().equals(""))
						|| atividade.getTipoAtividade() == null || atividade.getEstilo() == null
						|| atividade.getModalidade() == null || atividade.getVagas() == null
						|| atividade.getTipoParticipante() == null || atividade.getCargaHoraria() == null
						|| atividade.getDataInicio() == null || atividade.getDataFim() == null
						|| atividade.getPreInscricaoDataInicio() == null || atividade.getPreInscricaoDataFim() == null
						|| atividade.getGeraCertificado() == null)
						|| (atividade.getModalidade().equals("PRESENCIAL")
								&& (atividade.getHoraInicio() == null || atividade.getHoraFim() == null
										|| (atividade.getLocal() == null || atividade.getLocal().equals(""))
										|| (atividade.getLogradouro() == null || atividade.getLogradouro().equals(""))
										|| (atividade.getBairro() == null || atividade.getBairro().equals(""))
										|| (atividade.getNumero() == null || atividade.getNumero().equals(""))
										|| atividade.getCep() == null || atividade.getEstado() == null
										|| atividade.getMunicipio() == null))
						|| (atividade.getTipoParticipante().equals("7") && isGerenciarVagasVisible()
								&& (atividade.getVagasSociedade() == null || atividade.getVagasServidor() == null
										|| atividade.getVagasJurisdicionado() == null))
						|| (atividade.getTipoParticipante().equals("4") && isGerenciarVagasVisible()
								&& (atividade.getVagasServidor() == null || atividade.getVagasJurisdicionado() == null))
						|| (atividade.getTipoParticipante().equals("5") && isGerenciarVagasVisible()
								&& (atividade.getVagasServidor() == null || atividade.getVagasSociedade() == null))
						|| (atividade.getTipoParticipante().equals("6") && isGerenciarVagasVisible()
								&& (atividade.getVagasJurisdicionado() == null
										|| atividade.getVagasSociedade() == null))) {
					throw new Exception("Todos os campos obrigatórios precisam ser preechidos corretamente.");
				} else if (atividade.getTipoParticipante().equals("7") && isGerenciarVagasVisible()
						&& (atividade.getVagas() != (atividade.getVagasSociedade() + atividade.getVagasServidor()
								+ atividade.getVagasJurisdicionado()))) {
					throw new Exception(
							"A quantidade total de vagas não coincide com as somas das vagas por tipo de participante.");
				} else if ((atividade.getTipoParticipante().equals("4") && isGerenciarVagasVisible()
						&& (atividade
								.getVagas() != (atividade.getVagasServidor() + atividade.getVagasJurisdicionado())))
						|| (atividade.getTipoParticipante().equals("5") && isGerenciarVagasVisible()
								&& (atividade
										.getVagas() != (atividade.getVagasServidor() + atividade.getVagasSociedade())))
						|| (atividade.getTipoParticipante().equals("6") && isGerenciarVagasVisible() && (atividade
								.getVagas() != (atividade.getVagasJurisdicionado() + atividade.getVagasSociedade())))) {
					throw new Exception(
							"A quantidade total de vagas não coincide com as somas das vagas por tipo de participante.");
				} else if (atividade.getTipoParticipante().equals("7") && isGerenciarVagasVisible()
						&& (atividade.getVagasSociedade() < 1 || atividade.getVagasServidor() < 1
								|| atividade.getVagasJurisdicionado() < 1)) {
					throw new Exception("A quantidade de vagas por tipo de participante precisa ser maior que zero.");
				} else if (atividade.getTipoParticipante().equals("4") && isGerenciarVagasVisible()
						&& (atividade.getVagasServidor() < 1 || atividade.getVagasJurisdicionado() < 1)) {
					throw new Exception("A quantidade de vagas por tipo de participante precisa ser maior que zero.");

				} else if (atividade.getTipoParticipante().equals("5") && isGerenciarVagasVisible()
						&& (atividade.getVagasSociedade() < 1 || atividade.getVagasServidor() < 1
								|| atividade.getVagasSociedade() < 1)) {
					throw new Exception("A quantidade de vagas por tipo de participante precisa ser maior que zero.");
				} else if (atividade.getTipoParticipante().equals("6") && isGerenciarVagasVisible()
						&& (atividade.getVagasSociedade() < 1 || atividade.getVagasJurisdicionado() < 1)) {
					throw new Exception("A quantidade de vagas por tipo de participante precisa ser maior que zero.");

				} else if ((verificaIntervalos = verificaIntervalos()) != null) {
					throw new Exception(verificaIntervalos);
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
	public void criarAtividade() {
		try {
			if ((atividade = atividadeFacade.include(atividade)) != null) {
				for (DataUtil du : dias) {
					if (du.getSelecionado()) {
						DiasAtividade da = new DiasAtividade();
						da.setAtividade(atividade);
						da.setDia(du.getData());
						da.setQuantidadeHoras(du.getQuantidadeHoras());
						diasAtividadeFacade.include(da);
					}
				}

				modalConfirmacao.setVisible(false);
				Clients.showNotification("Atividade criada com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null, null,
						3000, true);

				// Aproveitando dados de atividade já criada para outras atividades (evitando
				// digitar tudo de novo)
				atividade.setAtividadeId(null);
				atividade.setNome(null);
			}
		} catch (Exception e) {
			Clients.showNotification("Ocorreu um erro ao criar a atividade. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalAtualizarAtividade(@BindingParam("atividade") Atividade atividade,
			@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				this.atividade = atividade;
				modalidadeEAD();

				dias = new ArrayList<DataUtil>();
				DiasAtividade diasAtividade;

				if (atividade.getDataInicio() != null && atividade.getDataFim() != null) {
					Calendar inicio = Calendar.getInstance();
					Calendar fim = Calendar.getInstance();
					inicio.setTime(atividade.getDataInicio());
					fim.setTime(atividade.getDataFim());
					for (Date data = inicio.getTime(); !inicio.after(fim); inicio.add(Calendar.DATE,
							1), data = inicio.getTime()) {
						if ((diasAtividade = diasAtividadeFacade.findDiasByDiaAndAtividade(data, atividade)) == null) {
							DataUtil dataUtil = new DataUtil(data, null, false);
							dias.add(dataUtil);
						} else {
							DataUtil dataUtil = new DataUtil(data, diasAtividade.getQuantidadeHoras(), true);
							dias.add(dataUtil);
						}
					}
				}

				if (atividade.getVagasServidor() != null) {
					gerenciarVagasVisible = true;
				}
				alterarEstadoVisible = false;
				comboEstadosModal.setSelectedItem(null);
				comboMunicipiosModal.setSelectedItem(null);
				modalAtualizarAtividade.setVisible(visible);
			} else {
				gerenciarVagasVisible = false;
				adicionarTipoVisible = false;
				atividades = atividadeFacade.findAtividadesByEvento(eventoSelecionado);
				modalAtualizarAtividade.setVisible(visible);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void exibirOpcoesEditarEstado() {
		alterarEstadoVisible = true;
		atividade.setEstado(null);
		atividade.setMunicipio(null);
		comboEstadosModal.setSelectedItem(null);
		comboMunicipiosModal.setSelectedItem(null);
		municipios = null;
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmaAtualizarAtividade(@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				String verificaIntervalos = null;
				consertaDados(atividade);
				if ((atividade.getEvento() == null || (atividade.getNome() == null || atividade.getNome().equals(""))
						|| (atividade.getDescricao() == null || atividade.getDescricao().equals(""))
						|| atividade.getTipoAtividade() == null || atividade.getEstilo() == null
						|| atividade.getModalidade() == null || atividade.getVagas() == null
						|| atividade.getTipoParticipante() == null || atividade.getCargaHoraria() == null
						|| atividade.getDataInicio() == null || atividade.getDataFim() == null
						|| atividade.getPreInscricaoDataInicio() == null || atividade.getPreInscricaoDataFim() == null
						|| atividade.getGeraCertificado() == null)
						|| (atividade.getModalidade().equals("PRESENCIAL")
								&& (atividade.getHoraInicio() == null || atividade.getHoraFim() == null
										|| (atividade.getLocal() == null || atividade.getLocal().equals(""))
										|| (atividade.getLogradouro() == null || atividade.getLogradouro().equals(""))
										|| (atividade.getBairro() == null || atividade.getBairro().equals(""))
										|| (atividade.getNumero() == null || atividade.getNumero().equals(""))
										|| atividade.getCep() == null || atividade.getEstado() == null
										|| atividade.getMunicipio() == null))
						|| (atividade.getTipoParticipante().equals("7") && isGerenciarVagasVisible()
								&& (atividade.getVagasSociedade() == null || atividade.getVagasServidor() == null
										|| atividade.getVagasJurisdicionado() == null))
						|| (atividade.getTipoParticipante().equals("4") && isGerenciarVagasVisible()
								&& (atividade.getVagasServidor() == null || atividade.getVagasJurisdicionado() == null))
						|| (atividade.getTipoParticipante().equals("5") && isGerenciarVagasVisible()
								&& (atividade.getVagasServidor() == null || atividade.getVagasSociedade() == null))
						|| (atividade.getTipoParticipante().equals("6") && isGerenciarVagasVisible()
								&& (atividade.getVagasJurisdicionado() == null
										|| atividade.getVagasSociedade() == null))) {
					throw new Exception("Todos os campos obrigatórios precisam ser preechidos corretamente.");
				} else if (atividade.getTipoParticipante().equals("7") && isGerenciarVagasVisible()
						&& (atividade.getVagas() != (atividade.getVagasSociedade() + atividade.getVagasServidor()
								+ atividade.getVagasJurisdicionado()))) {
					throw new Exception(
							"A quantidade total de vagas não coincide com as somas das vagas por tipo de participante.");
				} else if ((atividade.getTipoParticipante().equals("4") && isGerenciarVagasVisible()
						&& (atividade
								.getVagas() != (atividade.getVagasServidor() + atividade.getVagasJurisdicionado())))
						|| (atividade.getTipoParticipante().equals("5") && isGerenciarVagasVisible()
								&& (atividade
										.getVagas() != (atividade.getVagasServidor() + atividade.getVagasSociedade())))
						|| (atividade.getTipoParticipante().equals("6") && isGerenciarVagasVisible() && (atividade
								.getVagas() != (atividade.getVagasJurisdicionado() + atividade.getVagasSociedade())))) {
					throw new Exception(
							"A quantidade total de vagas não coincide com as somas das vagas por tipo de participante.");
				} else if (atividade.getTipoParticipante().equals("7") && isGerenciarVagasVisible()
						&& (atividade.getVagasSociedade() < 1 || atividade.getVagasServidor() < 1
								|| atividade.getVagasJurisdicionado() < 1)) {
					throw new Exception("A quantidade de vagas por tipo de participante precisa ser maior que zero.");
				} else if (atividade.getTipoParticipante().equals("4") && isGerenciarVagasVisible()
						&& (atividade.getVagasServidor() < 1 || atividade.getVagasJurisdicionado() < 1)) {
					throw new Exception("A quantidade de vagas por tipo de participante precisa ser maior que zero.");
				} else if (atividade.getTipoParticipante().equals("5") && isGerenciarVagasVisible()
						&& (atividade.getVagasSociedade() < 1 || atividade.getVagasServidor() < 1
								|| atividade.getVagasSociedade() < 1)) {
					throw new Exception("A quantidade de vagas por tipo de participante precisa ser maior que zero.");
				} else if (atividade.getTipoParticipante().equals("6") && isGerenciarVagasVisible()
						&& (atividade.getVagasSociedade() < 1 || atividade.getVagasJurisdicionado() < 1)) {
					throw new Exception("A quantidade de vagas por tipo de participante precisa ser maior que zero.");
				} else if ((verificaIntervalos = verificaIntervalos()) != null) {
					throw new Exception(verificaIntervalos);
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
	public void atualizarAtividade() {
		try {
			if ((atividade = atividadeFacade.update(atividade)) != null) {
				// Se a data início ou data fim da atividade forem alteradas,
				// Atualiza a tabela dias_atividade e frequencia
				if (diaOuHoraAlterados) {
					alteracaoDeDatas();
				}

				diaOuHoraAlterados = false;
				modalConfirmacao.setVisible(false);
				modalAtualizarAtividade.setVisible(false);
				Clients.showNotification("Atividade atualizada com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null,
						null, 3000, true);
			} else {
				throw new Exception("Ocorreu um erro ao atualizar essa atividade. Tente novamente mais tarde.");
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@NotifyChange(".")
	private void alteracaoDeDatas() {
		try {
			diasAtividade = diasAtividadeFacade.findAllDiasByAtividade(atividade);
			for (DiasAtividade da : diasAtividade) {
				boolean encontrado = false;
				for (Iterator<DataUtil> iterator = dias.iterator(); iterator.hasNext();) {
					DataUtil du = iterator.next();
					if (du.getData().equals(da.getDia()) && du.getSelecionado()) {
						DiasAtividade diasAtividade = diasAtividadeFacade.findAnyDiasByDiaAndAtividade(du.getData(),
								atividade);
						diasAtividade.setQuantidadeHoras(du.getQuantidadeHoras());
						diasAtividadeFacade.update(diasAtividade);
						encontrado = true;
						iterator.remove();
						break;
					}
				}
				if (!encontrado) {
					// Seta quantidade de horas como null para evitar perda de dados
					// em dias_atividade e frequencia
					// Dias com quantidade_horas null indicam que esse dia estava previamente
					// registrado
					// mas foi removido da lista de dias de uma atividade
					da.setQuantidadeHoras(null);
					diasAtividadeFacade.update(da);
				}
			}

			// Insere novos dias que não estavam anteriormente
			// E também novas frequências
			for (DataUtil du : dias) {
				if (du.getSelecionado()) {
					DiasAtividade da = new DiasAtividade();
					da.setAtividade(atividade);
					da.setDia(du.getData());
					da.setQuantidadeHoras(du.getQuantidadeHoras());
					diasAtividadeFacade.include(da);

					participantesInscritos = participanteInscritoFacade
							.findParticipantesByAtividadeAndInscricaoAprovada(atividade, true);
					for (ParticipanteInscrito pi : participantesInscritos) {
						Frequencia frequencia = new Frequencia();
						frequencia.setDiasAtividade(da);
						frequencia.setParticipanteInscrito(pi);
						frequencia.setPresenca(false);

						if (frequenciaFacade.include(frequencia) == null) {
							// Se a inclusão de alguma frequência falhar,
							// remove as anteriores já inclusas
							// Mas o poder divino não deixará que algum erro ocorra
							Collection<Frequencia> frequencias = frequenciaFacade
									.findFrequenciasByParticipanteInscrito(pi);
							for (Frequencia f : frequencias) {
								frequenciaFacade.delete(f.getFrequenciaId());
							}
							break;
						}
					}
				}
			}

			// Recalcula presença (%) de cada participante inscrito
			diasAtividade = diasAtividadeFacade.findDiasByAtividade(atividade);
			participantesInscritos = participanteInscritoFacade
					.findParticipantesByAtividadeAndInscricaoAprovada(atividade, true);
			for (ParticipanteInscrito pi : participantesInscritos) {
				pi.setFrequencia(0.0);
				for (Frequencia f : frequenciaFacade.findFrequenciasValidasByParticipanteInscrito(pi)) {
					if (f.getPresenca()) {
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(atividade.getCargaHoraria());
						double minutosCargaHoraria = (int) Math
								.round((calendar.get(Calendar.HOUR_OF_DAY) * 60) + calendar.get(Calendar.MINUTE));

						Calendar intervalo = Calendar.getInstance();
						intervalo.setTime(f.getDiasAtividade().getQuantidadeHoras());
						double minutosDia = (intervalo.get(Calendar.HOUR_OF_DAY) * 60) + intervalo.get(Calendar.MINUTE);

						pi.setFrequencia(pi.getFrequencia() + ((minutosDia / minutosCargaHoraria) * 100));
					}
				}
				participanteInscritoFacade.update(pi);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@NotifyChange(".")
	private String verificaIntervalos() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(atividade.getCargaHoraria());
		int minutosCargaHoraria = (int) Math
				.round((calendar.get(Calendar.HOUR_OF_DAY) * 60) + calendar.get(Calendar.MINUTE));
		boolean intervaloZerado = false;

		int minutosSomados = 0;
		for (DataUtil du : dias) {
			if (du.getSelecionado()) {
				Calendar intervalo = Calendar.getInstance();
				if (du.getQuantidadeHoras() != null) {
					intervalo.setTime(du.getQuantidadeHoras());
					int minutos = (intervalo.get(Calendar.HOUR_OF_DAY) * 60) + intervalo.get(Calendar.MINUTE);
					if (minutos == 0) {
						intervaloZerado = true;
					}
					minutosSomados = minutosSomados + minutos;
				} else {
					intervaloZerado = true;
				}
			}
		}

		// Compara a soma dos intervalos da carga horária de cada dia com a carga
		// horária total com tolerância de +- 2 minutos
		if ((minutosCargaHoraria + 2) < minutosSomados || minutosSomados < (minutosCargaHoraria - 2)) {
			return "A soma de horas de cada dia de atividade não coincide com a carga horária.";
		} else if (intervaloZerado) {
			return "Um ou mais dias selecionados constam com quantidade vazia de horas.";
		}
		return null;
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmacaoAbrirFecharInscricoes(@BindingParam("atividade") Atividade atividade,
			@BindingParam("visible") Boolean visible) {
		try {
			if (visible) {
				this.atividade = atividade;
				modalConfirmacaoAbrirFecharInscricoes.setVisible(visible);
			} else {
				modalConfirmacaoAbrirFecharInscricoes.setVisible(visible);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirFecharInscricoes() {
		try {
			atividade.setInscricoesFechadas(!atividade.getInscricoesFechadas());
			if (atividadeFacade.update(atividade) != null) {
				modalConfirmacaoAbrirFecharInscricoes.setVisible(false);
				if (atividade.getInscricoesFechadas()) {
					Clients.showNotification("Inscrições fechadas com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null,
							null, 3000, true);
				} else {
					Clients.showNotification("Inscrições abertas com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null,
							null, 3000, true);
				}
			} else {
				throw new Exception("Ocorreu um erro ao realizar a operação. Tente novamente mais tarde.");
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalCancelarAtividade(@BindingParam("atividade") Atividade atividade,
			@BindingParam("visible") Boolean visible) {
		if (visible) {
			this.atividade = atividade;
			modalCancelarAtividade.setVisible(visible);
		} else {
			modalCancelarAtividade.setVisible(visible);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmacaoCancelarAtividade(@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				if (atividade.getMotivoCancelamento() == null || atividade.getMotivoCancelamento().trim().equals("")) {
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
					modalConfirmacaoCancelarAtividade.setVisible(visible);
				}
			} else {
				modalConfirmacaoCancelarAtividade.setVisible(visible);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void cancelarAtividade() {
		try {
			atividade.setAtividadeCancelada(true);
			if (atividadeFacade.update(atividade) != null) {
				if (atividade.getEvento().getCategoria().equals("PREVISTO")) {
					Collection<ParticipanteInteresse> destinatarios = participanteInteresseFacade
							.findListaDeInteresseByAtividade(atividade);
					new LongOperation() {
						@Override
						protected void execute() throws InterruptedException {
							emailSOPHIA.emailEventoPrevistoCancelado((ArrayList<ParticipanteInteresse>) destinatarios,
									false, " ");
						}
					}.start();
				} else {
					Collection<ParticipanteInscrito> destinatarios = participanteInscritoFacade
							.findParticipantesByAtividade(atividade);
					new LongOperation() {
						@Override
						protected void execute() throws InterruptedException {
							emailSOPHIA.emailEventoAbertoOuEmAndamentoCancelado(
									(ArrayList<ParticipanteInscrito>) destinatarios, false, " ");
						}
					}.start();
				}

				modalConfirmacaoCancelarAtividade.setVisible(false);
				modalCancelarAtividade.setVisible(false);
				Clients.showNotification("Atividade cancelada com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null, null,
						3000, true);
			} else {
				throw new Exception("Ocorreu um erro ao cancelar a atividade. Tente novamente mais tarde.");
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void exibirOpcoesAdicionarNovoTipo() {
		adicionarTipoVisible = true;
		tipoAtividade = new TipoAtividade();
	}

	@Command
	@NotifyChange(".")
	public void adicionarNovoTipo() {
		try {
			if (tipoAtividade.getNome() == null || tipoAtividade.getNome().equals("")) {
				throw new Exception("Informe o novo tipo de atividade.");
			}
			if (tipoAtividadeFacade.include(tipoAtividade) != null) {
				adicionarTipoVisible = false;
				tipoDeAtividades = tipoAtividadeFacade.findAll();
				Clients.showNotification("Novo tipo de atividade adicionado com sucesso!",
						Clients.NOTIFICATION_TYPE_INFO, null, null, 3000, true);
			} else {
				throw new Exception("Ocorreu um erro ao adicionar novo tipo. Tente novamente mais tarde.");
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalGerarPresenca(@BindingParam("atividade") Atividade atividade,
			@BindingParam("visible") boolean visible) {
		try {
			espacosEmBranco = 0;
			diaSelecionado = null;
			comboDiasGerarPresenca.setSelectedItem(null);
			if (visible) {
				atividadeSelecionada = atividade;
				carregarListaPorAtividade();
				modalGerarPresenca.setVisible(visible);
			} else {
				modalGerarPresenca.setVisible(visible);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalVisualizaLista(@BindingParam("visible") Boolean visible) {
		try {
			if (diaSelecionado == null) {
				throw new Exception("Selecione o dia para gerar a lista de presença.");
			} else {
				if (visible) {
					String path = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
					participantesInscritos = participanteInscritoFacade
							.findParticipantesByAtividadeAndInscricaoAprovada(atividadeSelecionada, true);
					if (participantesInscritos.isEmpty()) {
						throw new Exception("Não foram encontrados participantes inscritos com a "
								+ "inscrição aprovada. Aprove alguma inscrição antes de "
								+ "gerar a lista de presença.");
					}
					List<ListaPresencaVO> listaPresenca = ListaPresencaVO.preencheLista(participantesInscritos,
							espacosEmBranco);

					Map<String, Object> parameters = new HashMap<String, Object>();
					parameters.put("DATA_LISTA", diaSelecionado.getDataStr());
					parameters.put("EVENTO_NOME", atividadeSelecionada.getEvento().getNome());
					parameters.put("ATIVIDADE_NOME", atividadeSelecionada.getNome());
					parameters.put("IMAGE_DIR", path + "/imagens");

					String filename = "lista_presenca.jasper";
					lista = Report.getReportListaPresenca(path + "/jasper", listaPresenca, filename, parameters);
					iframeLista.setContent(new AMedia(lista, "application/pdf", null));
					modalVisualizaLista.setVisible(visible);
				} else {
					lista = null;
					iframeLista.setContent(null);
					modalVisualizaLista.setVisible(visible);
				}
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 4000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmaInscreverParticipante(@BindingParam("participante") Pessoa participante,
			@BindingParam("visible") Boolean visible) {
		try {
			if (visible) {
				if (atividadeSelecionada == null) {
					throw new Exception("Selecione a atividade que deseja realizar a inscrição.");
				} else if (atividadeSelecionada.isAtividadeCancelada()) {
					throw new Exception("Atividade cancelada.");
				} else if (participanteInscritoFacade.findByParticipanteAndAtividade(participante,
						atividadeSelecionada) != null) {
					throw new Exception("Participante já inscrito nessa atividade.");
				} else if (!atividadeSelecionada.getTipoParticipante().equals("1")) {
					if (atividadeSelecionada.getTipoParticipante().equals("2")
							&& (participante.getTipoPessoa().equals("1") || participante.getTipoPessoa().equals("3"))) {
						throw new Exception("Participante não atende ao público-alvo dessa atividade.");
					} else if (atividadeSelecionada.getTipoParticipante().equals("3")
							&& (participante.getTipoPessoa().equals("1") || participante.getTipoPessoa().equals("2")
									|| participante.getTipoPessoa().equals("4"))) {
						throw new Exception("Participante não atende ao público-alvo dessa atividade.");
					} else if (atividadeSelecionada.getTipoParticipante().equals("4")
							&& (participante.getTipoPessoa().equals("1"))) {
						throw new Exception("Participante não atende ao público-alvo dessa atividade.");
					}
				} else if (vagasDisponiveis == 0) {
					throw new Exception("Não há mais vagas disponíveis para essa atividade. "
							+ "Aumente o número de vagas ou remova alguma inscrição para dar lugar a essa.");
				} else if (atividadeSelecionada.getVagasServidor() != null) {
					if (participante.getTipoParticipanteStr().equals("SOCIEDADE")) {
						if (vagasSociedade == 0) {
							throw new Exception(
									"Não há mais vagas disponíveis para essa atividade para o público SOCIEDADE. "
											+ "Aumente o número de vagas ou remova alguma inscrição para dar lugar a essa.");
						}
					} else if (participante.getTipoParticipanteStr().equals("SERVIDOR DO TCE-MA")) {
						if (vagasServidor == 0) {
							throw new Exception("Não há mais vagas disponíveis para essa atividade para o público "
									+ "SERVIDOR DO TCE-MA. Aumente o número de vagas ou remova alguma inscrição "
									+ "para dar lugar a essa.");
						}
					} else {
						if (vagasJurisdicionado == 0) {
							throw new Exception("Não há mais vagas disponíveis para essa atividade para o público "
									+ "JURISDICIONADO. Aumente o número de vagas ou remova alguma inscrição para "
									+ "dar lugar a essa.");
						}
					}
				}

				participanteInscrito = new ParticipanteInscrito();
				participanteInscrito.setParticipante(participante);
				participanteInscrito.setDtPreInscricao(new Date());
				participanteInscrito.setAtividade(atividadeSelecionada);
				participanteInscrito.setFrequencia(0.0);
				participanteInscrito.setInscricaoAprovada(true);
				participanteInscrito.setCertificadoDisponivel(false);
				participanteInscrito.setAvaliacaoPreenchida(false);
				modalConfirmacao.setVisible(visible);
			} else {
				modalConfirmacao.setVisible(visible);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 4000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void inscreverParticipante() {
		try {
			if (participanteInscritoFacade.include(participanteInscrito) != null) {
				diasAtividade = diasAtividadeFacade.findDiasByAtividade(atividadeSelecionada);
				for (DiasAtividade da : diasAtividade) {
					Frequencia frequencia = new Frequencia();
					frequencia.setDiasAtividade(da);
					frequencia.setParticipanteInscrito(participanteInscrito);
					frequencia.setPresenca(false);

					if (frequenciaFacade.include(frequencia) == null) {
						// Se a inclusão de alguma frequência falhar,
						// remove as anteriores já inclusas
						Collection<Frequencia> frequencias = frequenciaFacade
								.findFrequenciasByParticipanteInscrito(participanteInscrito);
						for (Frequencia f : frequencias) {
							frequenciaFacade.delete(f.getFrequenciaId());
						}
						// E também anula a inscrição já aprovada
						participanteInscrito.setFrequencia(null);
						participanteInscrito.setInscricaoAprovada(null);
						participanteInscrito.setCertificadoDisponivel(null);
						participanteInscritoFacade.update(participanteInscrito);
						break;
					}
				}

				ParticipanteInscrito destinatario = new ParticipanteInscrito();
				destinatario.setParticipante(participanteInscrito.getParticipante());
				destinatario.setAtividade(participanteInscrito.getAtividade());
				new LongOperation() {
					@Override
					protected void execute() throws InterruptedException {
						emailSOPHIA.emailParticipanteInscrito(destinatario.getParticipante(),
								destinatario.getAtividade(), " ");
					}
				}.start();

				countVagas();
				modalConfirmacao.setVisible(false);
				Clients.showNotification("Inscrição realizada com sucesso! "
						+ (atividadeSelecionada.getEvento().getCategoria().equals("REALIZADO")
								? "Lembre-se de registrar a(s) presença(s) desse participante na atividade selecionada."
								: ""),
						Clients.NOTIFICATION_TYPE_INFO, null, null, 5000, true);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void gerenciarPresenca(@BindingParam("frequencia") Frequencia frequencia) {
		try {
			frequencia.setPresenca(!frequencia.getPresenca());
			if (!frequenciasComAlteracao.contains(frequencia)) {
				frequenciasComAlteracao.add(frequencia);
			}
			if (frequencia.getPresenca()) {
				presentes++;
				ausentes--;
			} else {
				presentes--;
				ausentes++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void confirmarRemoverPresencas() {
		try {
			if (!frequencias.isEmpty()) {
				boolean status = frequencias.iterator().next().getPresenca() ? false : true;
				for (Frequencia f : frequencias) {
					if (!f.getParticipanteInscrito().getCertificadoDisponivel()) {
						f.setPresenca(status);
						if (!frequenciasComAlteracao.contains(f)) {
							frequenciasComAlteracao.add(f);
						}

					}
				}
				if (status) {
					presentes = frequencias.size();
					ausentes = 0;
				} else {
					presentes = 0;
					ausentes = frequencias.size();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmaAlteracoes(@BindingParam("visible") Boolean visible) {
		modalConfirmacao.setVisible(visible);
	}

	@Command
	@NotifyChange(".")
	public void confirmarAlteracoes() {
		try {
			for (Frequencia f : frequenciasComAlteracao) {
				ParticipanteInscrito pi = f.getParticipanteInscrito();
				Double frequencia = pi.getFrequencia();

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(atividadeSelecionada.getCargaHoraria());
				double minutosCargaHoraria = (int) Math
						.round((calendar.get(Calendar.HOUR_OF_DAY) * 60) + calendar.get(Calendar.MINUTE));

				Calendar intervalo = Calendar.getInstance();
				intervalo.setTime(f.getDiasAtividade().getQuantidadeHoras());
				double minutosDia = (intervalo.get(Calendar.HOUR_OF_DAY) * 60) + intervalo.get(Calendar.MINUTE);

				if (f.getPresenca()) {
					if (!frequenciaFacade.findByPrimaryKey(f.getFrequenciaId()).getPresenca()) {
						pi.setFrequencia(frequencia + ((minutosDia / minutosCargaHoraria) * 100));
					}
				} else if (pi.getFrequencia() > 0.0) {
					if (frequenciaFacade.findByPrimaryKey(f.getFrequenciaId()).getPresenca()) {
						pi.setFrequencia(frequencia - ((minutosDia / minutosCargaHoraria) * 100));
						if (pi.getFrequencia() < 0.0) {
							pi.setFrequencia(0.0);
						}
					}
				}

				if (frequenciaFacade.update(f) == null) {
					f.setPresenca(false);
					frequenciaFacade.update(f);
				}

				if (participanteInscritoFacade.update(pi) == null) {
					pi.setFrequencia(frequencia);
					participanteInscritoFacade.update(pi);
				}
			}
			frequenciasComAlteracao = new ArrayList<Frequencia>();
			modalConfirmacao.setVisible(false);
			Clients.showNotification("Alterações salvas com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null, null, 3000,
					true);
		} catch (Exception e) {
			Clients.showNotification("Ocorreu um erro inesperado ao salvar as alterações. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void aplicarFiltro() {
		try {
			if (frequencias.isEmpty()) {
				if (!filtroParticipante.filtroVazio()) {
					participantes = pessoaFacade.findPessoasByFiltro(filtroParticipante);
				} else {
					participantes = new ArrayList<Pessoa>();
				}
			} else {
				frequencias = frequenciaFacade.findFrequenciasByFiltroAndAtividadeAndDia(filtroParticipante,
						atividadeSelecionada, diaSelecionado.getData());
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
			if (frequencias.isEmpty()) {
				filtroParticipante.limparFiltro();
				participantes = new ArrayList<Pessoa>();
			} else {
				if (!filtroParticipante.getFiltro1().isEmpty() || !filtroParticipante.getFiltro2().isEmpty()) {
					filtroParticipante.limparFiltro();
					frequencias = frequenciaFacade.findFrequenciasUnicasByAtividadeAndDia(atividadeSelecionada,
							diaSelecionado.getData());
				}
			}
		} catch (Exception e) {
			Clients.showNotification("Erro ao realizar busca por filtro. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalDeAvaliacaoDeReacao(@BindingParam("atividade") Atividade atividade,
			@BindingParam("visible") Boolean visible) {
		try {
			if (visible) {
				avaliacoesReacao = avaliacaoReacaoFacade.findByAtividade(atividade);
				setAtividadeAvaliada(atividade);
				modalAvaliacaoDeReacao.setVisible(true);
			} else {
				modalAvaliacaoDeReacao.setVisible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalAvaliacaoDeReacaoIndividual(@BindingParam("avaliacaoReacao") AvaliacaoReacao avaliacaoReacao,
			@BindingParam("visible") Boolean visible) {
		try {
			if (visible) {
				this.avaliacaoReacao = avaliacaoReacao;
				this.legenda = gerarLegendaDaMedia(this.avaliacaoReacao.getMedia());
				modalAvaliacaoDeReacaoIndividual.setVisible(visible);
			} else {
				modalAvaliacaoDeReacaoIndividual.setVisible(visible);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalVisualizaRelatorioAvaliacaoDeReacao(@BindingParam("atividade") Atividade atividade,
			@BindingParam("visible") Boolean visible) {
		try {
			if (visible) {
				String path = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
				avaliacoesReacao = avaliacaoReacaoFacade.findByAtividade(atividade);
				atividadeSelecionada = atividade;
				atividadeSelecionada
						.setColaboradores(atividadeColaboradorFacade.findColaboradoresByAtividade(atividade));
				if (avaliacoesReacao.isEmpty()) {
					throw new Exception("Não foram encontrados avaliações dos participantes dessa atividade.");
				}
				List<AvaliacaoReacaoVO> listaAvaliacao = AvaliacaoReacaoVO.preencheLista(avaliacoesReacao);
				List<Double> medias = AvaliacaoReacaoVO.calculaMedias(listaAvaliacao);

				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("DATA_LISTA", atividadeSelecionada.getDataStr());
				parameters.put("EVENTO_NOME", atividadeSelecionada.getEvento().getNome());
				parameters.put("ATIVIDADE_NOME", atividadeSelecionada.getNome());
				parameters.put("COLABORADOR_NOME", atividadeSelecionada.getColaboradoresStr());

				DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				parameters.put("DATA_EMISSAO", df.format(new Date()));
				parameters.put("IMAGE_DIR", path + "/imagens");
				parameters.put("TOTAL_RESPOSTAS", listaAvaliacao.size());

				parameters.put("MEDIA_CONTEUDO_CURSO", medias.get(0));
				parameters.put("MEDIA_TEMPO_CONTEUDO", medias.get(1));
				parameters.put("MEDIA_METODOLOGIA", medias.get(2));
				parameters.put("MEDIA_DOMINIO_INSTRUTOR", medias.get(3));
				parameters.put("MEDIA_APOIO_INSTITUCIONAL", medias.get(5));
				parameters.put("MEDIA_GERAL", medias.get(6));

				if (medias.get(4) == 0.0) {
					parameters.put("MEDIA_MATERIAL_DIDATICO", "-");
					parameters.put("LEGENDA_MEDIA_MATERIAL_DIDATICO", "NÃO HOUVE");
				} else {
					parameters.put("MEDIA_MATERIAL_DIDATICO", "" + medias.get(4));
					parameters.put("LEGENDA_MEDIA_MATERIAL_DIDATICO", gerarLegendaDaMedia(medias.get(4)));
				}

				parameters.put("CHART", Chart.gerarChart(medias));

				parameters.put("LEGENDA_MEDIA_CONTEUDO_CURSO", gerarLegendaDaMedia(medias.get(0)));
				parameters.put("LEGENDA_MEDIA_TEMPO_CONTEUDO", gerarLegendaDaMedia(medias.get(1)));
				parameters.put("LEGENDA_MEDIA_METODOLOGIA", gerarLegendaDaMedia(medias.get(2)));
				parameters.put("LEGENDA_MEDIA_DOMINIO_INSTRUTOR", gerarLegendaDaMedia(medias.get(3)));
				parameters.put("LEGENDA_MEDIA_APOIO_INSTITUCIONAL", gerarLegendaDaMedia(medias.get(5)));
				parameters.put("LEGENDA_MEDIA", gerarLegendaDaMedia(medias.get(6)));

				String filename = "relatorio_avaliacao_reacao.jasper";
				lista = Report.getReportAvaliacaoReacao(path + "/jasper", listaAvaliacao, filename, parameters);
				iframeAvaliacaoReacao.setContent(new AMedia(lista, "application/pdf", null));
				modalVisualizaRelatorioAvaliacaoDeReacao.setVisible(visible);
			} else {
				lista = null;
				iframeAvaliacaoReacao.setContent(null);
				modalVisualizaRelatorioAvaliacaoDeReacao.setVisible(visible);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}

	}

	@Command
	@NotifyChange(".")
	public void abrirModalUploadArquivo(@BindingParam("visible") boolean visible,
			@BindingParam("atividade") Atividade atividade, @ContextParam(ContextType.BIND_CONTEXT) BindContext ctx)
			throws Exception {
		try {
			if (visible) {
				UploadEvent upEvent = null;
				Object objUploadEvent = ctx.getTriggerEvent();
				if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
					upEvent = (UploadEvent) objUploadEvent;
				}
				if (upEvent != null) {
					this.media = upEvent.getMedia();
					this.arquivo = new Arquivo();
					this.arquivo.setNome(this.media.getName());
					this.arquivo.setAtividade(atividade);				
				}
			}
			modalUploadArquivo.setVisible(visible);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void uploadArquivo(){
		try {
			if (this.media != null) {
				ArquivoUtil arqUtil = new ArquivoUtil();
				String nomePasta = "arquivos_atividades/arquivo_atividade_id_" + this.arquivo.getAtividade().getAtividadeId();
				
				//Formatando o nome do arquivo - retirar simbolos, caracteres com acentos
				this.arquivo.setNome(arqUtil.formatarNomeArquivo(this.media.getName()));
				
				modalUploadArquivo.setVisible(false);
				
				if (arqUtil.uploadArquivoFTP(this.media, this.arquivo.getNome(), nomePasta)) {
					arquivoFacade.include(this.arquivo);
					Clients.showNotification("Upload do arquivo " + this.arquivo.getNome() + " realizado com sucesso",
							Clients.NOTIFICATION_TYPE_INFO, null, null, 3000, true);
					
				} else {
					Clients.showNotification("Erro ao enviar o arquivo " + this.arquivo.getNome(), Clients.NOTIFICATION_TYPE_ERROR,
							null, null, 3000, true);
				}			
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalVisualizarArquivos(@BindingParam("visible") boolean visible,
			@BindingParam("atividade") Atividade atividade) {
		try {
			if (visible) {
				//String nomePasta = "arquivos_atividades/arquivo_atividade_id_" + atividade.getAtividadeId();
				//ArquivoUtil arq = new ArquivoUtil();
				
				this.atividadeSelecionada = atividade;
				this.arquivos = arquivoFacade.findByAtividade(atividade);
			}
			modalVisualizarArquivos.setVisible(visible);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void downloadArquivo(@BindingParam("arquivo") Arquivo arquivo) {
		try {
			String nomePasta = "arquivos_atividades/arquivo_atividade_id_" + arquivo.getAtividade().getAtividadeId();
			ArquivoUtil arq = new ArquivoUtil();
			InputStream in = arq.downloadArquivoFTP(arquivo.getNome(), nomePasta);
			if (in == null) {
				throw new Exception("Erro ao fazer o download do arquivo.");
			} else {
				String extensaoDoArquivo = FilenameUtils.getExtension(arquivo.getNome());
				Filedownload.save(in, "application/" + extensaoDoArquivo, arquivo.getNome());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalRemoverArquivo(@BindingParam("visible") boolean visible,
			@BindingParam("arquivo") Arquivo arquivo) {
		try {
			if (visible) {
				this.arquivo = arquivo;
			}
			modalRemoverArquivo.setVisible(visible);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void removerArquivo() {
		try {
			String nomePasta = "arquivos_atividades/arquivo_atividade_id_" + this.arquivo.getAtividade().getAtividadeId();
			ArquivoUtil arq = new ArquivoUtil();
			String nomeArquivo = this.arquivo.getNome();
			boolean status = arq.removerArquivoFTP(nomeArquivo, nomePasta);
			modalRemoverArquivo.setVisible(false);
			if (status) {
				arquivoFacade.delete(this.arquivo.getArquivoID());
				this.arquivos.remove(this.arquivo);
				Clients.showNotification("Arquivo " + nomeArquivo + " removido com sucesso",
						Clients.NOTIFICATION_TYPE_INFO, null, null, 3000, true);
			} else {
				Clients.showNotification("Erro ao apagar o arquivo " + nomeArquivo,
						Clients.NOTIFICATION_TYPE_ERROR, null, null, 3000, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
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

	private void consertaDados(Atividade atividade) {
		if (!testaQuantNumeros(atividade.getCep(), 8)) {
			atividade.setCep(null);
		}

		if (atividade.getComplemento() != null && atividade.getComplemento().trim().equals("")) {
			atividade.setComplemento(null);
		}

		if (atividade.getObservacoes() != null && atividade.getObservacoes().trim().equals("")) {
			atividade.setObservacoes(null);
		}
	}

	public String gerarLegendaDaMedia(Double media) {
		if (media >= 0 && media <= 1) {
			return "FRACO";
		} else if (media > 1 && media <= 2) {
			return "REGULAR";
		} else if (media > 2 && media <= 3) {
			return "BOM";
		} else {
			return "EXCELENTE";
		}
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

	public Atividade getAtividadeAvaliada() {
		return atividadeAvaliada;
	}

	public void setAtividadeAvaliada(Atividade atividadeAvaliada) {
		this.atividadeAvaliada = atividadeAvaliada;
	}

	public Evento getEventoSelecionado() {
		return eventoSelecionado;
	}

	public void setEventoSelecionado(Evento eventoSelecionado) {
		this.eventoSelecionado = eventoSelecionado;
	}

	public TipoAtividade getTipoAtividade() {
		return tipoAtividade;
	}

	public void setTipoAtividade(TipoAtividade tipoAtividade) {
		this.tipoAtividade = tipoAtividade;
	}

	public ParticipanteInscrito getParticipanteInscrito() {
		return participanteInscrito;
	}

	public void setParticipanteInscrito(ParticipanteInscrito participanteInscrito) {
		this.participanteInscrito = participanteInscrito;
	}

	public Filtro getFiltroParticipante() {
		return filtroParticipante;
	}

	public void setFiltroParticipante(Filtro filtroParticipante) {
		this.filtroParticipante = filtroParticipante;
	}

	public AvaliacaoReacao getAvaliacaoReacao() {
		return avaliacaoReacao;
	}

	public void setAvaliacaoReacao(AvaliacaoReacao avaliacaoReacao) {
		this.avaliacaoReacao = avaliacaoReacao;
	}

	public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}

	public DataUtil getDiaSelecionado() {
		return diaSelecionado;
	}

	public void setDiaSelecionado(DataUtil diaSelecionado) {
		this.diaSelecionado = diaSelecionado;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public boolean isGerenciarVagasVisible() {
		return gerenciarVagasVisible;
	}

	public void setGerenciarVagasVisible(boolean gerenciarVagasVisible) {
		this.gerenciarVagasVisible = gerenciarVagasVisible;
	}

	public boolean isAdicionarTipoVisible() {
		return adicionarTipoVisible;
	}

	public void setAdicionarTipoVisible(boolean adicionarTipoVisible) {
		this.adicionarTipoVisible = adicionarTipoVisible;
	}

	public boolean isAlterarEstadoVisible() {
		return alterarEstadoVisible;
	}

	public void setAlterarEstadoVisible(boolean alterarEstadoVisible) {
		this.alterarEstadoVisible = alterarEstadoVisible;
	}

	public boolean isAtividadeEAD() {
		return atividadeEAD;
	}

	public void setAtividadeEAD(boolean atividadeEAD) {
		this.atividadeEAD = atividadeEAD;
	}

	public boolean isDistribuirHorasAutomatico() {
		return distribuirHorasAutomatico;
	}

	public void setDistribuirHorasAutomatico(boolean distribuirHorasAutomatico) {
		this.distribuirHorasAutomatico = distribuirHorasAutomatico;
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

	public int getEspacosEmBranco() {
		return espacosEmBranco;
	}

	public void setEspacosEmBranco(int espacosEmBranco) {
		this.espacosEmBranco = espacosEmBranco;
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

	public Collection<Evento> getEventos() {
		return eventos;
	}

	public void setEventos(Collection<Evento> eventos) {
		this.eventos = eventos;
	}

	public Collection<Evento> getEventosRealizados() {
		return eventosRealizados;
	}

	public void setEventosRealizados(Collection<Evento> eventosRealizados) {
		this.eventosRealizados = eventosRealizados;
	}

	public Collection<Atividade> getAtividades() {
		return atividades;
	}

	public void setAtividades(Collection<Atividade> atividades) {
		this.atividades = atividades;
	}

	public Collection<TipoAtividade> getTipoDeAtividades() {
		return tipoDeAtividades;
	}

	public void setTipoDeAtividades(Collection<TipoAtividade> tipoDeAtividades) {
		this.tipoDeAtividades = tipoDeAtividades;
	}

	public Collection<Pessoa> getParticipantes() {
		return participantes;
	}

	public void setParticipantes(Collection<Pessoa> participantes) {
		this.participantes = participantes;
	}

	public Collection<AvaliacaoReacao> getAvaliacoesReacao() {
		return avaliacoesReacao;
	}

	public void setAvaliacoesReacao(Collection<AvaliacaoReacao> avaliacoesReacao) {
		this.avaliacoesReacao = avaliacoesReacao;
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

	public Collection<Frequencia> getFrequencias() {
		return frequencias;
	}

	public void setFrequencias(Collection<Frequencia> frequencias) {
		this.frequencias = frequencias;
	}

	public Collection<DataUtil> getDias() {
		return dias;
	}

	public void setDias(Collection<DataUtil> dias) {
		this.dias = dias;
	}

	public Collection<Arquivo> getArquivos() {
		return arquivos;
	}

	public void setArquivos(Collection<Arquivo> arquivos) {
		this.arquivos = arquivos;
	}

	public String getTextoRodape1() {
		return String.format(textoRodape1, frequencias.size(), presentes, ausentes);
	}

	public String getTextoRodape2() {
		return String.format(textoRodape2, participantes.size(), vagasTotal, vagasDisponiveis);
	}

	public String getLegenda() {
		return legenda;
	}

	public void setLegenda(String legenda) {
		this.legenda = legenda;
	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

}
