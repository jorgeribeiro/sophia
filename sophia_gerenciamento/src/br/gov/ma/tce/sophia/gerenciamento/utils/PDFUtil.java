package br.gov.ma.tce.sophia.gerenciamento.utils;

import java.awt.Dimension;
import java.awt.Insets;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.security.InvalidParameterException;

import org.zefer.pd4ml.PD4Constants;
import org.zefer.pd4ml.PD4ML;
import org.zefer.pd4ml.PD4PageMark;
import org.zkoss.zhtml.Filedownload;

public class PDFUtil {

	// margens do documento pd4ml
	protected Dimension format = PD4Constants.A4;
	protected boolean landscapeValue = true;
	protected int topValue = 10;
	protected int leftValue = 10;
	protected int rightValue = 10;
	protected int bottomValue = 10;

	// Método não utilizado mais. Gerando lista com JasperReports
	public InputStream gerarPdfListaDePresenca(String conteudo, String fileName, Boolean realizarDownload) {
		try {
			PD4ML pd4ml = new PD4ML();
			int userSpaceWidth = 1450;

			PD4PageMark footer = new PD4PageMark();
			footer.setAreaHeight(15);
			footer.setFontSize(10);
			footer.setPageNumberTemplate("página $[page]");
			footer.setTitleAlignment(PD4PageMark.RIGHT_ALIGN);
			footer.setPageNumberAlignment(PD4PageMark.LEFT_ALIGN);
			pd4ml.setPageFooter(footer);

			// pd4ml.enableTableBreaks(true);
			pd4ml.generateOutlines(true);
			pd4ml.setHtmlWidth(userSpaceWidth); // set frame width of "virtual
												// web browser"
			pd4ml.setPageSize(landscapeValue ? pd4ml.changePageOrientation(format) : format);
			pd4ml.setPageInsetsMM(new Insets(topValue, leftValue, bottomValue, rightValue));
			pd4ml.addStyle("BODY {margin: 0}", true);
			//pd4ml.addStyle("TABLE {page-break-inside: avoid !important}", true);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			pd4ml.render(new StringReader(conteudo), baos);
			baos.close();
			InputStream in = new ByteArrayInputStream(baos.toByteArray());

			/*
			 * // Para gerar arquivo e guardar em vez de fazer download byte[] buffer = new
			 * byte[in.available()]; in.read(buffer);
			 * 
			 * File targetFile = new File("/home/bwlobao/Documentos/NomeDaPasta/" +
			 * fileName); OutputStream outStream = new FileOutputStream(targetFile);
			 * outStream.write(buffer);
			 * 
			 * outStream.close();
			 */

			if (realizarDownload) {
				Filedownload.save(in, "application/pdf", fileName);
			}
			return in;
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public InputStream gerarPdfCertificado(String conteudo, String fileName, Boolean realizarDownload) {
		try {
			PD4ML pd4ml = new PD4ML();
			int userSpaceWidth = 1110;

			pd4ml.generateOutlines(true);
			pd4ml.setHtmlWidth(userSpaceWidth); // set frame width of "virtual
												// web browser"
			pd4ml.setPageSize(landscapeValue ? pd4ml.changePageOrientation(format) : format);
			pd4ml.setPageInsetsMM(new Insets(topValue, leftValue, 0, rightValue));
			pd4ml.addStyle("BODY {margin: 0}", true);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			pd4ml.render(new StringReader(conteudo), baos);
			baos.close();
			InputStream in = new ByteArrayInputStream(baos.toByteArray());

			/*
			 * // Para gerar arquivo e guardar em vez de fazer download byte[] buffer = new
			 * byte[in.available()]; in.read(buffer);
			 * 
			 * File targetFile = new File("/home/bwlobao/Documentos/NomeDaPasta/" +
			 * fileName); OutputStream outStream = new FileOutputStream(targetFile);
			 * outStream.write(buffer);
			 * 
			 * outStream.close();
			 */

			if (realizarDownload) {
				Filedownload.save(in, "application/pdf", fileName);
			}
			return in;
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
