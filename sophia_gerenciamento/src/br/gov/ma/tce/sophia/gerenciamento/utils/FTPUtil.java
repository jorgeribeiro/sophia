package br.gov.ma.tce.sophia.gerenciamento.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.zkoss.util.media.Media;

public class FTPUtil {

	public InputStream downloadArquivo2(String nomeArquivo, String nomePasta) throws Exception {

		FTPClient ftp = new FTPClient();
		InputStream in = null;
		ByteArrayOutputStream baos = null;

		try {

			ftp.connect("ip", 10021);
			if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
				ftp.login("login", "password");
			} else {
				ftp.disconnect();
				throw new Exception("Não Foi Possível Estabelecer Uma Conexão com o Servidor de Arquivos!!");
			}

			boolean exists = ftp.changeWorkingDirectory(nomePasta);

			if (exists) {
				ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
				baos = new ByteArrayOutputStream();
				ftp.retrieveFile(nomeArquivo, baos);
				in = new ByteArrayInputStream(baos.toByteArray());
			}
		} finally {
			if (baos != null) {
				baos.close();
			}
			if (in != null) {
				in.close();
			}
			ftp.disconnect();
		}
		return in;
	}

	public void upload2(Media media, String filename, String nomePasta) {

		FTPClient client = new FTPClient();
		InputStream fis = null;

		try {
			client.connect("ip", 10021);
			client.login("login", "password");

			fis = media.getStreamData();
			boolean exists = client.changeWorkingDirectory(nomePasta);

			if (!exists) {
				client.mkd(nomePasta);
				client.changeWorkingDirectory(nomePasta);
			}

			client.setFileType(FTP.BINARY_FILE_TYPE);
			client.storeFile(filename, fis);
			client.logout();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				client.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void removeArquivo(String nomeArquivo, String nomePasta) {

		FTPClient client = new FTPClient();
		try {
			client.connect("ip", 10021);
			client.login("login", "password");
			client.changeWorkingDirectory(nomePasta);
			client.removeDirectory(nomePasta);
			client.deleteFile(nomeArquivo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> listarArquivos(String nomePasta) {
		ArrayList<String> arquivos = new ArrayList<String>();
		FTPClient client = new FTPClient();
		try {
			client.connect("ip", 10021);
			client.login("login", "password");
			boolean exists = client.changeWorkingDirectory(nomePasta);
			if (exists) {
				FTPFile[] files = client.listFiles();
				for (FTPFile ftpFile : files) {
					arquivos.add(ftpFile.getName());
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arquivos;
	}

	@SuppressWarnings("unused")
	private static void showServerReply(FTPClient ftpClient) {
		String[] replies = ftpClient.getReplyStrings();
		if (replies != null && replies.length > 0) {
			for (String aReply : replies) {
				System.out.println("SERVER: " + aReply);
			}
		}
	}

}
