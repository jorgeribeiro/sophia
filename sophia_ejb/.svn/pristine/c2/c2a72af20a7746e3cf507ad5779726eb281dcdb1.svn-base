package br.gov.ma.tce.sophia.ejb.beans.capacitacaoservidor;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
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
import javax.persistence.Transient;

import br.gov.ma.tce.sophia.ejb.beans.pessoa.Pessoa;

@Entity(name = CapacitacaoServidor.NAME)
@Table(schema = "sigesco", name = "capacitacao_servidor")
public class CapacitacaoServidor implements Serializable {
	public static final String NAME = "sigesco_Capacitacao_Servidor";
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "sigesco.seq_capacitacao_servidor", sequenceName = "sigesco.seq_capacitacao_servidor", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sigesco.seq_capacitacao_servidor")
	@Column(name = "capacitacao_servidor_id")
	private Integer capacitacaoServidorId;

	@Column(name = "nome_capacitacao")
	private String nomeCapacitacao;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_inicio")
	private Date dataInicio;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_fim")
	private Date dataFim;

	@Column(name = "instituicao_provedora")
	private String instituicaoProvedora;

	@Temporal(TemporalType.TIME)
	@Column(name = "carga_horaria")
	private Date cargaHoraria;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_cadastro")
	private Date dtCadastro;

	@ManyToOne
	@JoinColumn(name = "pessoa_id")
	private Pessoa participante;
	
	@Transient
	public String getNomeParticipante() {
		return this.getParticipante().getNome();
	}

	@Transient
	public String getDataInicioStr() {
		try {
			DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT);
			return df.format(this.getDataInicio());
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getDataFimStr() {
		try {
			DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT);
			return df.format(this.getDataFim());
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public String getDataStr() {
		try {
			if (getDataInicioStr().equals(getDataFimStr())) {
				return getDataInicioStr();
			} else {
				return getDataInicioStr() + " a " + getDataFimStr();
			}
		} catch (Exception e) {
			return "-";
		}
	}

	@Transient
	public Integer getCargaHorariaHorasInt() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.cargaHoraria);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	@Transient
	public Integer getCargaHorariaMinutosInt() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.cargaHoraria);
		return calendar.get(Calendar.MINUTE);
	}

	@Transient
	public String getCargaHorariaStr() {
		try {
			String cargaHorariaStr = "";
			Integer cargaHorariaHoras = getCargaHorariaHorasInt();
			Integer cargaHorariaMinutos = getCargaHorariaMinutosInt();

			if (cargaHorariaHoras > 0) {
				cargaHorariaStr += cargaHorariaHoras + "h";
			}

			if (cargaHorariaMinutos > 0) {
				cargaHorariaStr += cargaHorariaMinutos + "min";
			}
			return cargaHorariaStr;
		} catch (Exception e) {
			return "-";
		}
	}

	public Integer getCapacitacaoServidorId() {
		return capacitacaoServidorId;
	}

	public void setCapacitacaoServidorId(Integer capacitacaoServidorId) {
		this.capacitacaoServidorId = capacitacaoServidorId;
	}

	public String getNomeCapacitacao() {
		return nomeCapacitacao;
	}

	public void setNomeCapacitacao(String nomeCapacitacao) {
		this.nomeCapacitacao = nomeCapacitacao;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getInstituicaoProvedora() {
		return instituicaoProvedora;
	}

	public void setInstituicaoProvedora(String instituicaoProvedora) {
		this.instituicaoProvedora = instituicaoProvedora;
	}

	public Date getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(Date cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public Date getDtCadastro() {
		return dtCadastro;
	}

	public void setDtCadastro(Date dtCadastro) {
		this.dtCadastro = dtCadastro;
	}

	public Pessoa getParticipante() {
		return participante;
	}

	public void setParticipante(Pessoa participante) {
		this.participante = participante;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((capacitacaoServidorId == null) ? 0 : capacitacaoServidorId.hashCode());
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
		CapacitacaoServidor other = (CapacitacaoServidor) obj;
		if (capacitacaoServidorId == null) {
			if (other.capacitacaoServidorId != null)
				return false;
		} else if (!capacitacaoServidorId.equals(other.capacitacaoServidorId))
			return false;
		return true;
	}

}
