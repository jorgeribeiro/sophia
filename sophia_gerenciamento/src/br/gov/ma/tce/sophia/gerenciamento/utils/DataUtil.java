package br.gov.ma.tce.sophia.gerenciamento.utils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Transient;

/*
 * Classe utilizada para iterar sobre dias de um período de uma atividade
 * Boolean selecionado serve para saber se o dia está selecionado 
 * no momento de criar ou atualizar uma atividade
 * 
 * Selecionado significa que a atividade acontecerá na data atribuída
 */
public class DataUtil {

	private Date data;
	private Date quantidadeHoras;
	private Boolean selecionado;

	public DataUtil(Date data, Date quantidadeHoras, Boolean selecionado) {
		super();
		this.data = data;
		this.quantidadeHoras = quantidadeHoras;
		this.selecionado = selecionado;
	}

	@Transient
	public String getDataStr() {
		try {
			DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT);
			return df.format(this.getData()) + " (" + getDiaDaSemana() + ")";
		} catch (Exception e) {
			return "-";
		}
	}

	private String getDiaDaSemana() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.getData());
		if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
			return "Domingo";
		} else if (calendar.get(Calendar.DAY_OF_WEEK) == 2) {
			return "Segunda-feira";
		} else if (calendar.get(Calendar.DAY_OF_WEEK) == 3) {
			return "Terça-feira";
		} else if (calendar.get(Calendar.DAY_OF_WEEK) == 4) {
			return "Quarta-feira";
		} else if (calendar.get(Calendar.DAY_OF_WEEK) == 5) {
			return "Quinta-feira";
		} else if (calendar.get(Calendar.DAY_OF_WEEK) == 6) {
			return "Sexta-feira";
		} else {
			return "Sábado";
		}
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getQuantidadeHoras() {
		return quantidadeHoras;
	}

	public void setQuantidadeHoras(Date quantidadeHoras) {
		this.quantidadeHoras = quantidadeHoras;
	}

	public Boolean getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

}
