package br.gov.ma.tce.sophia.ejb.beans.atividade;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.gov.ma.tce.gestores.server.beans.estado.Estado;
import br.gov.ma.tce.gestores.server.beans.estado.EstadoFacade;
import br.gov.ma.tce.gestores.server.beans.municipio.Municipio;
import br.gov.ma.tce.gestores.server.beans.municipio.MunicipioFacade;
import br.gov.ma.tce.sophia.ejb.beans.atividadecolaborador.AtividadeColaborador;
import br.gov.ma.tce.sophia.ejb.beans.diasatividade.DiasAtividade;
import br.gov.ma.tce.sophia.ejb.beans.evento.Evento;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscrito;
import br.gov.ma.tce.sophia.ejb.beans.participanteinteresse.ParticipanteInteresse;
import br.gov.ma.tce.sophia.ejb.beans.tipoatividade.TipoAtividade;

@Entity(name = Atividade.NAME)
@Table(schema = "sigesco", name = "atividade")
public class Atividade implements Serializable {
	public static final String NAME = "sigesco_Atividade";
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "sigesco.seq_atividade", sequenceName = "sigesco.seq_atividade", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sigesco.seq_atividade")
	@Column(name = "atividade_id")
	private Integer atividadeId;

	@Column(name = "nome")
	private String nome;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_inicio")
	private Date dataInicio;

	@Temporal(TemporalType.TIME)
	@Column(name = "hora_inicio")
	private Date horaInicio;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_fim")
	private Date dataFim;

	@Temporal(TemporalType.TIME)
	@Column(name = "hora_fim")
	private Date horaFim;

	@Column(name = "descricao")
	private String descricao;

	@Column(name = "estilo")
	private String estilo;

	@Column(name = "vagas")
	private Integer vagas;

	@Column(name = "vagas_sociedade")
	private Integer vagasSociedade;

	@Column(name = "vagas_servidor")
	private Integer vagasServidor;

	@Column(name = "vagas_jurisdicionado")
	private Integer vagasJurisdicionado;

	@Column(name = "modalidade")
	private String modalidade;

	@Column(name = "tipo_participante")
	private String tipoParticipante;

	@Column(name = "conteudo_programatico")
	private String conteudoProgramatico;

	@Column(name = "modulos")
	private String modulos;

	@Column(name = "local")
	private String local;

	@Column(name = "cep")
	private String cep;

	@Column(name = "logradouro")
	private String logradouro;

	@Column(name = "numero")
	private String numero;

	@Column(name = "bairro")
	private String bairro;

	@Column(name = "complemento")
	private String complemento;

	@Column(name = "cidade_id")
	private Integer municipioId;

	@Column(name = "estado_id")
	private Integer estadoId;

	@Temporal(TemporalType.TIME)
	@Column(name = "carga_horaria")
	private Date cargaHoraria;

	// Atributos com valor default precisam ser boolean, e não Boolean
	@Column(name = "atividade_cancelada")
	private boolean atividadeCancelada;

	@Column(name = "motivo_cancelamento")
	private String motivoCancelamento;

	// Atributos com valor default precisam ser boolean, e não Boolean
	@Column(name = "inscricoes_fechadas")
	private boolean inscricoesFechadas;

	@Column(name = "gera_certificado")
	private Boolean geraCertificado;

	@Temporal(TemporalType.DATE)
	@Column(name = "pre_inscricao_data_inicio")
	private Date preInscricaoDataInicio;

	@Temporal(TemporalType.DATE)
	@Column(name = "pre_inscricao_data_fim")
	private Date preInscricaoDataFim;

	@Column(name = "observacoes")
	private String observacoes;

	@ManyToOne
	@JoinColumn(name = "evento_id")
	private Evento evento;

	@ManyToOne
	@JoinColumn(name = "tipo_atividade_id")
	private TipoAtividade tipoAtividade;

