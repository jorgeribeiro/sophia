package br.gov.ma.tce.sophia.ejb.beans.arquivo;

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

import br.gov.ma.tce.sophia.ejb.beans.atividade.Atividade;

@Entity(name = Arquivo.NAME)
@Table(schema = "sigesco", name = "arquivo")
public class Arquivo implements Serializable {
	public static final String NAME = "sigesco_Arquivo";
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "sigesco.seq_arquivo", sequenceName = "sigesco.seq_arquivo", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sigesco.seq_arquivo")
	@Column(name = "arquivo_id")
	private Integer arquivoID;
	
	@Column(name = "nome")
	private String nome;
	
	@Column(name = "descricao")
	private String descricao;

	@ManyToOne
	@JoinColumn(name = "atividade_id")
	private Atividade atividade;
	
	public Integer getArquivoID() {
		return arquivoID;
	}

	public void setArquivoID(Integer arquivoID) {
		this.arquivoID = arquivoID;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}	
}
