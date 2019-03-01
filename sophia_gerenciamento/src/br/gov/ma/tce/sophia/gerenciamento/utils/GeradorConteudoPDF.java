package br.gov.ma.tce.sophia.gerenciamento.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

import javax.imageio.ImageIO;

import org.zkoss.zk.ui.Executions;

import br.gov.ma.tce.sophia.ejb.beans.atividade.Atividade;
import br.gov.ma.tce.sophia.ejb.beans.modelocertificado.ModeloCertificado;
import br.gov.ma.tce.sophia.ejb.beans.participanteinscrito.ParticipanteInscrito;

public class GeradorConteudoPDF {

	// Método não mais utilizado. Gerando lista com JasperReports
	public static String geradorListaDePresenca(Collection<ParticipanteInscrito> participantesInscritos,
			Atividade atividade, String dia, Integer espacosEmBranco) {
		try {
			String url = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
			StringBuilder html = new StringBuilder();

			html.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" "
					+ "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
			html.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
			html.append("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
			html.append("<title>LISTA DE PRESENÇA DE ATIVIDADE</title>");
			html.append("</head><body>");
			html.append("<table width=\"1100px\" style=\"background-image: '" + url
					+ "/imagens/fundo_lista.png'; background-repeat:no-repeat; background-size: 100% 100%\" >");			
			html.append("<tr><td><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p></td></tr>");
			html.append("<table align=\"center\" "
					+ "style=\"text-align:center; font-size:15pt; font-family:arial,sans-serif; "
					+ "border-collapse:collapse; width:100%; margin-left:80px; margin-top:11px; margin-bottom:15px\" >");
			html.append("<tr><td align=\"center\"><p align=\"center\" " + "style=\"text-align:center;\"><strong><span "
					+ "style=\"font-family:arial,sans-serif; font-size:20pt\" >"
					+ "LISTA DE PRESENÇA</span></strong></p></td></tr>");
			html.append("<tr><td align=\"center\"><p align=\"center\" " + "style=\"text-align:center;\"><span "
					+ "style=\"font-family:arial,sans-serif \">" + "EVENTO: <strong>" + atividade.getEvento().getNome()
					+ "</strong></span></p></td></tr>");
			html.append("<tr><td align=\"center\"><p align=\"center\" " + "style=\"text-align:center;\"><span "
					+ "style=\"font-family:arial,sans-serif \">" + "ATIVIDADE: <strong>" + atividade.getNome()
					+ "</strong></span></p></td></tr>");
			html.append("<tr><td align=\"center\"><p align=\"center\" " + "style=\"text-align:center;\"<span "
					+ "style=\"font-family:arial,sans-serif \">" + "DATA: <strong>" + dia
					+ "</strong></span></p></td></tr>");
			html.append(
					"<div align=\"center\"><table style=\"text-align:center; font-size:15pt; font-family:arial,sans-serif; border:2px solid #dddddd; border-collapse: collapse; width:100%\" ><tr><th style=\"border:1px solid #dddddd\" align=\"center\">Nº</th>"
							+ "<th style=\"border:1px solid #dddddd\" align=\"center\" width=\"200px\">NOME</th><th style=\"border:1px solid #dddddd\" align=\"center\">E-MAIL</th>"
							+ "<th style=\"border:1px solid #dddddd\" align=\"center\" width=\"150px\">CPF</th><th style=\"border:1px solid #dddddd\" width=\"490px\">ASSINATURA</th></tr>");

			int count = 0;
			for (ParticipanteInscrito pi : participantesInscritos) {
				count++;
				html.append("<tr><td height=\"40px\" align=\"center\" style=\"border:1px solid #dddddd\">" + count
						+ "</td>");
				html.append("<td height=\"40px\" style=\"border:1px solid #dddddd\">" + pi.getParticipante().getNome()
						+ "</td>");
				html.append("<td height=\"40px\" style=\"border:1px solid #dddddd\" align=\"center\">"
						+ pi.getParticipante().getEmail() + "</td>");
				html.append("<td height=\"40px\" style=\"border:1px solid #dddddd\" align=\"center\">"
						+ pi.getParticipante().getCpfStr() + "</td>");
				html.append("<td height=\"40px\" style=\"border:1px solid #dddddd\"><p></p></td></tr>");
			}

			for (int i = 0; i < espacosEmBranco; i++) {
				count++;
				html.append("<tr><td height=\"40px\" align=\"center\" style=\"border:1px solid #dddddd\">" + count
						+ "</td>");
				html.append("<td style=\"border:1px solid #dddddd\"><p></p></td>");
				html.append("<td style=\"border:1px solid #dddddd\"><p></p></td>");
				html.append("<td style=\"border:1px solid #dddddd\"><p></p></td>");
				html.append("<td style=\"border:1px solid #dddddd\"><p></p></td></tr>");
			}
			html.append("</td></tr></div></table></body></html>");

			return html.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Método não mais utilizado. Gerando lista com JasperReports
	public static String geradorCertificado(ModeloCertificado modeloCertificado, String token) {
		try {
			// Download da imagem de fundo
			FTPUtil ftpUtil = new FTPUtil();
			InputStream in = ftpUtil.downloadArquivo2(modeloCertificado.getFundoCertificado().getNomeFtp(),
					"fundos_certificado");

			// Converte imagem para uri
			BufferedImage imagemDeFundo = ImageIO.read(in);
			ImageIO.write(imagemDeFundo, "png", new File("tmpImage.png"));
			byte[] imageBytes = Files.readAllBytes(Paths.get("tmpImage.png"));
			Base64.Encoder encoder = Base64.getEncoder();
			String enconding = "data:image/png;base64," + encoder.encodeToString(imageBytes);

			// Construindo texto de data
			Date data = new Date();
			String dataStr = DateFormat.getDateInstance(DateFormat.LONG).format(data);

			StringBuilder html = new StringBuilder();
			html.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" "
					+ "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
			html.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
			html.append("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
			html.append("<title>CERTIFICADO ESCEX</title>");
			html.append("</head><body>");
			html.append("<table width=\"1100px\" style=\"background-image: url('" + enconding
					+ "'); background-repeat: no-repeat; background-size: 100% 100%;\">");
			html.append("<tr><td><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p>");
			html.append("<table width=\"954\" height=\"484\" border=\"0\" align=\"center\">");
			html.append("<tr><td height=\"115\" align=\"center\"><p align=\"center\" "
					+ "style=\"text-align:center;\"><strong><span style=\"font-family:'times', "
					+ "'serif'; font-size:60pt\">Certificado</td></span></strong></p></tr>");
			html.append("<tr><td width=\"954\" height=\"192\" ><p style=\"font-family: 'Franklin Gothic Book',"
					+ "'sans-serif'; font-size: 16.0pt; text-align: justify; text-indent:100px\">"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + modeloCertificado.getTexto()
					+ "</p></td></tr>");
			html.append("<tr><td height=\"170\"><div width=\"950px\"><p class=\"MsoHeader\" align=\"center\" "
					+ "style=\"text-align:center;\"><span style=\"font-family:'Franklin Gothic Book','sans-serif'; "
					+ "font-size:16.0pt; \">São Luís, " + dataStr + ".</span></p>");
			html.append("<p>&nbsp;</p>");
			html.append("<p>&nbsp;</p>");
			html.append("<p>&nbsp;</p>");
			// Linhas removidas pois o nome da assinatura vai junto com o fundo do certificado
			/*html.append("<p class=\"MsoHeader\" align=\"center\" style=\"text-align:center;\">"
					+ "<strong><span style=\"font-family:'Franklin Gothic Book','sans-serif'; "
					+ "font-size:14.0pt;\">William Jobim Farias");
			html.append("<br/>Gestor da ESCEX/TCE-MA</span></strong></p></div></td>");*/
			html.append("<tr><td height=\"50\"><p>&nbsp;</p><p class=\"MsoHeader\" "
					+ "style=\"font-family:'Franklin Gothic Book','sans-serif';\">Token de verificação: " + token
					+ "<br/>Verifique a autenticidade desse certificado no SOPHIA.</p></td></tr>");
			html.append("</tr></table></td></tr></table></body></html>");

			return html.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
