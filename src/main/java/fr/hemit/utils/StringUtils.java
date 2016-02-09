package fr.hemit.utils;

import java.security.MessageDigest;

public class StringUtils {

	public static String HashPasswordWithMd5(String password){
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(password.getBytes());
			byte[] messageDigestMD5 = messageDigest.digest();
			StringBuffer stringBuffer = new StringBuffer();
			for (byte bytes : messageDigestMD5) {
				stringBuffer.append(String.format("%02x", bytes & 0xff));
			}
			return stringBuffer.toString();
		} catch (Exception e) {
			return null;
		}
	}
}
