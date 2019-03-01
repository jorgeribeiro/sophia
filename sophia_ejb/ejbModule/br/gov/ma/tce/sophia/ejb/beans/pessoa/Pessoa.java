package br.gov.ma.tce.sophia.ejb.beans.pessoa;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.Normalizer;
import java.util.Collection;
import java.util.Date;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.swing.text.MaskFormatter;

import br.gov.ma.tce.gestores.server.beans.estado.Estado;
import br.gov.ma.tce.gestores.server.beans.estado.EstadoFacade;
import br.gov.ma.tce.gestores.server.beans.municipio.Municipio;
import br.gov.ma.tce.gestores.server.beans.municipio.MunicipioFacade;
import br.gov.ma.tce.sophia.ejb.beans.colaborador.Colaborador;
import br.gov.ma.tce.sophia.ejb.beans.impedimento.Impedimento;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscrito;
import br.gov.ma.tce.sophia.ejb.beans.usuario.Usuario;

@Entity(name = Pessoa.NAME)
@Table(schema = "sigesco", name = "pessoa")
public class Pessoa implements Serializable {
	public static final String NAME = "sigesco_Pessoa";
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "sigesco.seq_pessoa", sequenceName = "sigesco.seq_pessoa", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sigesco.seq_pessoa")
	@Column(name = "pessoa_id")
	private Integer pessoaId;

	@Column(name = "nome")
	private String nome;

	@Column(name = "cpf")
	private String cpf;

	@Column(name = "email")
	private String email;

	@Column(name = "sexo")
	private String sexo;

	@Column(name = "logradouro")
	private String logradouro;

	@Column(name = "numero")
	private String numero;

	@Column(name = "bairro")
	private String bairro;

	@Column(name = "complemento")
	private String complemento;

	@Column(name = "cep")
	private String cep;

	@Temporal(TemporalType.DATE)
	@Column(name = "dt_nascimento")
	private Date dtNascimento;

	@Column(name = "tel_fixo")
	private String telFixo;

	@Column(name = "tel_celular")
	private String telCelular;

	@Column(name = "tel_comercial")
	private String telComercial;

	@Column(name = "senha")
	private String senha;

	@Column(name = "tipo_pessoa")
	private String tipoPessoa;

	@Column(name = "estado_id")
	private Integer estadoId;

	@Column(name = "municipio_id")
	private Integer municipioId;

	@Column(name = "servidor_matricula")
	private String servidorMatricula;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_cadastro")
	private Date dtCadastro;

	@OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Colaborador colaborador;

	@OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Usuario usuario;

