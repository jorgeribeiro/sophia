package br.gov.ma.tce.sophia.ejb.beans.emaillog;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity(name = EmailLog.NAME)
@Table(schema = "sigesco", name = "email_log")
public class EmailLog implements Serializable {	
	public static final String NAME = "sigesco_Email_Log";
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "sigesco.seq_email_log", sequenceName = "sigesco.seq_email_log", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sigesco.seq_email_log")
	@Column(name = "email_log_id")
	private Integer emailLogId;
	
	@Column(name = "log")
	private String log;
	
	@Column(name = "erro_relacionado")
	private String erroRelacionado;
	
	@Column(name = "email_destinatario")
	private String emailDestinatario;

	public Integer getEmailLogId() {
		return emailLogId;
	}

	public void setEmailLogId(Integer emailLogId) {
		this.emailLogId = emailLogId;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public String getErroRelacionado() {
		return erroRelacionado;
	}

	public void setErroRelacionado(String erroRelacionado) {
		this.erroRelacionado = erroRelacionado;
	}

	public String getEmailDestinatario() {
		return emailDestinatario;
	}

	public void setEmailDestinatario(String emailDestinatario) {
		this.emailDestinatario = emailDestinatario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((emailLogId == null) ? 0 : emailLogId.hashCode());
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
		EmailLog other = (EmailLog) obj;
		if (emailLogId == null) {
			if (other.emailLogId != null)
				return false;
		} else if (!emailLogId.equals(other.emailLogId))
			return false;
		return true;
	}
	
}
