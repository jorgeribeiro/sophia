package br.gov.ma.tce.sophia.client.pages;

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
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import br.gov.ma.tce.sophia.client.utils.FTPUtil;
import br.gov.ma.tce.sophia.ejb.beans.atividadecolaborador.AtividadeColaborador;
import br.gov.ma.tce.sophia.ejb.beans.atividadecolaborador.AtividadeColaboradorFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.colaborador.Colaborador;
import br.gov.ma.tce.sophia.ejb.beans.colaborador.ColaboradorFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.formacaoacademica.FormacaoAcademica;
import br.gov.ma.tce.sophia.ejb.beans.formacaoacademica.FormacaoAcademicaFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.pessoa.Pessoa;
import br.gov.ma.tce.sophia.ejb.beans.pessoa.PessoaFacadeBean;
import br.gov.ma.tce.sophia.ejb.util.EscolaridadeUtil;
import br.gov.ma.tce.sophia.ejb.util.Filtro;

public class ColaboradoresVM {
	private Pessoa participante;
	private Colaborador colaborador;
	private Filtro filtroEvento;

	private FTPUtil ftpUtil;

	private boolean colaboradorCadastrado = false;
	private boolean uploadCurriculo = false;
	private Media media;
	private static final String textoRodape = "%d item(ns) encontrado(s)";

	private Collection<AtividadeColaborador> colaboracoes;
	private Collection<FormacaoAcademica> formacoesAcademicas;

	private PessoaFacadeBean pessoaFacade;
	private ColaboradorFacadeBean colaboradorFacade;
	private AtividadeColaboradorFacadeBean atividadeColaboradorFacade;
	private FormacaoAcademicaFacadeBean formacaoAcademicaFacade;

	EscolaridadeUtil escolaridadeUtil;

	@Wire("#modalConfirmacao")
	private Window modalConfirmacao;

