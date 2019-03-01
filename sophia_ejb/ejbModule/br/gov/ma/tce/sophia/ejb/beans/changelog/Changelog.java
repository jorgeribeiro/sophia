package br.gov.ma.tce.sophia.ejb.beans.changelog;

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

import br.gov.ma.tce.sophia.ejb.beans.usuario.Usuario;

@Entity(name = Changelog.NAME)
@Table(schema = "sigesco", name = "changelog")
public class Changelog implements Serializable {
	public static final String NAME = "sigesco_Changelog";
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "sigesco.seq_changelog", sequenceName = "sigesco.seq_changelog", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sigesco.seq_changelog")
	@Column(name = "changelog_id")
	private Integer changelogId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_alteracao")
	private Date dtAlteracao;

	@Column(name = "tabela_alterada")
	private String tabelaAlterada;

	@Column(name = "tipo_alteracao")
	private String tipoAlteracao;

	@Column(name = "id_alterado")
	private Integer idAlterado;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	public Integer getChangelogId() {
		return changelogId;
	}

	public void setChangelogId(Integer changelogId) {
		this.changelogId = changelogId;
	}

	public Date getDtAlteracao() {
		return dtAlteracao;
	}

	public void setDtAlteracao(Date dtAlteracao) {
		this.dtAlteracao = dtAlteracao;
	}

	public String getTabelaAlterada() {
		return tabelaAlterada;
	}

	public void setTabelaAlterada(String tabelaAlterada) {
		this.tabelaAlterada = tabelaAlterada;
	}

	public String getTipoAlteracao() {
		return tipoAlteracao;
	}

	public void setTipoAlteracao(String tipoAlteracao) {
		this.tipoAlteracao = tipoAlteracao;
	}

	public Integer getIdAlterado() {
		return idAlterado;
	}

	public void setIdAlterado(Integer idAlterado) {
		this.idAlterado = idAlterado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((changelogId == null) ? 0 : changelogId.hashCode());
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
		Changelog other = (Changelog) obj;
		if (changelogId == null) {
			if (other.changelogId != null)
				return false;
		} else if (!changelogId.equals(other.changelogId))
			return false;
		return true;
	}
}
