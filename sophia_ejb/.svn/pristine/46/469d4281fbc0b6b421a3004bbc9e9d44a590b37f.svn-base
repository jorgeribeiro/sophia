package br.gov.ma.tce.sophia.ejb.beans.fundocertificado;

import java.awt.Image;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name = FundoCertificado.NAME)
@Table(schema = "sigesco", name = "fundo_certificado")
public class FundoCertificado implements Serializable {
	public static final String NAME = "sigesco_Fundo_Certificado";
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "sigesco.seq_fundo_certificado", sequenceName = "sigesco.seq_fundo_certificado", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sigesco.seq_fundo_certificado")
	@Column(name = "fundo_certificado_id")
	private Integer fundoCertificadoId;

	@Column(name = "nome")
	private String nome;
	
	@Column(name = "nome_ftp")
	private String nomeFtp;
	
	@Transient
	private Image imagem;

	public Integer getFundoCertificadoId() {
		return fundoCertificadoId;
	}

	public void setFundoCertificadoId(Integer fundoCertificadoId) {
		this.fundoCertificadoId = fundoCertificadoId;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeFtp() {
		return nomeFtp;
	}

	public void setNomeFtp(String nomeFtp) {
		this.nomeFtp = nomeFtp;
	}

	public Image getImagem() {
		return imagem;
	}

	public void setImagem(Image imagem) {
		this.imagem = imagem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fundoCertificadoId == null) ? 0 : fundoCertificadoId.hashCode());
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
		FundoCertificado other = (FundoCertificado) obj;
		if (fundoCertificadoId == null) {
			if (other.fundoCertificadoId != null)
				return false;
		} else if (!fundoCertificadoId.equals(other.fundoCertificadoId))
			return false;
		return true;
	}
}
