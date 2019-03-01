package br.gov.ma.tce.sophia.client.utils;

import java.io.InputStream;
import java.text.Normalizer;
import java.util.ArrayList;

public class ArquivoUtil {

	public ArrayList<String> listarTodosOSArquivosFTP(String nomePasta) {
		try {
			FTPUtil ftpUtil = new FTPUtil();
			ArrayList<String> arquivos = ftpUtil.listarArquivos(nomePasta);
			return arquivos;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public InputStream downloadArquivoFTP(String nomeArquivo, String nomePasta) {
		try {
			FTPUtil ftpUtil = new FTPUtil();
			InputStream in = ftpUtil.downloadArquivo2(nomeArquivo, nomePasta);
			return in;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public String formatarNomeArquivo(String nomeArquivo) {
		return Normalizer.normalize(nomeArquivo, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll("/",
				"");
	}

}
