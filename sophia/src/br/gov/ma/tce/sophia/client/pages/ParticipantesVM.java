package br.gov.ma.tce.sophia.client.pages;

import java.util.ArrayList;
import java.util.Collection;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import br.gov.ma.tce.sophia.ejb.beans.capacitacaoservidor.CapacitacaoServidor;
import br.gov.ma.tce.sophia.ejb.beans.capacitacaoservidor.CapacitacaoServidorFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscrito;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscritoFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.participanteinteresse.ParticipanteInteresse;
import br.gov.ma.tce.sophia.ejb.beans.participanteinteresse.ParticipanteInteresseFacadeBean;
import br.gov.ma.tce.sophia.ejb.beans.pessoa.Pessoa;
import br.gov.ma.tce.sophia.ejb.util.Filtro;

public class ParticipantesVM {
	private Pessoa participante;
	private ParticipanteInteresse participanteInteresse;
	private ParticipanteInscrito participanteInscrito;
	private Filtro filtroEvento;

	private static final String textoRodape = "%d item(ns) encontrado(s)";

	private Collection<ParticipanteInteresse> participantesInteressados;
	private Collection<ParticipanteInscrito> participantesInscritos;
	private Collection<CapacitacaoServidor> capacitacoesServidor;

	private ParticipanteInteresseFacadeBean participanteInteresseFacade;
	private ParticipanteInscritoFacadeBean participanteInscritoFacade;
	private CapacitacaoServidorFacadeBean capacitacaoServidorFacade;

	@Wire("#modalCancelarInscricao")
	private Window modalCancelarInscricao;

	@Wire("#modalSairListaDeInteresse")
	private Window modalSairListaDeInteresse;

