package br.gov.ma.tce.sophia.client.pages;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Window;

import br.gov.ma.tce.gestores.server.beans.estado.Estado;
import br.gov.ma.tce.gestores.server.beans.estado.EstadoFacade;
import br.gov.ma.tce.gestores.server.beans.municipio.Municipio;
import br.gov.ma.tce.gestores.server.beans.municipio.MunicipioFacade;
import br.gov.ma.tce.sophia.client.utils.FTPUtil;
import br.gov.ma.tce.sophia.ejb.beans.atividadecolaborador.AtividadeColaborador;
import br.gov.ma.tce.sophia.ejb.beans.atividadecolaborador.AtividadeColaboradorFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.avaliacaoreacao.AvaliacaoReacao;
import br.gov.ma.tce.sophia.ejb.beans.avaliacaoreacao.AvaliacaoReacaoFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.colaborador.Colaborador;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscrito;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscritoFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.pessoa.Pessoa;
import br.gov.ma.tce.sophia.ejb.beans.pessoa.PessoaFacadeBean;
import br.gov.ma.tce.sophia.ejb.util.Filtro;
import br.gov.ma.tce.sophia.ejb.util.HashUtil;

public class MinhaContaVM {
	private Pessoa participante;
	private Colaborador colaborador;
	private ParticipanteInscrito participanteInscrito;
	private AvaliacaoReacao avaliacaoReacao;

	private boolean alterarEstadoVisible = false;
	private String atualSenha, novaSenha, repeteSenha;
	private Filtro filtroEvento;
	private EmailValidator emailValidator;

	private FTPUtil ftpUtil;

	private static final String textoRodape = "%d certificado(s) encontrado(s)";

	private Collection<Estado> estados;
	private Collection<Municipio> municipios;
	private Collection<ParticipanteInscrito> certificadosParticipacao;
	private Collection<AtividadeColaborador> certificadosColaboracao;

	private PessoaFacadeBean pessoaFacade;
	private ParticipanteInscritoFacadeBean participanteInscritoFacade;
	private AtividadeColaboradorFacadeBean atividadeColaboradorFacade;
	private AvaliacaoReacaoFacadeBean avaliacaoReacaoFacade;
	private EstadoFacade estadoFacade;
	private MunicipioFacade municipioFacade;

	@Wire("#modalWindow #comboEstadosModal")
	private Combobox comboEstadosModal;

	@Wire("#modalWindow #comboMunicipios")
	private Combobox comboMunicipios;

	@Wire("#modalWindow")
	private Window modalWindow;

	@Wire("#modalConfirmacao")
	private Window modalConfirmacao;

	@Wire("#modalIniciaAvaliacao")
	private Window modalIniciaAvaliacao;

	@Wire("#modalPreencherAvaliacao")
	private Window modalPreencherAvaliacao;

	@Wire("#modalConfirmaAvaliacao")
	private Window modalConfirmaAvaliacao;
	
	@Wire("#modalPreencherAvaliacao #radioConteudo")
	private Radiogroup radioConteudo;
	
	@Wire("#modalPreencherAvaliacao #radioTempoConteudo")
	private Radiogroup radioTempoConteudo;
	
	@Wire("#modalPreencherAvaliacao #radioCumpriuConteudo")
	private Radiogroup radioCumpriuConteudo;
	
	@Wire("#modalPreencherAvaliacao #radioMetodologia")
	private Radiogroup radioMetodologia;
	
	@Wire("#modalPreencherAvaliacao #radioDominioInstrutor")
	private Radiogroup radioDominioInstrutor;
	
	@Wire("#modalPreencherAvaliacao #radioMaterialDidatico")
	private Radiogroup radioMaterialDidatico;
	
	@Wire("#modalPreencherAvaliacao #radioApoioInstitucional")
	private Radiogroup radioApoioInstitucional;

