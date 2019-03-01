package br.gov.ma.tce.sophia.ejb.beans.colaborador;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ma.tce.sophia.ejb.beans.atividadecolaborador.AtividadeColaborador;
import br.gov.ma.tce.sophia.ejb.beans.formacaoacademica.FormacaoAcademica;
import br.gov.ma.tce.sophia.ejb.beans.pessoa.Pessoa;

@Entity(name = Colaborador.NAME)
@Table(schema = "sigesco", name = "colaborador")
public class Colaborador implements Serializable {
	public static final String NAME = "sigesco_Colaborador";
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "sigesco.seq_colaborador", sequenceName = "sigesco.seq_colaborador", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sigesco.seq_colaborador")
	@Column(name = "colaborador_id")
	private Integer colaboradorId;

	@Column(name = "escolaridade")
	private String escolaridade;

	@Column(name = "perfil")
	private String perfil;

	@Column(name = "observacao")
	private String observacao;

	@Column(name = "status_cadastro")
	private String statusCadastro;

	@ManyToOne
	@JoinColumn(name = "pessoa_id")
	private Pessoa pessoa;

	@ManyToOne
	@JoinColumn(name = "formacao_academica_id")
	private FormacaoAcademica formacaoAcademica;

	@OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<AtividadeColaborador> atividadesColaboradores;

	@Transient
	public String getPerfilStr() {
		try {
			if (getPerfil() != null) {
				return getPerfil();
			} else {
				return "SEM PERFIL CADASTRADO";
			}
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getObservacaoStr() {
		try {
			if (getObservacao() != null) {
				return getObservacao();
			} else {
				return "SEM OBSERVAÇÃO CADASTRADA";
			}
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getStatusCadastroStr() {
		try {
			if (getStatusCadastro().equals("1")) {
				return "Aguardando confirmação";
			} else if (getStatusCadastro().equals("2")) {
				return "Cadastro confirmado";
			} else if (getStatusCadastro().equals("3")) {
				return "Cadastro desativado";
			} else {
				return "Não cadastrado";
			}
		} catch (Exception e) {
			return "-";
		}
	}

	public Integer getColaboradorId() {
		return colaboradorId;
	}

	public void setColaboradorId(Integer colaboradorId) {
		this.colaboradorId = colaboradorId;
	}

	public String getEscolaridade() {
		return escolaridade;
	}

	public void setEscolaridade(String escolaridade) {
		this.escolaridade = escolaridade;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		if (perfil.equals("")) {
			perfil = null;
		}
		this.perfil = perfil;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		if (observacao.equals("")) {
			observacao = null;
		}
		this.observacao = observacao;
	}

	public String getStatusCadastro() {
		return statusCadastro;
	}

	public void setStatusCadastro(String statusCadastro) {
		this.statusCadastro = statusCadastro;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public FormacaoAcademica getFormacaoAcademica() {
		return formacaoAcademica;
	}

	public void setFormacaoAcademica(FormacaoAcademica formacaoAcademica) {
		this.formacaoAcademica = formacaoAcademica;
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
		result = prime * result + ((colaboradorId == null) ? 0 : colaboradorId.hashCode());
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
		Colaborador other = (Colaborador) obj;
		if (colaboradorId == null) {
			if (other.colaboradorId != null)
				return false;
		} else if (!colaboradorId.equals(other.colaboradorId))
			return false;
		return true;
	}

}