	@OneToMany(mappedBy = "atividade", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<AtividadeColaborador> colaboradores;

	@OneToMany(mappedBy = "atividade", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<ParticipanteInscrito> inscricoes;

	@OneToMany(mappedBy = "atividade", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<ParticipanteInteresse> interessados;

	@OneToMany(mappedBy = "atividade", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<DiasAtividade> diasAtividade;

	@Transient
	private Estado estado;

	@Transient
	private Municipio municipio;

	@Transient
	public String getNomeStr() {
		try {
			return getNome() + (isAtividadeCancelada() ? " (ATIVIDADE CANCELADA)" : "");
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getAtividadeStr() {
		try {
			return getNomeStr() + " - " + this.getDataStr();
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getDataInicioStr() {
		try {
			DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT);
			return df.format(this.getDataInicio());
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getDataFimStr() {
		try {
			DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT);
			return df.format(this.getDataFim());
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getPreInscricaoDataInicioStr() {
		try {
			DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT);
			return df.format(this.getPreInscricaoDataInicio());
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getPreInscricaoDataFimStr() {
		try {
			DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT);
			return df.format(this.getPreInscricaoDataFim());
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getHoraInicioStr() {
		try {
			DateFormat df = new SimpleDateFormat("HH:mm");
			return df.format(this.getHoraInicio());
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getHoraFimStr() {
		try {
			DateFormat df = new SimpleDateFormat("HH:mm");
			return df.format(this.getHoraFim());
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getDataStr() {
		try {
			if (getDataInicioStr().equals(getDataFimStr())) {
				return getDataInicioStr();
			} else {
				return getDataInicioStr() + " a " + getDataFimStr();
			}
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getPreInscricaoDataStr() {
		try {
			return getPreInscricaoDataInicioStr() + " a " + getPreInscricaoDataFimStr();
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getPeriodoParaCertificadoStr() {
		try {
			if (getDataInicio().equals(getDataFim())) {
				return "dia " + getDataInicioStr();
			} else {
				return "período de " + getDataInicioStr() + " a " + getDataFimStr();
			}
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getHorarioStr() {
		try {
			if (getModalidade().equals("PRESENCIAL")) {
				return getHoraInicioStr() + " às " + getHoraFimStr();
			} else {
				return "-";
			}
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getTipoParticipanteStr() {
		try {
			if (getTipoParticipante().equals("2")) {
				return "SERVIDOR DO TCE-MA";
			} else if (getTipoParticipante().equals("3")) {
				return "JURISDICIONADO";
			} else if (getTipoParticipante().equals("1")) {
				return "SOCIEDADE";
			} else if (getTipoParticipante().equals("4")) {
				return "SERVIDOR DO TCE-MA e JURISDICIONADO";
			} else if (getTipoParticipante().equals("5")) {
				return "SERVIDOR DO TCE-MA e SOCIEDADE";
			}else if (getTipoParticipante().equals("6")) {
				return "JURISDICIONADO e SOCIEDADE";
			} else {
				return "SOCIEDADE, SERVIDOR DO TCE-MA e JURISDICIONADO";
			}
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getEnderecoStr() {
		try {
			if (getModalidade().equals("PRESENCIAL")) {
				return getLocal() + ", " + getLogradouro() + ", " + getNumero() + ", " + getBairro() + ", "
						+ getMunicipio().getNome() + " - " + getEstado().getSigla();
			} else {
				return "INTERNET";
			}
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getConteudoProgramaticoStr() {
		try {
			if (getConteudoProgramatico() == null || getConteudoProgramatico().equals("")) {
				return "SEM CONTEÚDO PROGRAMÁTICO CADASTRADO";
			} else {
				return getConteudoProgramatico();
			}
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getModulosStr() {
		try {
			if (getModulos() == null || getModulos().equals("")) {
				return "SEM MÓDULOS CADASTRADOS";
			} else {
				return getModulos();
			}
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getColaboradoresStr() {
		try {
			String toReturn = "";
			for (AtividadeColaborador ac : colaboradores) {
				toReturn += ac.getColaborador().getPessoa().getNome() + " (" + ac.getTipoColaborador().getNome() + ")"
						+ ", ";
			}
			return toReturn.substring(0, toReturn.length() - 2);
		} catch (Exception e) {
			return "SEM COLABORADORES ATRIBUÍDOS";
		}
	}

	@Transient
	public Boolean getPreInscricoesEncerradas() {
		try {
			Calendar calendar = Calendar.getInstance();
			Calendar preInscricaoDataFim = Calendar.getInstance();
			preInscricaoDataFim.setTime(getPreInscricaoDataFim());

			// Soma necessária pra evitar erros de comparação com hora
			// Sendo Pré-inscrição data fim e dia atual 16/01/2018:
			// 16/01/2018 5:00:00 > 16/01/2018 0:00:00
			// 16/01/2018 5:00:00 < 17/01/2018 0:00:00
			preInscricaoDataFim.add(Calendar.DAY_OF_MONTH, 1);

			return calendar.after(preInscricaoDataFim);
		} catch (Exception e) {
			return null;
		}
	}

	@Transient
	public String getGeraCertificadoStr() {
		try {
			return getGeraCertificado() ? "SIM" : "NÃO";
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getNomeFormatado() {
		return Normalizer.normalize(getNome(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll("/", "");
	}

	@Transient
	public Integer getCargaHorariaHorasInt() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.cargaHoraria);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	@Transient
	public Integer getCargaHorariaMinutosInt() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.cargaHoraria);
		return calendar.get(Calendar.MINUTE);
	}

	@Transient
	public String getCargaHorariaStr() {
		try {
			String cargaHorariaStr = "";
			Integer cargaHorariaHoras = getCargaHorariaHorasInt();
			Integer cargaHorariaMinutos = getCargaHorariaMinutosInt();

			if (cargaHorariaHoras > 0) {
				cargaHorariaStr += cargaHorariaHoras + "h";
			}

			if (cargaHorariaMinutos > 0) {
				cargaHorariaStr += cargaHorariaMinutos + "min";
			}
			return cargaHorariaStr;
		} catch (Exception e) {
			return "-";
		}
	}

	public Integer getAtividadeId() {
		return atividadeId;
	}

	public void setAtividadeId(Integer atividadeId) {
		this.atividadeId = atividadeId;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(Date horaInicio) {
		this.horaInicio = horaInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Date getHoraFim() {
		return horaFim;
	}

	public void setHoraFim(Date horaFim) {
		this.horaFim = horaFim;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getEstilo() {
		return estilo;
	}

	public void setEstilo(String estilo) {
		this.estilo = estilo;
	}

	public Integer getVagas() {
		return vagas;
	}

	public void setVagas(Integer vagas) {
		this.vagas = vagas;
	}

	public Integer getVagasSociedade() {
		return vagasSociedade;
	}

	public void setVagasSociedade(Integer vagasSociedade) {
		this.vagasSociedade = vagasSociedade;
	}

	public Integer getVagasServidor() {
		return vagasServidor;
	}

	public void setVagasServidor(Integer vagasServidor) {
		this.vagasServidor = vagasServidor;
	}

	public Integer getVagasJurisdicionado() {
		return vagasJurisdicionado;
	}

	public void setVagasJurisdicionado(Integer vagasJurisdicionado) {
		this.vagasJurisdicionado = vagasJurisdicionado;
	}

	public String getModalidade() {
		return modalidade;
	}

	public void setModalidade(String modalidade) {
		this.modalidade = modalidade;
	}

	public String getTipoParticipante() {
		return tipoParticipante;
	}

	public void setTipoParticipante(String tipoParticipante) {
		this.tipoParticipante = tipoParticipante;
	}

	public String getConteudoProgramatico() {
		return conteudoProgramatico;
	}

	public void setConteudoProgramatico(String conteudoProgramatico) {
		this.conteudoProgramatico = conteudoProgramatico;
	}

	public String getModulos() {
		return modulos;
	}

	public void setModulos(String modulos) {
		this.modulos = modulos;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public Integer getMunicipioId() {
		return municipioId;
	}

	public void setMunicipioId(Integer municipioId) {
		this.municipioId = municipioId;
	}

	public Integer getEstadoId() {
		return estadoId;
	}

	public void setEstadoId(Integer estadoId) {
		this.estadoId = estadoId;
	}

	public Date getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(Date cargaHorariaTime) {
		this.cargaHoraria = cargaHorariaTime;
	}

	public boolean isAtividadeCancelada() {
		return atividadeCancelada;
	}

	public void setAtividadeCancelada(boolean atividadeCancelada) {
		this.atividadeCancelada = atividadeCancelada;
	}

	public String getMotivoCancelamento() {
		return motivoCancelamento;
	}

	public void setMotivoCancelamento(String motivoCancelamento) {
		this.motivoCancelamento = motivoCancelamento;
	}

	public boolean getInscricoesFechadas() {
		return inscricoesFechadas;
	}

	public void setInscricoesFechadas(boolean inscricoesEncerradas) {
		this.inscricoesFechadas = inscricoesEncerradas;
	}

	public Boolean getGeraCertificado() {
		return geraCertificado;
	}

	public void setGeraCertificado(Boolean geraCertificado) {
		this.geraCertificado = geraCertificado;
	}

	public Date getPreInscricaoDataInicio() {
		return preInscricaoDataInicio;
	}

	public void setPreInscricaoDataInicio(Date preInscricaoDataInicio) {
		this.preInscricaoDataInicio = preInscricaoDataInicio;
	}

	public Date getPreInscricaoDataFim() {
		return preInscricaoDataFim;
	}

	public void setPreInscricaoDataFim(Date preInscricaoDataFim) {
		this.preInscricaoDataFim = preInscricaoDataFim;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public TipoAtividade getTipoAtividade() {
		return tipoAtividade;
	}

	public void setTipoAtividade(TipoAtividade tipoAtividade) {
		this.tipoAtividade = tipoAtividade;
	}

	public Collection<AtividadeColaborador> getColaboradores() {
		return colaboradores;
	}

	public void setColaboradores(Collection<AtividadeColaborador> colaboradores) {
		this.colaboradores = colaboradores;
	}

	public Collection<ParticipanteInscrito> getInscricoes() {
		return inscricoes;
	}

	public void setInscricoes(Collection<ParticipanteInscrito> inscricoes) {
		this.inscricoes = inscricoes;
	}

	public Collection<ParticipanteInteresse> getInteressados() {
		return interessados;
	}

	public void setInteressados(Collection<ParticipanteInteresse> interessados) {
		this.interessados = interessados;
	}

	public Collection<DiasAtividade> getDiasAtividade() {
		return diasAtividade;
	}

	public void setDiasAtividade(Collection<DiasAtividade> diasAtividade) {
		this.diasAtividade = diasAtividade;
	}

	public Estado getEstado() {
		if (this.getEstadoId() != null) {
			try {
				EstadoFacade estadoFacade;
				InitialContext ctx = new InitialContext();
				estadoFacade = (EstadoFacade) ctx.lookup(
						"java:global/sophia_ear/gestores_server/gertrudes_EstadoFacadeBean!br.gov.ma.tce.gestores.server.beans.estado.EstadoFacade");
				estado = estadoFacade.findByPrimaryKey(this.getEstadoId());
			} catch (NamingException e) {
				e.printStackTrace();
				estado = null;
			}
			return estado;
		} else {
			return null;
		}
	}

	public void setEstado(Estado estado) {
		try {
			if (estado == null) {
				this.estado = null;
				this.estadoId = null;
			} else {
				this.estado = estado;
				this.estadoId = estado.getEstadoId();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public Municipio getMunicipio() {
		if (this.getMunicipioId() != null) {
			try {
				MunicipioFacade municipioFacade;
				InitialContext ctx = new InitialContext();
				municipioFacade = (MunicipioFacade) ctx.lookup(
						"java:global/sophia_ear/gestores_server/gertrudes_MunicipioFacadeBean!br.gov.ma.tce.gestores.server.beans.municipio.MunicipioFacade");
				municipio = municipioFacade.findByPrimaryKey(this.getMunicipioId());
			} catch (NamingException e) {
				e.printStackTrace();
				municipio = null;
			}
			return municipio;
		} else {
			return null;
		}
	}

	public void setMunicipio(Municipio municipio) {
		try {
			if (municipio == null) {
				this.municipio = null;
				this.municipioId = null;
			} else {
				this.municipio = municipio;
				this.municipioId = municipio.getMunicipioId();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((atividadeId == null) ? 0 : atividadeId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Atividade other = (Atividade) obj;
		if (atividadeId == null) {
			if (other.atividadeId != null)
				return false;
		} else if (!atividadeId.equals(other.atividadeId))
			return false;
		return true;
	}

}