	@OneToMany(mappedBy = "participante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<ParticipanteInscrito> inscricoes;

	@OneToMany(mappedBy = "participanteImpedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<Impedimento> impedimentos;

	@Transient
	private Estado estado;

	@Transient
	private Municipio municipio;

	@Transient
	private String repeteEmail;

	@Transient
	private String repeteSenha;

	@Transient
	public String getCpfStr() {
		String toReturn = "";
		try {
			// Coloca 0's à esquerda caso estejam faltando
			while (getCpf().length() < 11) {
				setCpf("0" + getCpf());
			}
			MaskFormatter cpf = new MaskFormatter("###.###.###-##");
			cpf.setValueContainsLiteralCharacters(false);
			toReturn = cpf.valueToString(getCpf());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toReturn;
	}

	@Transient
	public String getEnderecoStr() {
		try {
			return getLogradouro() + ", " + getNumero() + ", "
					+ (getComplemento() != null ? getComplemento() + ", " : "") + getBairro();
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getMunicipioEstadoStr() {
		try {
			return getMunicipio().getNome() + " - " + getEstado().getSigla();
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getDtNascimentoStr() {
		try {
			DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT);
			return df.format(getDtNascimento());
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getTipoParticipanteStr() {
		try {
			if (getTipoPessoa().equals("1")) {
				return "SOCIEDADE";
			} else if (getTipoPessoa().equals("2")) {
				return "SERVIDOR DO TCE-MA";
			} else if (getTipoPessoa().equals("3")) {
				return "JURIDISCIONADO";
			} else {
				return "SERVIDOR TERCEIRIZADO";
			}
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getNomeFormatado() {
		return Normalizer.normalize(getNome(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}

	@Transient
	public String getNomeSobrenome() {
		String[] nomeSobrenome = this.nome.split(" ");
		return nomeSobrenome[0] + " " + nomeSobrenome[nomeSobrenome.length - 1];
	}

	public Integer getPessoaId() {
		return pessoaId;
	}

	public void setPessoaId(Integer pessoaId) {
		this.pessoaId = pessoaId;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public Date getDtNascimento() {
		return dtNascimento;
	}

	public void setDtNascimento(Date dtNascimento) {
		this.dtNascimento = dtNascimento;
	}

	public String getTelFixo() {
		return telFixo;
	}

	public void setTelFixo(String telFixo) {
		this.telFixo = telFixo;
	}

	public String getTelCelular() {
		return telCelular;
	}

	public void setTelCelular(String telCelular) {
		this.telCelular = telCelular;
	}

	public String getTelComercial() {
		return telComercial;
	}

	public void setTelComercial(String telComercial) {
		this.telComercial = telComercial;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public Integer getEstadoId() {
		return estadoId;
	}

	public void setEstadoId(Integer estadoId) {
		this.estadoId = estadoId;
	}

	public Integer getMunicipioId() {
		return municipioId;
	}

	public void setMunicipioId(Integer municipioId) {
		this.municipioId = municipioId;
	}

	public String getServidorMatricula() {
		return servidorMatricula;
	}

	public void setServidorMatricula(String servidorMatricula) {
		this.servidorMatricula = servidorMatricula;
	}

	public Date getDtCadastro() {
		return dtCadastro;
	}

	public void setDtCadastro(Date dtCadastro) {
		this.dtCadastro = dtCadastro;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Collection<ParticipanteInscrito> getInscricoes() {
		return inscricoes;
	}

	public void setInscricoes(Collection<ParticipanteInscrito> inscricoes) {
		this.inscricoes = inscricoes;
	}

	public Collection<Impedimento> getImpedimentos() {
		return impedimentos;
	}

	public void setImpedimentos(Collection<Impedimento> impedimentos) {
		this.impedimentos = impedimentos;
	}

	public Estado getEstado() {
		if (this.getEstadoId() != null) {
			try {
				EstadoFacade estadoFacade;
				InitialContext ctx = new InitialContext();
				estadoFacade = (EstadoFacade) ctx.lookup(
						"java:global/sophia_ear/gestores_server/gertrudes_EstadoFacadeBean!br.gov.ma.tce.gestores.server.beans.estado.EstadoFacade");
				estado = estadoFacade.findByPrimaryKey(this.getEstadoId());
			} catch (NamingException e) {
				e.printStackTrace();
				estado = null;
			}
			return estado;
		} else {
			return null;
		}
	}

	public void setEstado(Estado estado) {
		try {
			if (estado == null) {
				this.estado = null;
				this.estadoId = null;
			} else {
				this.estado = estado;
				this.estadoId = estado.getEstadoId();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public Municipio getMunicipio() {
		if (this.getMunicipioId() != null) {
			try {
				MunicipioFacade municipioFacade;
				InitialContext ctx = new InitialContext();
				municipioFacade = (MunicipioFacade) ctx.lookup(
						"java:global/sophia_ear/gestores_server/gertrudes_MunicipioFacadeBean!br.gov.ma.tce.gestores.server.beans.municipio.MunicipioFacade");
				municipio = municipioFacade.findByPrimaryKey(this.getMunicipioId());
			} catch (NamingException e) {
				e.printStackTrace();
				municipio = null;
			}
			return municipio;
		} else {
			return null;
		}
	}

	public void setMunicipio(Municipio municipio) {
		try {
			if (municipio == null) {
				this.municipio = null;
				this.municipioId = null;
			} else {
				this.municipio = municipio;
				this.municipioId = municipio.getMunicipioId();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public String getRepeteEmail() {
		return repeteEmail;
	}

	public void setRepeteEmail(String repeteEmail) {
		this.repeteEmail = repeteEmail;
	}

	public String getRepeteSenha() {
		return repeteSenha;
	}

	public void setRepeteSenha(String repeteSenha) {
		this.repeteSenha = repeteSenha;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pessoaId == null) ? 0 : pessoaId.hashCode());
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
		Pessoa other = (Pessoa) obj;
		if (pessoaId == null) {
			if (other.pessoaId != null)
				return false;
		} else if (!pessoaId.equals(other.pessoaId))
			return false;
		return true;
	}

}
