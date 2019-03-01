package br.gov.ma.tce.sophia.ejb.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.gov.ma.tce.sophia.ejb.beans.avaliacaoreacao.AvaliacaoReacao;

public class AvaliacaoReacaoVO {
	private Integer conteudoCurso;
	private Integer tempoConteudo;
	private Integer metodologia;
	private Integer dominioInstrutor;
	private Integer materialDidatico;
	private Integer apoioInstitucional;

	public AvaliacaoReacaoVO(Integer conteudoCurso, Integer tempoConteudo, Integer metodologia,
			Integer dominioInstrutor, Integer materialDidatico, Integer apoioInstitucional) {
		this.conteudoCurso = conteudoCurso;
		this.tempoConteudo = tempoConteudo;
		this.metodologia = metodologia;
		this.dominioInstrutor = dominioInstrutor;
		this.materialDidatico = materialDidatico;
		this.apoioInstitucional = apoioInstitucional;
	}

	public static List<AvaliacaoReacaoVO> preencheLista(Collection<AvaliacaoReacao> avaliacoesReacao) {
		List<AvaliacaoReacaoVO> listaAvaliacao = new ArrayList<AvaliacaoReacaoVO>();
		for (AvaliacaoReacao av : avaliacoesReacao) {
			AvaliacaoReacaoVO object = new AvaliacaoReacaoVO(av.getConteudoCurso(), av.getTempoConteudo(),
					av.getMetodologia(), av.getDominioInstrutor(), av.getMaterialDidatico(),
					av.getApoioInstitucional());

			listaAvaliacao.add(object);
		}

		return listaAvaliacao;
	}

	public static List<Double> calculaMedias(Collection<AvaliacaoReacaoVO> avaliacoesReacao) {
		List<Double> medias = new ArrayList<Double>();
		int i;
		Double size = (double) avaliacoesReacao.size();
		if (size > 0) {
			Integer somaConteudoCurso, somaTempoConteudo, somaMetodologia, somaDominioInstrutor, somaMaterialDidatico,
					somaApoioInstitucional;
			Double houveMaterial = size;
			somaConteudoCurso = somaTempoConteudo = somaMetodologia = somaDominioInstrutor =
					somaMaterialDidatico = somaApoioInstitucional = 0;
			
			for (AvaliacaoReacaoVO av : avaliacoesReacao) {
				somaConteudoCurso += av.getConteudoCurso();
				somaTempoConteudo += av.getTempoConteudo();
				somaMetodologia += av.getMetodologia();
				somaDominioInstrutor += av.getDominioInstrutor();
				somaMaterialDidatico += av.getMaterialDidatico();
				if(av.getMaterialDidatico()==0) {
					houveMaterial -= 1.0;
				}
				somaApoioInstitucional += av.getApoioInstitucional();
			}

			medias.add(Math.round((somaConteudoCurso / size) * 100) / 100d);
			medias.add(Math.round((somaTempoConteudo / size) * 100) / 100d);
			medias.add(Math.round((somaMetodologia / size) * 100) / 100d);
			medias.add(Math.round((somaDominioInstrutor / size) * 100) / 100d);
			if(houveMaterial!=0) {
				medias.add(Math.round((somaMaterialDidatico / houveMaterial) * 100) / 100d);
			}else {
				//media é zerada, se media é zerada coloca-se '-' no relatorio
				medias.add(houveMaterial);
			}
			medias.add(Math.round((somaApoioInstitucional / size) * 100) / 100d);
			
			// Média Geral
			Double somMediaGeral = 0.0;
			for (Double m : medias) {
				somMediaGeral += m;
			}
			if(houveMaterial!=0) {
				medias.add(Math.round((somMediaGeral / 6.0) * 100) / 100d);
			}else {
				medias.add(Math.round((somMediaGeral / 5.0) * 100) / 100d);
			}
		} else {
			for (i = 0; i < 7; i++) {
				medias.add(0.0);
			}
		}

		return medias;
	}

	public Integer getConteudoCurso() {
		return conteudoCurso;
	}

	public Integer getTempoConteudo() {
		return tempoConteudo;
	}

	public Integer getMetodologia() {
		return metodologia;
	}

	public Integer getDominioInstrutor() {
		return dominioInstrutor;
	}

	public Integer getMaterialDidatico() {
		return materialDidatico;
	}

	public Integer getApoioInstitucional() {
		return apoioInstitucional;
	}

	public void setConteudoCurso(Integer conteudoCurso) {
		this.conteudoCurso = conteudoCurso;
	}

	public void setTempoConteudo(Integer tempoConteudo) {
		this.tempoConteudo = tempoConteudo;
	}

	public void setMetodologia(Integer metodologia) {
		this.metodologia = metodologia;
	}

	public void setDominioInstrutor(Integer dominioInstrutor) {
		this.dominioInstrutor = dominioInstrutor;
	}

	public void setMaterialDidatico(Integer materialDidatico) {
		this.materialDidatico = materialDidatico;
	}

	public void setApoioInstitucional(Integer apoioInstitucional) {
		this.apoioInstitucional = apoioInstitucional;
	}
}
