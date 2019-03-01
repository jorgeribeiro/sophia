package br.gov.ma.tce.sophia.ejb.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;

public class HashUtil {
	private static MessageDigest md;
	private static ArrayList<String> senhas;

	public static String hashSenha(String senha) {
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.update(senha.getBytes());
		byte[] digest = md.digest();
		String hash = DatatypeConverter.printHexBinary(digest).toUpperCase();
		return hash;
	}

	public static Boolean rehashSenha(String senha, String token) {
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.update(senha.getBytes());
		byte[] digest = md.digest();
		String hash = DatatypeConverter.printHexBinary(digest).toUpperCase();
		return hash.equals(token);
	}

	public static String getSenhaRandom() {
		senhas = new ArrayList<String>();
		senhas.add("1sg5ax2");
		senhas.add("2h5fdu9");
		senhas.add("3t8qa5k");
		senhas.add("4u6f9w0");
		senhas.add("5q6açs4");
		senhas.add("6z8u6g2");
		senhas.add("7f2s5w9");
		senhas.add("8b5p0q3");
		senhas.add("9f3c6q8");
		Random random = new Random();
		int index = random.nextInt(senhas.size());
		return senhas.get(index);
	}

}
