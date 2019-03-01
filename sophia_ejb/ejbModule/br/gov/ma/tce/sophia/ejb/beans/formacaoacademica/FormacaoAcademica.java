package br.gov.ma.tce.sophia.ejb.beans.formacaoacademica;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity(name = FormacaoAcademica.NAME)
@Table(schema = "sigesco", name = "formacao_academica")
public class FormacaoAcademica implements Serializable {
	public static final String NAME = "sigesco_Formacao_Academica";
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "sigesco.seq_formacao_academica", sequenceName = "sigesco.seq_formacao_academica", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sigesco.seq_formacao_academica")
	@Column(name = "formacao_academica_id")
	private Integer formacaoAcademicaId;

	@Column(name = "nome")
	private String nome;

	@Column(name = "tipo_formacao_academica")
	private int tipoFormacaoAcademica;

	public Integer getFormacaoAcademicaId() {
		return formacaoAcademicaId;
	}

	public void setFormacaoAcademicaId(Integer formacaoAcademicaId) {
		this.formacaoAcademicaId = formacaoAcademicaId;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public int getTipoFormacaoAcademica() {
		return tipoFormacaoAcademica;
	}

	public void setTipoFormacaoAcademica(int tipoFormacaoAcademica) {
		this.tipoFormacaoAcademica = tipoFormacaoAcademica;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((formacaoAcademicaId == null) ? 0 : formacaoAcademicaId.hashCode());
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
		FormacaoAcademica other = (FormacaoAcademica) obj;
		if (formacaoAcademicaId == null) {
			if (other.formacaoAcademicaId != null)
				return false;
		} else if (!formacaoAcademicaId.equals(other.formacaoAcademicaId))
			return false;
		return true;
	}	
}
