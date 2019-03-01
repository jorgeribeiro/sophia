package br.gov.ma.tce.sophia.ejb.beans.tipoatividade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity(name = TipoAtividade.NAME)
@Table(schema = "sigesco", name = "tipo_atividade")
public class TipoAtividade implements Serializable {
	public static final String NAME = "sigesco_Tipo_Atividade";
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "sigesco.seq_tipo_atividade", sequenceName = "sigesco.seq_tipo_atividade", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sigesco.seq_tipo_atividade")
	@Column(name = "tipo_atividade_id")
	private Integer tipoAtividadeId;

	@Column(name = "nome")
	private String nome;

	public Integer getTipoAtividadeId() {
		return tipoAtividadeId;
	}

	public void setTipoAtividadeId(Integer tipoAtividadeId) {
		this.tipoAtividadeId = tipoAtividadeId;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((tipoAtividadeId == null) ? 0 : tipoAtividadeId.hashCode());
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
		TipoAtividade other = (TipoAtividade) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (tipoAtividadeId == null) {
			if (other.tipoAtividadeId != null)
				return false;
		} else if (!tipoAtividadeId.equals(other.tipoAtividadeId))
			return false;
		return true;
	}

}
