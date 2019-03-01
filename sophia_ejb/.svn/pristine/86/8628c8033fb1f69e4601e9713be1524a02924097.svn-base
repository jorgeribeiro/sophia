package br.gov.ma.tce.sophia.ejb.old;

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

import br.gov.ma.tce.sophia.ejb.beans.modelocertificado.ModeloCertificado;

@Entity(name = CertificadoAssinatura.NAME)
@Table(schema = "sigesco", name = "certificado_assinatura")
public class CertificadoAssinatura implements Serializable {
	public static final String NAME = "sigesco_Certificado_Assinatura";
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "sigesco.seq_certificado_assinatura", sequenceName = "sigesco.seq_certificado_assinatura", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sigesco.seq_certificado_assinatura")
	@Column(name = "certificado_assinatura_id")
	private Integer certificadoAssinaturaId;

	@ManyToOne
	@JoinColumn(name = "modelo_certificado_id")
	private ModeloCertificado modeloCertificado;

	@ManyToOne
	@JoinColumn(name = "assinatura_id")
	private Assinatura assinatura;

	public Integer getCertificadoAssinaturaId() {
		return certificadoAssinaturaId;
	}

	public void setCertificadoAssinaturaId(Integer certificadoAssinaturaId) {
		this.certificadoAssinaturaId = certificadoAssinaturaId;
	}

	public ModeloCertificado getModeloCertificado() {
		return modeloCertificado;
	}

	public void setModeloCertificado(ModeloCertificado modeloCertificado) {
		this.modeloCertificado = modeloCertificado;
	}

	public Assinatura getAssinatura() {
		return assinatura;
	}

	public void setAssinatura(Assinatura assinatura) {
		this.assinatura = assinatura;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((certificadoAssinaturaId == null) ? 0 : certificadoAssinaturaId.hashCode());
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
		CertificadoAssinatura other = (CertificadoAssinatura) obj;
		if (certificadoAssinaturaId == null) {
			if (other.certificadoAssinaturaId != null)
				return false;
		} else if (!certificadoAssinaturaId.equals(other.certificadoAssinaturaId))
			return false;
		return true;
	}
}
