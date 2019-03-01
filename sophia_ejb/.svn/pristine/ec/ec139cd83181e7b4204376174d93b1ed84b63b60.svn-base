package br.gov.ma.tce.sophia.ejb.beans.frequencia;

import java.io.Serializable;
import java.text.DateFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ma.tce.sophia.ejb.beans.diasatividade.DiasAtividade;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscrito;

@Entity(name = Frequencia.NAME)
@Table(schema = "sigesco", name = "frequencia")
public class Frequencia implements Serializable {
	public static final String NAME = "sigesco_Frequencia";
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "sigesco.seq_frequencia", sequenceName = "sigesco.seq_frequencia", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sigesco.seq_frequencia")
	@Column(name = "frequencia_id")
	private Integer frequenciaId;

	@Column(name = "presenca")
	private Boolean presenca;

	@ManyToOne
	@JoinColumn(name = "participante_inscrito_id")
	private ParticipanteInscrito participanteInscrito;

	@ManyToOne
	@JoinColumn(name = "dias_atividade_id")
	private DiasAtividade diasAtividade;

	@Transient
	public String getDataFrequenciaStr() {
		try {
			DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT);
			return df.format(this.getDiasAtividade().getDia());
		} catch (Exception e) {
			return "-";
		}
	}

	public Integer getFrequenciaId() {
		return frequenciaId;
	}

	public void setFrequenciaId(Integer frequenciaId) {
		this.frequenciaId = frequenciaId;
	}

	public Boolean getPresenca() {
		return presenca;
	}

	public void setPresenca(Boolean presenca) {
		this.presenca = presenca;
	}

	public ParticipanteInscrito getParticipanteInscrito() {
		return participanteInscrito;
	}

	public void setParticipanteInscrito(ParticipanteInscrito participanteInscrito) {
		this.participanteInscrito = participanteInscrito;
	}

	public DiasAtividade getDiasAtividade() {
		return diasAtividade;
	}

	public void setDiasAtividade(DiasAtividade diasAtividade) {
		this.diasAtividade = diasAtividade;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((frequenciaId == null) ? 0 : frequenciaId.hashCode());
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
		Frequencia other = (Frequencia) obj;
		if (frequenciaId == null) {
			if (other.frequenciaId != null)
				return false;
		} else if (!frequenciaId.equals(other.frequenciaId))
			return false;
		return true;
	}

}
