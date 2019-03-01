package br.gov.ma.tce.sophia.gerenciamento.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.gov.ma.tce.sophia.ejb.beans.formacaoacademica.FormacaoAcademica;
import br.gov.ma.tce.sophia.ejb.beans.formacaoacademica.FormacaoAcademicaFacadeBean;

public class EscolaridadeUtil {

	FormacaoAcademicaFacadeBean formacaoAcademicaFacade;
	List<String> escolaridades = new ArrayList<String>();

	public EscolaridadeUtil() {
		try {
			InitialContext ctx = new InitialContext();
			formacaoAcademicaFacade = (FormacaoAcademicaFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/FormacaoAcademicaFacadeBean!br.gov.ma.tce.sophia.ejb.beans.formacaoacademica.FormacaoAcademicaFacadeBean");
		} catch (NamingException e) {
			e.printStackTrace();
		}

		preencheArray();
	}

	public void preencheArray() {
		escolaridades.add("ENSINO FUNDAMENTAL");
		escolaridades.add("ENSINO MÉDIO");
		escolaridades.add("ENSINO TÉCNICO");
		escolaridades.add("ENSINO SUPERIOR");
		escolaridades.add("ESPECIALIZAÇÃO");
		escolaridades.add("MESTRADO");
		escolaridades.add("DOUTORADO");
		escolaridades.add("PÓS-DOUTORADO");
	}

	public String getEscolaridade(String id) {
		return escolaridades.get(Integer.parseInt(id));
	}

	public boolean showFormacaoAcademica(String escolaridade) {
		if (escolaridade.equals(escolaridades.get(0)) || escolaridade.equals(escolaridades.get(1))) {
			return false;
		} else {
			return true;
		}
	}

	public Collection<FormacaoAcademica> getFormacoesAcademicas(String escolaridade) {

		int id = escolaridades.indexOf(escolaridade);

		if (id > 3) {
			id = 3;
		}

		if (showFormacaoAcademica(escolaridade)) {
			return formacaoAcademicaFacade.findByFormacao(id + 1);
		}
		return null;
	}

	public List<String> getEscolaridades() {
		return escolaridades;
	}

	public void setEscolaridades(List<String> escolaridades) {
		this.escolaridades = escolaridades;
	}
}