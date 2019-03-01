package br.gov.ma.tce.sophia.gerenciamento.utils;

import java.security.SecureRandom;

/**
 * Classe com método gerador de tokens únicos para certificados
 * @author jorgeribeiro
 *
 */
public class GeradorDeToken {

	protected static SecureRandom random = new SecureRandom();
	
	public static String generateToken() {
		long longToken = Math.abs(random.nextLong());
		String random = Long.toString(longToken, 16);
		return random;
	}
}
