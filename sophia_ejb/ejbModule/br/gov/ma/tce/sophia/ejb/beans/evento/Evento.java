package br.gov.ma.tce.sophia.ejb.beans.evento;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import br.gov.ma.tce.sophia.ejb.beans.atividade.Atividade;

@Entity(name = Evento.NAME)
@Table(schema = "sigesco", name = "evento")
public class Evento implements Serializable {
	public static final String NAME = "sigesco_Evento";
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "sigesco.seq_evento", sequenceName = "sigesco.seq_evento", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sigesco.seq_evento")
	@Column(name = "evento_id")
	private Integer eventoId;

	@Column(name = "nome")
	private String nome;

	@Column(name = "descricao")
	private String descricao;

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

	@Column(name = "objetivo_geral")
	private String objetivoGeral;

	@Column(name = "detalhes")
	private String detalhes;

	@Column(name = "categoria")
	private String categoria;

	@Column(name = "motivo_cancelamento")
	private String motivoCancelamento;
	
	@Column(name = "categoria_anterior_cancelamento")
	private String categoriaAnteriorCancelamento;

	@OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<Atividade> atividades;

	@Transient
	private Estado estado;

	@Transient
	private Municipio municipio;

	@Transient
	public String getNomeStr() {
		try {
			return getNome() + (getCategoria().equals("CANCELADO") ? " (EVENTO CANCELADO)" : "");
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getEventoStr() {
		try {
			return this.nome + " - " + this.getDataStr();
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

	public Integer getEventoId() {
		return eventoId;
	}

	public void setEventoId(Integer eventoId) {
		this.eventoId = eventoId;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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

	public String getObjetivoGeral() {
		return objetivoGeral;
	}

	public void setObjetivoGeral(String objetivoGeral) {
		this.objetivoGeral = objetivoGeral;
	}

	public String getDetalhes() {
		return detalhes;
	}

	public void setDetalhes(String detalhes) {
		this.detalhes = detalhes;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getMotivoCancelamento() {
		return motivoCancelamento;
	}

	public void setMotivoCancelamento(String motivoCancelamento) {
		this.motivoCancelamento = motivoCancelamento;
	}

	public String getCategoriaAnteriorCancelamento() {
		return categoriaAnteriorCancelamento;
	}

	public void setCategoriaAnteriorCancelamento(String categoriaAnteriorCancelamento) {
		this.categoriaAnteriorCancelamento = categoriaAnteriorCancelamento;
	}

	public Collection<Atividade> getAtividades() {
		return atividades;
	}

	public void setAtividades(Collection<Atividade> atividades) {
		this.atividades = atividades;
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
		result = prime * result + ((eventoId == null) ? 0 : eventoId.hashCode());
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
		Evento other = (Evento) obj;
		if (eventoId == null) {
			if (other.eventoId != null)
				return false;
		} else if (!eventoId.equals(other.eventoId))
			return false;
		return true;
	}

}
