package br.gov.ma.tce.sophia.ejb.old;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity(name = Assinatura.NAME)
@Table(schema = "sigesco", name = "assinatura")
public class Assinatura implements Serializable {
	public static final String NAME = "sigesco_Assinatura";
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "sigesco.seq_assinatura", sequenceName = "sigesco.seq_assinatura", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sigesco.seq_assinatura")
	@Column(name = "assinatura_id")
	private Integer assinaturaId;

	@Column(name = "nome")
	private String nome;

	@Column(name = "cargo")
	private String cargo;

	public Integer getAssinaturaId() {
		return assinaturaId;
	}

	public void setAssinaturaId(Integer assinaturaId) {
		this.assinaturaId = assinaturaId;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assinaturaId == null) ? 0 : assinaturaId.hashCode());
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
		Assinatura other = (Assinatura) obj;
		if (assinaturaId == null) {
			if (other.assinaturaId != null)
				return false;
		} else if (!assinaturaId.equals(other.assinaturaId))
			return false;
		return true;
	}
}
