package br.gov.ma.tce.sophia.ejb.beans.participanteinteresse;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.gov.ma.tce.sophia.ejb.beans.atividade.Atividade;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscrito;
import br.gov.ma.tce.sophia.ejb.beans.pessoa.Pessoa;

@Entity(name = ParticipanteInteresse.NAME)
@Table(schema = "sigesco", name = "participante_interesse")
public class ParticipanteInteresse implements Serializable {
	public static final String NAME = "sigesco_Participante_Interesse";
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "sigesco.seq_participante_interesse", sequenceName = "sigesco.seq_participante_interesse", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sigesco.seq_participante_interesse")
	@Column(name = "participante_interesse_id")
	private Integer participanteInteresseId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_interesse")
	private Date dtInteresse;
	
	@Column(name = "inscrito_na_atividade")
	private Boolean inscritoNaAtividade;
	
	@ManyToOne
	@JoinColumn(name = "pessoa_id")
	private Pessoa participante;

	@ManyToOne
	@JoinColumn(name = "atividade_id")
	private Atividade atividade;
	
	@Transient
	public String getDtInteresseStr() {
		try {
			DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT);
			return df.format(this.getDtInteresse());
		} catch (Exception e) {
			return "-";
		}
	}
	
	@Transient
	public String getNomeParticipante() {
		return this.getParticipante().getNome();
	}
	
	public static ParticipanteInteresse copiarParticipanteInscrito(ParticipanteInscrito participanteInscrito) {
		ParticipanteInteresse p = new ParticipanteInteresse();
		p.setInscritoNaAtividade(false);
		p.setDtInteresse(participanteInscrito.getDtPreInscricao());
		p.setParticipante(participanteInscrito.getParticipante());
		p.setAtividade(participanteInscrito.getAtividade());
		return p;
	}

	public Integer getParticipanteInteresseId() {
		return participanteInteresseId;
	}

	public void setParticipanteInteresseId(Integer participanteInteresseId) {
		this.participanteInteresseId = participanteInteresseId;
	}

	public Date getDtInteresse() {
		return dtInteresse;
	}

	public void setDtInteresse(Date dtInteresse) {
		this.dtInteresse = dtInteresse;
	}

	public Boolean getInscritoNaAtividade() {
		return inscritoNaAtividade;
	}

	public void setInscritoNaAtividade(Boolean inscritoNaAtividade) {
		this.inscritoNaAtividade = inscritoNaAtividade;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((participanteInteresseId == null) ? 0 : participanteInteresseId.hashCode());
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
		ParticipanteInteresse other = (ParticipanteInteresse) obj;
		if (participanteInteresseId == null) {
			if (other.participanteInteresseId != null)
				return false;
		} else if (!participanteInteresseId.equals(other.participanteInteresseId))
			return false;
		return true;
	}
	
}
