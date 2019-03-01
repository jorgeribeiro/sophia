package br.gov.ma.tce.sophia.gerenciamento.utils;

import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscrito;
import br.gov.ma.tce.sophia.ejb.beans.participanteinteresse.ParticipanteInteresse;
import br.gov.ma.tce.sophia.ejb.beans.pessoa.Pessoa;

/*
 * Classe utilizada para marcar destinatários de envio de mensagens
 * na tela "Envio de mensagens", podendo ser um ParticipanteInscrito ou
 * ParticipanteInteresse ou mesmo uma Pessoa
 */
public class DestinatarioUtil {

	private ParticipanteInscrito participanteInscrito;
	private ParticipanteInteresse participanteInteresse;
	private Pessoa pessoa;
	private Boolean selecionado;

	public DestinatarioUtil(ParticipanteInscrito participanteInscrito, ParticipanteInteresse participanteInteresse,
			Pessoa pessoa, Boolean selecionado) {
		super();
		this.participanteInscrito = participanteInscrito;
		this.participanteInteresse = participanteInteresse;
		this.pessoa = pessoa;
		this.selecionado = selecionado;
	}

	public ParticipanteInscrito getParticipanteInscrito() {
		return participanteInscrito;
	}

	public void setParticipanteInscrito(ParticipanteInscrito participanteInscrito) {
		this.participanteInscrito = participanteInscrito;
	}

	public ParticipanteInteresse getParticipanteInteresse() {
		return participanteInteresse;
	}

	public void setParticipanteInteresse(ParticipanteInteresse participanteInteresse) {
		this.participanteInteresse = participanteInteresse;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Boolean getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	public String getNome() {
		if (participanteInscrito != null) {
			return participanteInscrito.getNomeParticipante();
		} else if (participanteInteresse != null) {
			return participanteInteresse.getNomeParticipante();
		} else {
			return pessoa.getNome();
		}
	}
	
	public String getEmail() {
		if (participanteInscrito != null) {
			return participanteInscrito.getParticipante().getEmail();
		} else if (participanteInteresse != null) {
			return participanteInteresse.getParticipante().getEmail();
		} else {
			return pessoa.getEmail();
		}
	}

	public String getTipoParticipante() {
		if (participanteInscrito != null) {
			return participanteInscrito.getParticipante().getTipoParticipanteStr();
		} else if (participanteInteresse != null) {
			return participanteInteresse.getParticipante().getTipoParticipanteStr();
		} else {
			return pessoa.getTipoParticipanteStr();
		}
	}

}
