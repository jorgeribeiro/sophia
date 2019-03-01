package br.gov.ma.tce.sophia.ejb.beans.tokencertificado;

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

import br.gov.ma.tce.sophia.ejb.beans.atividadecolaborador.AtividadeColaborador;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscrito;

@Entity(name = TokenCertificado.NAME)
@Table(schema = "sigesco", name = "token_certificado")
public class TokenCertificado implements Serializable {
	public static final String NAME = "sigesco_Token_Certificado";
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "sigesco.seq_token_certificado", sequenceName = "sigesco.seq_token_certificado", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sigesco.seq_token_certificado")
	@Column(name = "token_certificado_id")
	private Integer tokenCertificadoId;
	
	@Column(name = "token")
	private String token;
	
	@ManyToOne
	@JoinColumn(name = "participante_inscrito_id")
	private ParticipanteInscrito participanteInscrito;
	
	@ManyToOne
	@JoinColumn(name = "atividade_colaborador_id")
	private AtividadeColaborador atividadeColaborador;

	public Integer getTokenCertificadoId() {
		return tokenCertificadoId;
	}

	public void setTokenCertificadoId(Integer tokenCertificadoId) {
		this.tokenCertificadoId = tokenCertificadoId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public ParticipanteInscrito getParticipanteInscrito() {
		return participanteInscrito;
	}

	public void setParticipanteInscrito(ParticipanteInscrito participanteInscrito) {
		this.participanteInscrito = participanteInscrito;
	}

	public AtividadeColaborador getAtividadeColaborador() {
		return atividadeColaborador;
	}

	public void setAtividadeColaborador(AtividadeColaborador atividadeColaborador) {
		this.atividadeColaborador = atividadeColaborador;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tokenCertificadoId == null) ? 0 : tokenCertificadoId.hashCode());
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
		TokenCertificado other = (TokenCertificado) obj;
		if (tokenCertificadoId == null) {
			if (other.tokenCertificadoId != null)
				return false;
		} else if (!tokenCertificadoId.equals(other.tokenCertificadoId))
			return false;
		return true;
	}
}
