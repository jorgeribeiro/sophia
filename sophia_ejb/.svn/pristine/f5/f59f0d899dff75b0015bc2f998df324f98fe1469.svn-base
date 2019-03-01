package br.gov.ma.tce.sophia.ejb.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscrito;

public class ListaPresencaVO {
	private String nome;
	private String email;
	private String cpf;

	public ListaPresencaVO(String nome, String email, String cpf) {
		this.nome = nome;
		this.email = email;
		this.cpf = cpf;
	}

	public static List<ListaPresencaVO> preencheLista(Collection<ParticipanteInscrito> participantesInscritos,
			int espacosEmBranco) {
		List<ListaPresencaVO> listaPresenca = new ArrayList<ListaPresencaVO>();
		for (ParticipanteInscrito pi : participantesInscritos) {
			ListaPresencaVO object = new ListaPresencaVO(pi.getNomeParticipante(), pi.getParticipante().getEmail(),
					pi.getParticipante().getCpfStr());
			listaPresenca.add(object);
		}

		for (int i = 0; i < espacosEmBranco; i++) {
			ListaPresencaVO object = new ListaPresencaVO(null, null, null);
			listaPresenca.add(object);
		}

		return listaPresenca;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

}
