package br.gov.ma.tce.sophia.gerenciamento.pages;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

import br.gov.ma.tce.sophia.ejb.beans.usuario.Usuario;

public class IndexVM {
	private Usuario usuario;
	private String caminhoInclude;
	private String titleHome;

	@Wire("#modalSair")
	private Window modalSair;
	
	public IndexVM() {
		
	}
	
	@Init
	public void init() {
		usuario = (Usuario) Sessions.getCurrent().getAttribute("usuario");
		if (usuario == null) {
			Executions.sendRedirect("/login.zul");
		} else {
			if (usuario.getPessoa().getSexo().equals("MASCULINO")) {
				titleHome = "Bem vindo, ";
			} else {
				titleHome = "Bem vinda, ";
			}
		}

		// Box inicial
		caminhoInclude = (String) Sessions.getCurrent().getAttribute("paginaatual");
		if (caminhoInclude == null) {
			caminhoInclude = "home.zul";
		}
	}

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}

	@Command
	@NotifyChange(".")
	public void abrirModalSair(@BindingParam("visible") boolean visible) {
		try {
			if (visible) {
				modalSair.setVisible(true);
			} else {
				modalSair.setVisible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void sair() {
		try {
			Sessions.getCurrent().invalidate();
			Executions.sendRedirect("/login.zul");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange(".")
	public void homepage() {
		caminhoInclude = "home.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void criarEvento() {
		caminhoInclude = "eventos/criarevento.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void gerenciarEventos() {
		caminhoInclude = "eventos/gerenciareventos.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void eventosRealizados() {
		caminhoInclude = "eventos/eventosrealizados.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void eventosCancelados() {
		caminhoInclude = "eventos/eventoscancelados.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void criarAtividade() {
		caminhoInclude = "atividades/criaratividade.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void gerenciarAtividades() {
		caminhoInclude = "atividades/gerenciaratividades.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}
	
	@Command
	@NotifyChange(".")
	public void disponibilizarMaterial() {
		caminhoInclude = "atividades/disponibilizarMaterial.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void inscreverParticipantes() {
		caminhoInclude = "atividades/inscreverparticipantes.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void registrarPresenca() {
		caminhoInclude = "atividades/registrarpresenca.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void relatorioDeAvaliacaoDeReacaoAtividades() {
		caminhoInclude = "atividades/relatoriodeavaliacaodeatividades.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void gerenciarParticipantes() {
		caminhoInclude = "participantes/gerenciarparticipantes.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void gerenciarInscricoes() {
		caminhoInclude = "participantes/gerenciarinscricoes.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void gerenciarListasDeInteresse() {
		caminhoInclude = "participantes/gerenciarlistasdeinteresse.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void registroDeCapacitacoes() {
		caminhoInclude = "participantes/registrodecapacitacoes.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void relatorioMensalCapacitacoes() {
		caminhoInclude = "participantes/relatoriomensalcapacitacoes.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void envioDeMensagens() {
		caminhoInclude = "participantes/enviodemensagens.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void atribuirColaboradoresAAtividades() {
		caminhoInclude = "colaboradores/atribuircolaboradoresaatividades.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void gerenciarColaboradores() {
		caminhoInclude = "colaboradores/gerenciarcolaboradores.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void criarModeloDeCertificado() {
		caminhoInclude = "certificados/criarmodelodecertificado.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void disponibilizarCertificados() {
		caminhoInclude = "certificados/disponibilizarcertificados.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	@Command
	@NotifyChange(".")
	public void certificadosDisponibilizados() {
		caminhoInclude = "certificados/certificadosdisponibilizados.zul";
		Sessions.getCurrent().setAttribute("paginaatual", caminhoInclude);
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getCaminhoInclude() {
		return caminhoInclude;
	}

	public void setCaminhoInclude(String caminhoInclude) {
		this.caminhoInclude = caminhoInclude;
	}

	public String getTitleHome() {
		if (usuario != null) {
			return titleHome + usuario.getPessoa().getNomeSobrenome();
		} else {
			return "";
		}
	}

	public void setTitleHome(String titleHome) {
		this.titleHome = titleHome;
	}

}