	public MinhaContaVM() {
		try {
			InitialContext ctx = new InitialContext();
			pessoaFacade = (PessoaFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/PessoaFacadeBean!br.gov.ma.tce.sophia.ejb.beans.pessoa.PessoaFacadeBean");
			participanteInscritoFacade = (ParticipanteInscritoFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/ParticipanteInscritoFacadeBean!br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscritoFacadeBean");
			atividadeColaboradorFacade = (AtividadeColaboradorFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/AtividadeColaboradorFacadeBean!br.gov.ma.tce.sophia.ejb.beans.atividadecolaborador.AtividadeColaboradorFacadeBean");
			avaliacaoReacaoFacade = (AvaliacaoReacaoFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/AvaliacaoReacaoFacadeBean!br.gov.ma.tce.sophia.ejb.beans.avaliacaoreacao.AvaliacaoReacaoFacadeBean");
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
		
		participante = (Pessoa) Sessions.getCurrent().getAttribute("pessoa");
		participante = pessoaFacade.findByPrimaryKey(participante.getPessoaId());
		if (participante != null) {
			colaborador = participante.getColaborador();
		}
		certificadosParticipacao = new ArrayList<ParticipanteInscrito>();
		certificadosColaboracao = new ArrayList<AtividadeColaborador>();
		filtroEvento = new Filtro();
		emailValidator = new EmailValidator();
	}

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}

	@Command
	@NotifyChange(".")
	public void carregarCertificadosPorParticipante() {
		try {
			certificadosParticipacao = participanteInscritoFacade
					.findInscricoesByParticipanteAndCertificadoDisponivel(participante);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void carregarCertificadosPorColaborador() {
		try {
			certificadosColaboracao = atividadeColaboradorFacade
					.findAtividadesByColaboradorAndCertificadoDisponivel(colaborador);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@NotifyChange(".")
	public void carregarListaDeEstados() {
		estados = estadoFacade.findAll();
	}

	@Command
	@NotifyChange(".")
	public void carregarListaPorEstado() {
		try {
			municipios = municipioFacade.findMunicipiosByEstado(participante.getEstado().getEstadoId());
			participante.setMunicipio(null);
			if (comboMunicipios != null) {
				comboMunicipios.setSelectedItem(null);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalAtualizarParticipante(@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				participante.setRepeteEmail(participante.getEmail());
				this.alterarEstadoVisible = false;
				comboEstadosModal.setSelectedItem(null);
				comboMunicipios.setSelectedItem(null);
				modalWindow.setVisible(visible);
			} else {
				participante = pessoaFacade.findByPrimaryKey(participante.getPessoaId());
				modalWindow.setVisible(visible);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void exibirOpcoesEditarEstado() {
		carregarListaDeEstados();
		alterarEstadoVisible = true;
		participante.setEstado(null);
		participante.setMunicipio(null);
		comboEstadosModal.setSelectedItem(null);
		comboMunicipios.setSelectedItem(null);
		municipios = null;
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmaAtualizarParticipante(@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				consertaDados(participante);
				if ((participante.getNome() == null || participante.getNome().equals(""))
						|| (participante.getLogradouro() == null || participante.getLogradouro().equals(""))
						|| (participante.getNumero() == null || participante.getNumero().equals(""))
						|| participante.getEstado() == null || participante.getMunicipio() == null
						|| (participante.getBairro() == null || participante.getBairro().equals(""))
						|| participante.getCep() == null || participante.getDtNascimento() == null
						|| (participante.getEmail() == null || participante.getEmail().equals(""))
						|| (participante.getRepeteEmail() == null || participante.getRepeteEmail().equals(""))) {
					throw new Exception("Todos os campos precisam ser preechidos corretamente.");
				} else if (participante.getTelCelular() == null && participante.getTelFixo() == null
						&& participante.getTelComercial() == null) {
					throw new Exception("Preencha pelo menos um telefone para contato.");
				} else if (!emailValidator.isValid(participante.getEmail(), null)) {
					throw new Exception("Insira um endereço de e-mail válido.");
				} else if (!participante.getEmail().equals(participante.getRepeteEmail())) {
					throw new Exception("E-mails não correspondem.");
				} else if (pessoaFacade.findByPrimaryKeyAndEmail(participante.getPessoaId(),
						participante.getEmail()) != null) {
					throw new Exception("E-mail já cadastrado para outro participante.");
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
	public void atualizarParticipante() {
		try {
			if ((participante = pessoaFacade.update(participante)) != null) {
				modalConfirmacao.setVisible(false);
				modalWindow.setVisible(false);
				Clients.showNotification("Dados atualizados com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null, null,
						3000, true);
			}
		} catch (Exception e) {
			Clients.showNotification(
					"Ocorreu um erro inesperado ao fazer a atualização dos dados. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmaAlterarSenha(@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				if ((atualSenha == null || atualSenha.equals("")) || (novaSenha == null || novaSenha.equals(""))
						|| (repeteSenha == null || repeteSenha.equals(""))) {
					throw new Exception("Todos os campos precisam ser preechidos corretamente.");
				} else if (!HashUtil.rehashSenha(atualSenha, participante.getSenha())) {
					throw new Exception("Senha atual incorreta.");
				} else if (novaSenha.length() < 4) {
					throw new Exception("A senha deve ter no mínimo 4 caracteres.");
				} else if (!novaSenha.equals(repeteSenha)) {
					throw new Exception("Senhas não correspondem.");
				} else if (novaSenha.equals(atualSenha)) {
					throw new Exception("Nova senha e senha atual precisam ser diferentes.");
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
	public void alterarSenha() {
		try {
			participante.setSenha(HashUtil.hashSenha(novaSenha));
			if (pessoaFacade.update(participante) != null) {
				atualSenha = novaSenha = repeteSenha = null;
				modalConfirmacao.setVisible(false);
				Clients.showNotification("Senha alterada com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null, null,
						3000, true);
			}
		} catch (Exception e) {
			Clients.showNotification(
					"Ocorreu um erro inesperado ao fazer a alteração de senha. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void aplicarFiltro(@BindingParam("buscarParticipante") boolean buscarParticipante) {
		try {
			if (buscarParticipante) {
				certificadosParticipacao = participanteInscritoFacade
						.findInscricoesByParticipanteAndCertificadoDisponivelAndFiltro(participante, filtroEvento);
			} else {
				certificadosColaboracao = atividadeColaboradorFacade
						.findAtividadesByColaboradorAndCertificadoDisponivelAndFiltro(colaborador, filtroEvento);
			}
		} catch (Exception e) {
			Clients.showNotification("Erro ao realizar busca por filtro. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void limparFiltro(@BindingParam("buscarParticipante") boolean buscarParticipante) {
		try {
			if (!filtroEvento.getFiltro1().isEmpty() || !filtroEvento.getFiltro2().isEmpty()) {
				filtroEvento.limparFiltro();
				if (buscarParticipante) {
					certificadosParticipacao = participanteInscritoFacade
							.findInscricoesByParticipanteAndCertificadoDisponivel(participante);
				} else {
					certificadosColaboracao = atividadeColaboradorFacade
							.findAtividadesByColaboradorAndCertificadoDisponivel(colaborador);
				}
			}
		} catch (Exception e) {
			Clients.showNotification("Erro ao realizar busca por filtro. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void baixarCertificadoParticipacao(
			@BindingParam("participanteInscrito") ParticipanteInscrito participanteInscrito) {
		try {
			this.participanteInscrito = participanteInscrito;
			if (participanteInscrito.getAvaliacaoPreenchida()) {
				if (ftpUtil == null) {
					ftpUtil = new FTPUtil();
				}
				String nomeArquivo = "certificado_participante_"
						+ participanteInscrito.getAtividade().getNomeFormatado() + "_"
						+ participanteInscrito.getParticipante().getNomeFormatado() + ".pdf";
				String nomePasta = "certificados_participantes/atividade_id_"
						+ participanteInscrito.getAtividade().getAtividadeId();
				InputStream in = ftpUtil.downloadArquivo2(nomeArquivo, nomePasta);
				if (in == null) {
					throw new Exception("Certificado ainda não disponível.");
				} else {
					Filedownload.save(in, "application/pdf", nomeArquivo);
				}
			} else {				
				abrirModalIniciaAvaliacao(true);
			}
		} catch (Exception e) {
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 4000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void baixarCertificadoColaboracao(
			@BindingParam("atividadeColaborador") AtividadeColaborador atividadeColaborador) {
		try {
			if (!atividadeColaborador.getCertificadoDisponivel()) {
				throw new Exception("Certificado não disponível.");
			}
			if (ftpUtil == null) {
				ftpUtil = new FTPUtil();
			}
			String nomeArquivo = "certificado_colaborador_" + atividadeColaborador.getAtividade().getNomeFormatado()
					+ "_" + atividadeColaborador.getColaborador().getPessoa().getNomeFormatado() + ".pdf";
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
			Clients.showNotification(e.getMessage(), Clients.NOTIFICATION_TYPE_WARNING, null, null, 4000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalIniciaAvaliacao(@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				modalIniciaAvaliacao.setVisible(true);
			} else {
				modalIniciaAvaliacao.setVisible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalPreencherAvaliacao(@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				modalIniciaAvaliacao.setVisible(false);
				avaliacaoReacao = new AvaliacaoReacao();
				avaliacaoReacao.setDtAvaliacao(new Date());
				limparRadioGroups();
				modalPreencherAvaliacao.setVisible(true);
			
			} else {
				modalPreencherAvaliacao.setVisible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalConfirmaAvaliacao(@BindingParam("visible") boolean visible) {

		try {
			if (visible) {
				avaliacaoReacao.setParticipanteInscrito(participanteInscrito);
				if (avaliacaoReacao.getConteudoCurso() == null || avaliacaoReacao.getCumpriuConteudo() == null
						|| avaliacaoReacao.getDominioInstrutor() == null
						|| avaliacaoReacao.getApoioInstitucional() == null || avaliacaoReacao.getMetodologia() == null
						|| avaliacaoReacao.getMaterialDidatico() == null
						|| avaliacaoReacao.getTempoConteudo() == null) {

					Clients.showNotification("Marque todos os campos para poder continuar.",
							Clients.NOTIFICATION_TYPE_WARNING, null, null, 4000, true);
				} else {
					modalConfirmaAvaliacao.setVisible(true);
				}

			} else {
				modalConfirmaAvaliacao.setVisible(false);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void enviarAvaliacao() {

		try {
			avaliacaoReacaoFacade.include(avaliacaoReacao);
			participanteInscrito.setAvaliacaoPreenchida(true);
			participanteInscritoFacade.update(participanteInscrito);
			modalConfirmaAvaliacao.setVisible(false);
			modalPreencherAvaliacao.setVisible(false);
			Clients.showNotification("Avaliação preenchida com sucesso! Seu certificado já está disponível.",
					Clients.NOTIFICATION_TYPE_INFO, null, null, 4000, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@NotifyChange(".")	
	public void limparRadioGroups() {
		radioConteudo.setSelectedItem(null);
		radioTempoConteudo.setSelectedItem(null);
		radioCumpriuConteudo.setSelectedItem(null);
		radioMetodologia.setSelectedItem(null);
		radioDominioInstrutor.setSelectedItem(null);
		radioMaterialDidatico.setSelectedItem(null);
		radioApoioInstitucional.setSelectedItem(null);
	}

	@Command
	@NotifyChange(".")
	public String concatenar(String str1, String str2) {
		return str1 + str2;
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

	private void consertaDados(Pessoa participante) {
		if (!testaQuantNumeros(participante.getTelFixo(), 10)) {
			participante.setTelFixo(null);
		}

		if (!testaQuantNumeros(participante.getTelComercial(), 10)) {
			participante.setTelComercial(null);
		}

		if (!testaQuantNumeros(participante.getTelCelular(), 11)) {
			participante.setTelCelular(null);
		}

		if (!testaQuantNumeros(participante.getCep(), 8)) {
			participante.setCep(null);
		}

		if (participante.getComplemento() != null && participante.getComplemento().trim().equals("")) {
			participante.setComplemento(null);
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

	public ParticipanteInscrito getParticipanteInscrito() {
		return participanteInscrito;
	}

	public void setParticipanteInscrito(ParticipanteInscrito participanteInscrito) {
		this.participanteInscrito = participanteInscrito;
	}

	public AvaliacaoReacao getAvaliacaoReacao() {
		return avaliacaoReacao;
	}

	public void setvaliacaoReacao(AvaliacaoReacao avaliacaoReacao) {
		this.avaliacaoReacao = avaliacaoReacao;
	}

	public boolean isAlterarEstadoVisible() {
		return alterarEstadoVisible;
	}

	public void setAlterarEstadoVisible(boolean alterarEstadoVisible) {
		this.alterarEstadoVisible = alterarEstadoVisible;
	}

	public String getAtualSenha() {
		return atualSenha;
	}

	public void setAtualSenha(String atualSenha) {
		this.atualSenha = atualSenha;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public String getRepeteSenha() {
		return repeteSenha;
	}

	public void setRepeteSenha(String repeteSenha) {
		this.repeteSenha = repeteSenha;
	}

	public Filtro getFiltroEvento() {
		return filtroEvento;
	}

	public void setFiltroEvento(Filtro filtroEvento) {
		this.filtroEvento = filtroEvento;
	}

	public String getTextoRodape() {
		if (!certificadosParticipacao.isEmpty()) {
			return String.format(textoRodape, certificadosParticipacao.size());
		} else {
			return String.format(textoRodape, certificadosColaboracao.size());
		}
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

	public Collection<ParticipanteInscrito> getCertificadosParticipacao() {
		return certificadosParticipacao;
	}

	public void setCertificadosParticipacao(Collection<ParticipanteInscrito> certificados) {
		this.certificadosParticipacao = certificados;
	}

	public Collection<AtividadeColaborador> getCertificadosColaboracao() {
		return certificadosColaboracao;
	}

	public void setCertificadosColaboracao(Collection<AtividadeColaborador> certificadosColaboracao) {
		this.certificadosColaboracao = certificadosColaboracao;
	}

}
