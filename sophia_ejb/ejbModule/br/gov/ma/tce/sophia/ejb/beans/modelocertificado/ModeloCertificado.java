package br.gov.ma.tce.sophia.ejb.beans.modelocertificado;

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
import br.gov.ma.tce.sophia.ejb.beans.fundocertificado.FundoCertificado;

@Entity(name = ModeloCertificado.NAME)
@Table(schema = "sigesco", name = "modelo_certificado")
public class ModeloCertificado implements Serializable {
	public static final String NAME = "sigesco_Modelo_Certificado";
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "sigesco.seq_modelo_certificado", sequenceName = "sigesco.seq_modelo_certificado", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sigesco.seq_modelo_certificado")
	@Column(name = "modelo_certificado_id")
	private Integer modeloCertificadoId;

	@Column(name = "tipo")
	private String tipo;

	@Column(name = "texto")
	private String texto;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_criacao")
	private Date dataCriacao;
	
	@ManyToOne
	@JoinColumn(name = "fundo_certificado_id")
	private FundoCertificado fundoCertificado;

	@ManyToOne
	@JoinColumn(name = "atividade_id")
	private Atividade atividade;
	
	public static ModeloCertificado copiaModeloCertificado(ModeloCertificado modeloCertificado) {
		ModeloCertificado mc = new ModeloCertificado();
		mc.setAtividade(modeloCertificado.getAtividade());
		mc.setDataCriacao(modeloCertificado.getDataCriacao());
		mc.setFundoCertificado(modeloCertificado.getFundoCertificado());
		mc.setTexto(modeloCertificado.getTexto());
		mc.setTipo(modeloCertificado.getTipo());
		return mc;
	}

	public Integer getModeloCertificadoId() {
		return modeloCertificadoId;
	}

	public void setModeloCertificadoId(Integer modeloCertificadoId) {
		this.modeloCertificadoId = modeloCertificadoId;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public FundoCertificado getFundoCertificado() {
		return fundoCertificado;
	}

	public void setFundoCertificado(FundoCertificado fundoCertificado) {
		this.fundoCertificado = fundoCertificado;
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
		result = prime * result + ((modeloCertificadoId == null) ? 0 : modeloCertificadoId.hashCode());
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
		ModeloCertificado other = (ModeloCertificado) obj;
		if (modeloCertificadoId == null) {
			if (other.modeloCertificadoId != null)
				return false;
		} else if (!modeloCertificadoId.equals(other.modeloCertificadoId))
			return false;
		return true;
	}

}
