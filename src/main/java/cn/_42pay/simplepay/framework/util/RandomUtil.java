package cn._42pay.simplepay.framework.util;

import java.util.Random;

/**
 * Created by kevin on 2018/6/21.
 */
public class RandomUtil {
	private static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String LETTERCHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String UPPERLETTERCHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String LOWERLETTERCHAR = "abcdefghijklmnopqrstuvwxyz";
	private static final String NUMBERCHAR = "0123456789";
	private static final String NUMBERLOWERLETTERCHAR = "0123456789abcdefghijklmnopqrstuvwxyz";
	private static final String NUMBERUPPERLETTERCHAR = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static String getRandomAllChar(int n) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < n; i++) {
			sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
		}
		return sb.toString();
	}

	public static String getRandomllCharToUpperCase(int n) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < n; i++) {
			sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
		}
		return sb.toString().toUpperCase();
	}

	public static String getRandomAllCharToLowerCase(int n) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < n; i++) {
			sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
		}
		return sb.toString().toLowerCase();
	}

	public static String getRandomLetterChar(int n) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < n; i++) {
			sb.append(LETTERCHAR.charAt(random.nextInt(LETTERCHAR.length())));
		}
		return sb.toString();
	}

	public static String getRandomUpperLetterChar(int n) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < n; i++) {
			sb.append(UPPERLETTERCHAR.charAt(random.nextInt(UPPERLETTERCHAR.length())));
		}
		return sb.toString();
	}

	public static String getRandomLowerLetterChar(int n) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < n; i++) {
			sb.append(LOWERLETTERCHAR.charAt(random.nextInt(LOWERLETTERCHAR.length())));
		}
		return sb.toString();
	}

	public static String getRandomNumberChar(int n) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < n; i++) {
			sb.append(NUMBERCHAR.charAt(random.nextInt(NUMBERCHAR.length())));
		}
		return sb.toString();
	}

	public static String getRandomNumberLowerLetterChar(int n) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < n; i++) {
			sb.append(NUMBERLOWERLETTERCHAR.charAt(random.nextInt(NUMBERLOWERLETTERCHAR.length())));
		}
		return sb.toString();
	}

	public static String getRandomNumberUpperLetterChar(int n) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < n; i++) {
			sb.append(NUMBERUPPERLETTERCHAR.charAt(random.nextInt(NUMBERUPPERLETTERCHAR.length())));
		}
		return sb.toString();
	}
}
