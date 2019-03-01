package br.gov.ma.tce.sophia.ejb.beans.impedimento;

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

import br.gov.ma.tce.sophia.ejb.beans.pessoa.Pessoa;
import br.gov.ma.tce.sophia.ejb.beans.usuario.Usuario;

@Entity(name = Impedimento.NAME)
@Table(schema = "sigesco", name = "impedimento")
public class Impedimento implements Serializable {
	public static final String NAME = "sigesco_Impedimento";
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "sigesco.seq_impedimento", sequenceName = "sigesco.seq_impedimento", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sigesco.seq_impedimento")
	@Column(name = "impedimento_id")
	private Integer impedimentoId;

	@Column(name = "motivo")
	private String motivo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_inicio_impedimento")
	private Date dataInicioImpedimento;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_fim_impedimento")
	private Date dataFimImpedimento;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_impedimento")
	private Date dataImpedimento;

	@ManyToOne
	@JoinColumn(name = "usuario_responsavel_id")
	private Usuario usuarioResponsavelImpedimento;

	@ManyToOne
	@JoinColumn(name = "participante_id")
	private Pessoa participanteImpedido;

	public Integer getImpedimentoId() {
		return impedimentoId;
	}

	public void setImpedimentoId(Integer impedimentoId) {
		this.impedimentoId = impedimentoId;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Date getDataInicioImpedimento() {
		return dataInicioImpedimento;
	}

	public void setDataInicioImpedimento(Date dataInicioImpedimento) {
		this.dataInicioImpedimento = dataInicioImpedimento;
	}

	public Date getDataFimImpedimento() {
		return dataFimImpedimento;
	}

	public void setDataFimImpedimento(Date dataFimImpedimento) {
		this.dataFimImpedimento = dataFimImpedimento;
	}

	public Date getDataImpedimento() {
		return dataImpedimento;
	}

	public void setDataImpedimento(Date dataImpedimento) {
		this.dataImpedimento = dataImpedimento;
	}

	public Usuario getUsuarioResponsavelImpedimento() {
		return usuarioResponsavelImpedimento;
	}

	public void setUsuarioResponsavelImpedimento(Usuario usuarioResponsavelImpedimento) {
		this.usuarioResponsavelImpedimento = usuarioResponsavelImpedimento;
	}

	public Pessoa getParticipanteImpedido() {
		return participanteImpedido;
	}

	public void setParticipanteImpedido(Pessoa participanteImpedido) {
		this.participanteImpedido = participanteImpedido;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((impedimentoId == null) ? 0 : impedimentoId.hashCode());
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
		Impedimento other = (Impedimento) obj;
		if (impedimentoId == null) {
			if (other.impedimentoId != null)
				return false;
		} else if (!impedimentoId.equals(other.impedimentoId))
			return false;
		return true;
	}

}
