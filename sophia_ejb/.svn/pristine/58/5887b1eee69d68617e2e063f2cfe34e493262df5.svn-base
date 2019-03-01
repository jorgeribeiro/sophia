package br.gov.ma.tce.sophia.ejb.beans.atividadecolaborador;

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

import br.gov.ma.tce.sophia.ejb.beans.atividade.Atividade;
import br.gov.ma.tce.sophia.ejb.beans.colaborador.Colaborador;
import br.gov.ma.tce.sophia.ejb.beans.tipocolaborador.TipoColaborador;

@Entity(name = AtividadeColaborador.NAME)
@Table(schema = "sigesco", name = "atividade_colaborador")
public class AtividadeColaborador implements Serializable {
	public static final String NAME = "sigesco_Atividade_Colaborador";
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "sigesco.seq_atividade_colaborador", sequenceName = "sigesco.seq_atividade_colaborador", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sigesco.seq_atividade_colaborador")
	@Column(name = "atividade_colaborador_id")
	private Integer atividadeColaboradorId;

	@ManyToOne
	@JoinColumn(name = "atividade_id")
	private Atividade atividade;

	@ManyToOne
	@JoinColumn(name = "colaborador_id")
	private Colaborador colaborador;

	@ManyToOne
	@JoinColumn(name = "tipo_colaborador_id")
	private TipoColaborador tipoColaborador;

	@Column(name = "certificado_disponivel")
	private boolean certificadoDisponivel;
	
	@Transient
	public String getCertificadoDisponivelStr() {
		return getCertificadoDisponivel() ? "Sim" : "Não";
	}

	public Integer getAtividadeColaboradorId() {
		return atividadeColaboradorId;
	}

	public void setAtividadeColaboradorId(Integer atividadeColaboradorId) {
		this.atividadeColaboradorId = atividadeColaboradorId;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	public TipoColaborador getTipoColaborador() {
		return tipoColaborador;
	}

	public void setTipoColaborador(TipoColaborador tipoColaborador) {
		this.tipoColaborador = tipoColaborador;
	}

	public boolean getCertificadoDisponivel() {
		return certificadoDisponivel;
	}

	public void setCertificadoDisponivel(boolean certificadoDisponivel) {
		this.certificadoDisponivel = certificadoDisponivel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((atividadeColaboradorId == null) ? 0 : atividadeColaboradorId.hashCode());
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
		AtividadeColaborador other = (AtividadeColaborador) obj;
		if (atividadeColaboradorId == null) {
			if (other.atividadeColaboradorId != null)
				return false;
		} else if (!atividadeColaboradorId.equals(other.atividadeColaboradorId))
			return false;
		return true;
	}

}