	public ParticipantesVM() {
		try {
			InitialContext ctx = new InitialContext();
			participanteInteresseFacade = (ParticipanteInteresseFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/ParticipanteInteresseFacadeBean!br.gov.ma.tce.sophia.ejb.beans.participanteinteresse.ParticipanteInteresseFacadeBean");
			participanteInscritoFacade = (ParticipanteInscritoFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/ParticipanteInscritoFacadeBean!br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscritoFacadeBean");
			capacitacaoServidorFacade = (CapacitacaoServidorFacadeBean) ctx.lookup(
					"java:global/sophia_ear/sophia_ejb/CapacitacaoServidorFacadeBean!br.gov.ma.tce.sophia.ejb.beans.capacitacaoservidor.CapacitacaoServidorFacadeBean");
		} catch (NamingException e) {
			Clients.showNotification("Erro de conexão interno. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
			e.printStackTrace();
		}
	}

	@Init
	public void init() {
		participante = (Pessoa) Sessions.getCurrent().getAttribute("pessoa");
		participantesInscritos = new ArrayList<ParticipanteInscrito>();
		participantesInteressados = new ArrayList<ParticipanteInteresse>();
		capacitacoesServidor = new ArrayList<CapacitacaoServidor>();
		filtroEvento = new Filtro();
	}

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}

	@Command
	@NotifyChange(".")
	public void carregarPreInscricoesPorParticipante() {
		participantesInscritos = participanteInscritoFacade.findPreInscricoesByParticipante(participante);
	}

	@Command
	@NotifyChange(".")
	public void carregarListaDeInteressePorParticipante() {
		participantesInteressados = participanteInteresseFacade.findListaDeInteresseByParticipante(participante);
	}

	@Command
	@NotifyChange(".")
	public void carregarCapacitacoesPorParticipante() {
		capacitacoesServidor = capacitacaoServidorFacade.findCapacitacoesByParticipante(participante);
	}

	@Command
	@NotifyChange(".")
	public void aplicarFiltro(@BindingParam("listaDeInteresse") boolean listaDeInteresse) {
		try {
			if (listaDeInteresse) {
				participantesInteressados = participanteInteresseFacade
						.findListaDeInteresseByParticipanteAndFiltro(participante, filtroEvento);
			} else {
				participantesInscritos = participanteInscritoFacade
						.findPreInscricoesByParticipanteAndFiltro(participante, filtroEvento);
			}
		} catch (Exception e) {
			Clients.showNotification("Erro ao realizar busca por filtro. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void limparFiltro(@BindingParam("listaDeInteresse") boolean listaDeInteresse) {
		try {
			if (!filtroEvento.getFiltro1().isEmpty() || !filtroEvento.getFiltro2().isEmpty()) {
				filtroEvento.limparFiltro();
				if (listaDeInteresse) {
					participantesInteressados = participanteInteresseFacade
							.findListaDeInteresseByParticipante(participante);
				} else {
					participantesInscritos = participanteInscritoFacade.findPreInscricoesByParticipante(participante);
				}
			}
		} catch (Exception e) {
			Clients.showNotification("Erro ao realizar busca por filtro. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalCancelarPreInscricao(
			@BindingParam("participanteInscrito") ParticipanteInscrito participanteInscrito,
			@BindingParam("visible") boolean visible) {
		try {
			this.participanteInscrito = participanteInscrito;
			if (visible) {
				modalCancelarInscricao.setVisible(visible);
			} else {
				modalCancelarInscricao.setVisible(visible);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void cancelarPreInscricao() {
		try {
			participanteInscritoFacade.delete(this.participanteInscrito.getParticipanteInscritoId());
			carregarPreInscricoesPorParticipante();
			modalCancelarInscricao.setVisible(false);
			Clients.showNotification("Inscrição cancelada com sucesso!", Clients.NOTIFICATION_TYPE_INFO, null, null,
					3000, true);
		} catch (Exception e) {
			Clients.showNotification("Ocorreu um erro ao cancelar essa inscrição. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	@Command
	@NotifyChange(".")
	public void abrirModalSairListaDeInteresse(
			@BindingParam("participanteInteresse") ParticipanteInteresse participanteInteresse,
			@BindingParam("visible") boolean visible) {
		try {
			this.participanteInteresse = participanteInteresse;
			if (visible) {
				modalSairListaDeInteresse.setVisible(visible);
			} else {
				modalSairListaDeInteresse.setVisible(visible);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void sairListaDeInteresse() {
		try {
			participanteInteresseFacade.delete(this.participanteInteresse.getParticipanteInteresseId());
			carregarListaDeInteressePorParticipante();
			modalSairListaDeInteresse.setVisible(false);
			Clients.showNotification("Você saiu da lista de interesse com sucesso!", Clients.NOTIFICATION_TYPE_INFO,
					null, null, 3000, true);
		} catch (Exception e) {
			Clients.showNotification("Ocorreu um erro ao sair da lista de interesse. Tente novamente mais tarde.",
					Clients.NOTIFICATION_TYPE_WARNING, null, null, 3000, true);
		}
	}

	public Pessoa getParticipante() {
		return participante;
	}

	public void setParticipante(Pessoa participante) {
		this.participante = participante;
	}

	public Filtro getFiltroEvento() {
		return filtroEvento;
	}

	public void setFiltroEvento(Filtro filtroEvento) {
		this.filtroEvento = filtroEvento;
	}

	public String getTextoRodape() {
		if (!participantesInscritos.isEmpty()) {
			return String.format(textoRodape, participantesInscritos.size());
		} else if (!participantesInteressados.isEmpty()) {
			return String.format(textoRodape, participantesInteressados.size());
		} else {
			return String.format(textoRodape, capacitacoesServidor.size());
		}
	}

	public Collection<ParticipanteInteresse> getParticipantesInteressados() {
		return participantesInteressados;
	}

	public void setParticipantesInteressados(Collection<ParticipanteInteresse> participantesInteresses) {
		this.participantesInteressados = participantesInteresses;
	}

	public Collection<ParticipanteInscrito> getParticipantesInscritos() {
		return participantesInscritos;
	}

	public void setParticipantesInscritos(Collection<ParticipanteInscrito> participantesInscritos) {
		this.participantesInscritos = participantesInscritos;
	}

	public Collection<CapacitacaoServidor> getCapacitacoesServidor() {
		return capacitacoesServidor;
	}

	public void setCapacitacoesServidor(Collection<CapacitacaoServidor> capacitacoesServidor) {
		this.capacitacoesServidor = capacitacoesServidor;
	}

}
