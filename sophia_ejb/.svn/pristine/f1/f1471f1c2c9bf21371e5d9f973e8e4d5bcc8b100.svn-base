package br.gov.ma.tce.sophia.ejb.beans.participanteinscrito;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;

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

import br.gov.ma.tce.sophia.ejb.beans.atividade.Atividade;
import br.gov.ma.tce.sophia.ejb.beans.frequencia.Frequencia;
import br.gov.ma.tce.sophia.ejb.beans.participanteinteresse.ParticipanteInteresse;
import br.gov.ma.tce.sophia.ejb.beans.pessoa.Pessoa;

@Entity(name = ParticipanteInscrito.NAME)
@Table(schema = "sigesco", name = "participante_inscrito")
public class ParticipanteInscrito implements Serializable {
	public static final String NAME = "sigesco_Participante_Inscrito";
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "sigesco.seq_participante_inscrito", sequenceName = "sigesco.seq_participante_inscrito", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sigesco.seq_participante_inscrito")
	@Column(name = "participante_inscrito_id")
	private Integer participanteInscritoId;

	@Column(name = "inscricao_aprovada")
	private Boolean inscricaoAprovada;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_pre_inscricao")
	private Date dtPreInscricao;

	@Column(name = "frequencia")
	private Double frequencia;

	@Column(name = "certificado_disponivel")
	private Boolean certificadoDisponivel;
	
	@Column(name = "avaliacao_preenchida")
	private Boolean avaliacaoPreenchida;

	@ManyToOne
	@JoinColumn(name = "pessoa_id")
	private Pessoa participante;

	@ManyToOne
	@JoinColumn(name = "atividade_id")
	private Atividade atividade;

	@OneToMany(mappedBy = "participanteInscrito", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<Frequencia> frequencias;

	@Transient
	public String getInscricaoAprovadaStr() {
		if (getInscricaoAprovada() != null) {
			return getInscricaoAprovada() ? "Sim" : "Não";
		} else {
			return "Não";
		}
	}

	@Transient
	public String getFrequenciaStr() {
		String toReturn = "";
		try {
			DecimalFormat df = new DecimalFormat("#.##");
			toReturn = df.format(getFrequencia());
		} catch (Exception e) {
			return "-";
		}
		return toReturn + "%";
	}

	@Transient
	public String getDtPreInscricaoStr() {
		try {
			DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT);
			return df.format(this.getDtPreInscricao());
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getNomeParticipante() {
		return this.getParticipante().getNome();
	}

	@Transient
	public String getCertificadoDisponivelStr() {
		return getCertificadoDisponivel() ? "Sim" : "Não";
	}
	
	@Transient
	public String getAvaliacaoPreenchidaStr() {
		return getAvaliacaoPreenchida() ? "Sim" : "Não";
	}

	public static ParticipanteInscrito copiarParticipanteInteresse(ParticipanteInteresse participanteInteresse) {
		ParticipanteInscrito p = new ParticipanteInscrito();
		p.setInscricaoAprovada(true);
		p.setDtPreInscricao(participanteInteresse.getDtInteresse());
		p.setFrequencia(0.0);
		p.setCertificadoDisponivel(false);
		p.setParticipante(participanteInteresse.getParticipante());
		p.setAtividade(participanteInteresse.getAtividade());
		return p;
	}

	public Integer getParticipanteInscritoId() {
		return participanteInscritoId;
	}

	public void setParticipanteInscritoId(Integer participanteInscritoId) {
		this.participanteInscritoId = participanteInscritoId;
	}

	public Boolean getInscricaoAprovada() {
		return inscricaoAprovada;
	}

	public void setInscricaoAprovada(Boolean inscricaoConfirmada) {
		this.inscricaoAprovada = inscricaoConfirmada;
	}

	public Date getDtPreInscricao() {
		return dtPreInscricao;
	}

	public void setDtPreInscricao(Date dtPreInscricao) {
		this.dtPreInscricao = dtPreInscricao;
	}

	public Double getFrequencia() {
		return frequencia;
	}

	public void setFrequencia(Double frequencia) {
		this.frequencia = frequencia;
	}

	public Boolean getCertificadoDisponivel() {
		return certificadoDisponivel;
	}

	public void setCertificadoDisponivel(Boolean certificadoDisponivel) {
		this.certificadoDisponivel = certificadoDisponivel;
	}

	public Boolean getAvaliacaoPreenchida() {
		return avaliacaoPreenchida;
	}

	public void setAvaliacaoPreenchida(Boolean avaliacaoPreenchida) {
		this.avaliacaoPreenchida = avaliacaoPreenchida;
	}

	public Pessoa getParticipante() {
		return participante;
	}

	public void setParticipante(Pessoa participante) {
		this.participante = participante;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public Collection<Frequencia> getFrequencias() {
		return frequencias;
	}

	public void setFrequencias(Collection<Frequencia> frequencias) {
		this.frequencias = frequencias;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((participanteInscritoId == null) ? 0 : participanteInscritoId.hashCode());
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
		ParticipanteInscrito other = (ParticipanteInscrito) obj;
		if (participanteInscritoId == null) {
			if (other.participanteInscritoId != null)
				return false;
		} else if (!participanteInscritoId.equals(other.participanteInscritoId))
			return false;
		return true;
	}

}
