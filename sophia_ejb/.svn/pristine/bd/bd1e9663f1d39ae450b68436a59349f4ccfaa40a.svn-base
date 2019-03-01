package br.gov.ma.tce.sophia.ejb.beans.avaliacaoreacao;

import java.beans.Transient;
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

import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscrito;

@Entity(name = AvaliacaoReacao.NAME)
@Table(schema = "sigesco", name = "avaliacao_reacao")
public class AvaliacaoReacao implements Serializable {
	public static final String NAME = "sigesco_Avaliacao_Reacao";
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "sigesco.seq_avaliacao_reacao", sequenceName = "sigesco.seq_avaliacao_reacao", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sigesco.seq_avaliacao_reacao")
	@Column(name = "avaliacao_reacao_id")
	private Integer avaliacaoReacaoId;
	
	@Column(name = "conteudo_curso")
	private Integer conteudoCurso;

	@Column(name = "tempo_conteudo")
	private Integer tempoConteudo;

	@Column(name = "cumpriu_conteudo")
	private Integer cumpriuConteudo;

	@Column(name = "metodologia")
	private Integer metodologia;
	
	@Column(name = "dominio_instrutor")
	private Integer dominioInstrutor;

	@Column(name = "material_didatico")
	private Integer materialDidatico;

	@Column(name = "apoio_institucional")
	private Integer apoioInstitucional;

	@Column(name = "comentarios")
	private String comentarios;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_avaliacao")
	private Date dtAvaliacao;
	
	@ManyToOne
	@JoinColumn(name = "participante_inscrito_id")
	private ParticipanteInscrito participanteInscrito;
	
	@Transient
	public Double getMedia() {
		Integer soma = (getConteudoCurso()+getTempoConteudo()+getMetodologia()
			+getDominioInstrutor()+getMaterialDidatico()+getApoioInstitucional());
		//Colocar com duas casas decimais
		return Math.round((soma/6.0) * 100) / 100d;
	}
	
	@Transient
	public String getCumpriuConteudoStr() {
		
		if (getCumpriuConteudo()==1) {
			return "Sim";
		}else if(getCumpriuConteudo()==-1) {
			return "Não";
		}else {
			return "Parcial";
		}
	
	}
	
	@Transient
	public String getMaterialDidaticoStr() {
		
		if (getMaterialDidatico()==0) {
			return "Não houve";
		}else {
			return ""+getMaterialDidatico();
		}
	
	}

	public Integer getAvaliacaoReacaoId() {
		return avaliacaoReacaoId;
	}

	public void setAvaliacaoReacaoId(Integer avaliacaoReacaoId) {
		this.avaliacaoReacaoId = avaliacaoReacaoId;
	}

	public ParticipanteInscrito getParticipanteInscrito() {
		return participanteInscrito;
	}

	public void setParticipanteInscrito(ParticipanteInscrito participanteInscrito) {
		this.participanteInscrito = participanteInscrito;
	}

	public Integer getConteudoCurso() {
		return conteudoCurso;
	}

	public void setConteudoCurso(Integer conteudoCurso) {
		this.conteudoCurso = conteudoCurso;
	}

	public Integer getTempoConteudo() {
		return tempoConteudo;
	}

	public void setTempoConteudo(Integer tempoConteudo) {
		this.tempoConteudo = tempoConteudo;
	}

	public Integer getCumpriuConteudo() {
		return cumpriuConteudo;
	}

	public void setCumpriuConteudo(Integer cumpriuConteudo) {
		this.cumpriuConteudo = cumpriuConteudo;
	}

	public Integer getMetodologia() {
		return metodologia;
	}

	public void setMetodologia(Integer metodologia) {
		this.metodologia = metodologia;
	}

	public Integer getDominioInstrutor() {
		return dominioInstrutor;
	}

	public void setDominioInstrutor(Integer dominioInstrutor) {
		this.dominioInstrutor = dominioInstrutor;
	}

	public Integer getMaterialDidatico() {
		return materialDidatico;
	}

	public void setMaterialDidatico(Integer materialDidatico) {
		this.materialDidatico = materialDidatico;
	}

	public Integer getApoioInstitucional() {
		return apoioInstitucional;
	}

	public void setApoioInstitucional(Integer apoioInstitucional) {
		this.apoioInstitucional = apoioInstitucional;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public Date getDtAvaliacao() {
		return dtAvaliacao;
	}

	public void setDtAvaliacao(Date dtAvaliacao) {
		this.dtAvaliacao = dtAvaliacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((avaliacaoReacaoId == null) ? 0 : avaliacaoReacaoId.hashCode());
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
		AvaliacaoReacao other = (AvaliacaoReacao) obj;
		if (avaliacaoReacaoId == null) {
			if (other.avaliacaoReacaoId != null)
				return false;
		} else if (!avaliacaoReacaoId.equals(other.avaliacaoReacaoId))
			return false;
		return true;
	}
	
}
