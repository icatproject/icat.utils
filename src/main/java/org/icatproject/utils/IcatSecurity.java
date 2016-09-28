package org.icatproject.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class IcatSecurity {

	private static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F' };

	public static String digest(Long id, String location, String key) throws NoSuchAlgorithmException {
		byte[] pattern = (id + location + key).getBytes();
		MessageDigest digest = null;

		digest = MessageDigest.getInstance("SHA-256");

		byte[] bytes = digest.digest(pattern);
		char[] hexChars = new char[bytes.length * 2];
		int v;
		for (int j = 0; j < bytes.length; j++) {
			v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_CHARS[v >>> 4];
			hexChars[j * 2 + 1] = HEX_CHARS[v & 0x0F];
		}
		return new String(hexChars);
	}

}