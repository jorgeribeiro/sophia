package br.gov.ma.tce.sophia.ejb.beans.diasatividade;

import java.io.Serializable;
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

import br.gov.ma.tce.sophia.ejb.beans.atividade.Atividade;

@Entity(name = DiasAtividade.NAME)
@Table(schema = "sigesco", name = "dias_atividade")
public class DiasAtividade implements Serializable {
	public static final String NAME = "sigesco_Dias_Atividade";
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "sigesco.seq_dias_atividade", sequenceName = "sigesco.seq_dias_atividade", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sigesco.seq_dias_atividade")
	@Column(name = "dias_atividade_id")
	private Integer diasAtividadeId;

	@Temporal(TemporalType.DATE)
	@Column(name = "dia")
	private Date dia;

	@Temporal(TemporalType.TIME)
	@Column(name = "quantidade_horas")
	private Date quantidadeHoras;

	@ManyToOne
	@JoinColumn(name = "atividade_id")
	private Atividade atividade;

	public Integer getDiasAtividadeId() {
		return diasAtividadeId;
	}

	public void setDiasAtividadeId(Integer diasAtividadeId) {
		this.diasAtividadeId = diasAtividadeId;
	}

	public Date getDia() {
		return dia;
	}

	public void setDia(Date dia) {
		this.dia = dia;
	}

	public Date getQuantidadeHoras() {
		return quantidadeHoras;
	}

	public void setQuantidadeHoras(Date quantidadeHoras) {
		this.quantidadeHoras = quantidadeHoras;
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
		result = prime * result + ((diasAtividadeId == null) ? 0 : diasAtividadeId.hashCode());
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
		DiasAtividade other = (DiasAtividade) obj;
		if (diasAtividadeId == null) {
			if (other.diasAtividadeId != null)
				return false;
		} else if (!diasAtividadeId.equals(other.diasAtividadeId))
			return false;
		return true;
	}

}
