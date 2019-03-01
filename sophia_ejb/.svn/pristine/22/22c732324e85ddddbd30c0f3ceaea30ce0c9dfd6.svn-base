package br.gov.ma.tce.sophia.ejb.beans.tipocolaborador;

import java.io.Serializable;
import java.util.Collection;

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

import br.gov.ma.tce.sophia.ejb.beans.atividadecolaborador.AtividadeColaborador;

@Entity(name = TipoColaborador.NAME)
@Table(schema = "sigesco", name = "tipo_colaborador")
public class TipoColaborador implements Serializable {
	public static final String NAME = "sigesco_Tipo_Colaborador";
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "sigesco.seq_tipo_colaborador", sequenceName = "sigesco.seq_tipo_colaborador", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sigesco.seq_tipo_colaborador")
	@Column(name = "tipo_colaborador_id")
	private Integer tipoColaboradorId;

	@Column(name = "nome")
	private String nome;

	@OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<AtividadeColaborador> atividadesColaboradores;

	public Integer getTipoColaboradorId() {
		return tipoColaboradorId;
	}

	public void setTipoColaboradorId(Integer tipoColaboradorId) {
		this.tipoColaboradorId = tipoColaboradorId;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Collection<AtividadeColaborador> getAtividadesColaboradores() {
		return atividadesColaboradores;
	}

	public void setAtividadesColaboradores(Collection<AtividadeColaborador> atividadesColaboradores) {
		this.atividadesColaboradores = atividadesColaboradores;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tipoColaboradorId == null) ? 0 : tipoColaboradorId.hashCode());
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
		TipoColaborador other = (TipoColaborador) obj;
		if (tipoColaboradorId == null) {
			if (other.tipoColaboradorId != null)
				return false;
		} else if (!tipoColaboradorId.equals(other.tipoColaboradorId))
			return false;
		return true;
	}

}