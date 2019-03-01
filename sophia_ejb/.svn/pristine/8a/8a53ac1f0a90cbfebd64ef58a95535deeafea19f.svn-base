package br.gov.ma.tce.sophia.ejb.util;

public class Filtro {
	private String filtro1 = "", filtro2 = "";

	public String getFiltro1() {
		return filtro1;
	}

	public void setFiltro1(String filtro1) {
		this.filtro1 = filtro1;
	}

	public String getFiltro2() {
		return filtro2;
	}

	public void setFiltro2(String filtro2) {
		this.filtro2 = filtro2;
	}

	public void limparFiltro() {
		this.filtro1 = "";
		this.filtro2 = "";
	}

	public boolean filtroVazio() {
		if ((filtro1 == null || filtro1.trim().equals("")) && (filtro2 == null || filtro2.trim().equals(""))) {
			return true;
		}
		return false;
	}

}
