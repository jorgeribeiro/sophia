package br.gov.ma.tce.sophia.ejb.old;

import java.io.Serializable;

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

import br.gov.ma.tce.sophia.ejb.beans.pessoa.Pessoa;

@Entity(name = Participante.NAME)
@Table(schema = "sigesco", name = "participante")
public class Participante implements Serializable {
	public static final String NAME = "sigesco_Participante";
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "sigesco.seq_participante", sequenceName = "sigesco.seq_participante", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sigesco.seq_participante")
	@Column(name = "participante_id")
	private Integer participanteId;

	@Column(name = "tipo_participante")
	private String tipoParticipante;

	@ManyToOne
	@JoinColumn(name = "pessoa_id")
	private Pessoa pessoa;

	@Transient
	public String getTipoParticipanteStr() {
		try {
			if (getTipoParticipante().equals("1")) {
				return "SOCIEDADE";
			} else if (getTipoParticipante().equals("2")) {
				return "SERVIDOR DO TCE-MA";
			} else {
				return "JURIDISCIONADO";
			}
		} catch (Exception e) {
			return "-";
		}
	}

	public Integer getParticipanteId() {
		return participanteId;
	}

	public void setParticipanteId(Integer participanteId) {
		this.participanteId = participanteId;
	}

	public String getTipoParticipante() {
		return tipoParticipante;
	}

	public void setTipoParticipante(String tipoParticipante) {
		this.tipoParticipante = tipoParticipante;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((participanteId == null) ? 0 : participanteId.hashCode());
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
		Participante other = (Participante) obj;
		if (participanteId == null) {
			if (other.participanteId != null)
				return false;
		} else if (!participanteId.equals(other.participanteId))
			return false;
		return true;
	}

}