	public ColaboradoresVM() {
		try {
			InitialContext ctx = new InitialContext();
			pessoaFacade = (PessoaFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/PessoaFacadeBean!br.gov.ma.tce.sophia.ejb.beans.pessoa.PessoaFacadeBean");
			colaboradorFacade = (ColaboradorFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/ColaboradorFacadeBean!br.gov.ma.tce.sophia.ejb.beans.colaborador.ColaboradorFacadeBean");
			atividadeColaboradorFacade = (AtividadeColaboradorFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/AtividadeColaboradorFacadeBean!br.gov.ma.tce.sophia.ejb.beans.atividadecolaborador.AtividadeColaboradorFacadeBean");
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
		escolaridadeUtil = new EscolaridadeUtil();
		participante = (Pessoa) Sessions.getCurrent().getAttribute("pessoa");
		participante = pessoaFacade.findByPrimaryKey(participante.getPessoaId());
		if (participante != null) {
			colaborador = participante.getColaborador();
			if (colaborador == null) {
				colaboradorCadastrado = false;
				colaborador = new Colaborador();
				colaborador.setStatusCadastro("0");
			} else {
				colaboradorCadastrado = true;
			}
		}
		colaboracoes = new ArrayList<AtividadeColaborador>();
		formacoesAcademicas = formacaoAcademicaFacade.findAll();
		filtroEvento = new Filtro();
	}

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
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
	public void abrirModalConfirmaCadastro(@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				if (colaborador.getEscolaridade() == null) {
					throw new Exception("Todos os campos obrigatórios precisam ser preenchidos corretamente.");
				} else if ((!colaborador.getEscolaridade().equals("ENSINO FUNDAMENTAL")
						&& !colaborador.getEscolaridade().equals("ENSINO MÉDIO"))
						&& colaborador.getFormacaoAcademica() == null) {
					throw new Exception("Todos os campos obrigatórios precisam ser preenchidos corretamente.");
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
	public void cadastroColaborador() {
		try {
			if (colaborador.getEscolaridade().equals("ENSINO FUNDAMENTAL")
					|| colaborador.getEscolaridade().equals("ENSINO MÉDIO")) {
				colaborador.setFormacaoAcademica(null);
			}
			colaborador.setStatusCadastro("1");
			colaborador.setPessoa(participante);
			if ((colaborador = colaboradorFacade.include(colaborador)) != null) {
				try {
					if (ftpUtil == null) {
						ftpUtil = new FTPUtil();
					}
					if (media != null) {
						ftpUtil.upload2(media, "curriculo_colaborador_id_" + colaborador.getColaboradorId() + ".pdf",
								"curriculos_colaboradores/curriculo_colaborador_id_" + colaborador.getColaboradorId());
					}
				} catch (Exception e) {
					Clients.showNotification(
							"Ocorreu um erro inesperado ao fazer o upload do arquivo de currículo. Tente novamente mais tarde.",
							Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
				}
				media = null;
				colaboradorCadastrado = true;
				modalConfirmacao.setVisible(false);
				Clients.showNotification("Pré-cadastro de colaborador realizado com sucesso!",
						Clients.NOTIFICATION_TYPE_INFO, null, null, 3000, true);
			}
		} catch (Exception e) {
			Clients.showNotification(
					"Ocorreu um erro inesperado ao fazer o cadastro de colaborador. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarAtividadesPorColaborador() {
		if (colaboradorCadastrado) {
			colaboracoes = atividadeColaboradorFacade.findAtividadesByColaborador(colaborador);
		}
	}

	@Command
	@NotifyChange(".")
	public void baixarCertificado(@BindingParam("atividadeColaborador") AtividadeColaborador atividadeColaborador) {
		try {
			if (!atividadeColaborador.getCertificadoDisponivel()) {
				throw new Exception("Certificado não disponível.");
			}
			if (ftpUtil == null) {
				ftpUtil = new FTPUtil();
			}
			String nomeArquivo = "certificado_colaborador_" + atividadeColaborador.getAtividade().getNome() + "_"
					+ atividadeColaborador.getColaborador().getPessoa().getNome() + ".pdf";
			String nomePasta = "certificados_colaboradores/atividade_id_"
					+ atividadeColaborador.getAtividade().getAtividadeId();
			InputStream in = ftpUtil.downloadArquivo2(nomeArquivo, nomePasta);
			if (in == null) {
				throw new Exception(
						"Ocorreu um erro ao realizar o download do arquivo de certificado. Tente novamente mais tarde.");
			} else {
				Filedownload.save(in, "application/pdf", nomeArquivo);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void aplicarFiltro() {
		try {
			if (colaboradorCadastrado) {
				colaboracoes = atividadeColaboradorFacade.findAtividadesByColaboradorAndFiltro(colaborador,
						filtroEvento);
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
			if (!filtroEvento.getFiltro1().isEmpty() || !filtroEvento.getFiltro2().isEmpty()) {
				filtroEvento.limparFiltro();
				if (colaboradorCadastrado) {
					colaboracoes = atividadeColaboradorFacade.findAtividadesByColaborador(colaborador);
				}
			}
		} catch (Exception e) {
			Clients.showNotification("Erro ao realizar busca por filtro. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	public Pessoa getParticipante() {
		return participante;
	}

	public void setParticipante(Pessoa participante) {
		this.participante = participante;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	public Filtro getFiltroEvento() {
		return filtroEvento;
	}

	public void setFiltroEvento(Filtro filtroEvento) {
		this.filtroEvento = filtroEvento;
	}

	public boolean isColaboradorCadastrado() {
		return colaboradorCadastrado;
	}

	public void setColaboradorCadastrado(boolean colaboradorCadastrado) {
		this.colaboradorCadastrado = colaboradorCadastrado;
	}

	public boolean isUploadCurriculo() {
		return uploadCurriculo;
	}

	public void setUploadCurriculo(boolean uploadCurriculo) {
		this.uploadCurriculo = uploadCurriculo;
	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	public String getTextoRodape() {
		return String.format(textoRodape, colaboracoes.size());
	}

	public Collection<AtividadeColaborador> getColaboracoes() {
		return colaboracoes;
	}

	public void setColaboracoes(Collection<AtividadeColaborador> colaboracoes) {
		this.colaboracoes = colaboracoes;
	}

	public Collection<FormacaoAcademica> getFormacoesAcademicas() {
		return formacoesAcademicas;
	}

	public void setFormacoesAcademicas(Collection<FormacaoAcademica> formacoesAcademicas) {
		this.formacoesAcademicas = formacoesAcademicas;
	}

}
