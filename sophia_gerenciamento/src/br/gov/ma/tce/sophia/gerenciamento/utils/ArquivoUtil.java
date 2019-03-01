package br.gov.ma.tce.sophia.gerenciamento.utils;

import java.io.InputStream;
import java.text.Normalizer;
import java.util.ArrayList;

import org.zkoss.util.media.Media;

public class ArquivoUtil {
	
	public boolean uploadArquivoFTP(Media media, String nomeArquivo, String nomePasta) {
		try {
			FTPUtil ftpUtil = new FTPUtil();
			// String type = media.getContentType().split("/")[0];
			ftpUtil.upload2(media, nomeArquivo, nomePasta);
			return true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public ArrayList<String> listarTodosOSArquivosFTP(String nomePasta){
		try {
			FTPUtil ftpUtil = new FTPUtil();
			ArrayList<String> arquivos = ftpUtil.listarArquivos(nomePasta);
			return arquivos;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public InputStream downloadArquivoFTP(String nomeArquivo, String nomePasta) {
		try {
			FTPUtil ftpUtil = new FTPUtil();
			InputStream in = ftpUtil.downloadArquivo2(nomeArquivo, nomePasta);
			return in;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public boolean removerArquivoFTP(String nomeArquivo, String nomePasta) {
		try {
			FTPUtil ftpUtil = new FTPUtil();
			// String type = media.getContentType().split("/")[0];
			ftpUtil.removeArquivo(nomeArquivo, nomePasta);
			return true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String formatarNomeArquivo(String nomeArquivo) {
		return Normalizer.normalize(nomeArquivo, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll("/", "");
	}
}
