package cn._42pay.simplepay.framework.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by kevin on 2018/6/21.
 */
public class DateUtil {
	public static final String YYYY_MM_DD_HH_MM_SS_NO_DIVIDE = "yyyyMMddHHmmss";

	private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	private static final String YYYY_MM_DD = "yyyy-MM-dd";
	private static final String YYYY_MM_DD_NO_DIVIDE = "yyyyMMdd";
	private static final String T = "T";
	private static final String EMPTY_STRING = " ";
	private static final String YYYY_MM_DD_HH_MM_SS_SSS_NO_DIVIDE="yyyyMMddHHmmssSSS";

	public static String dateToString(Date date) {
		SimpleDateFormat dateformat = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
		dateformat.format(date);
		return dateformat.format(date);
	}

	public static String dateToStringNoDivide(Date date) {
		SimpleDateFormat dateformat = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS_NO_DIVIDE);
		dateformat.format(date);
		return dateformat.format(date);
	}

	public static String dateToStringYmdNoDivide(Date date) {
		SimpleDateFormat dateformat = new SimpleDateFormat(YYYY_MM_DD_NO_DIVIDE);
		dateformat.format(date);
		return dateformat.format(date);
	}

	public static String dateToStringMillonSecondNoDivide(Date date) {
		SimpleDateFormat dateformat = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS_SSS_NO_DIVIDE);
		dateformat.format(date);
		return dateformat.format(date);
	}

	public static Date stringToDate(String dateStr) {
		SimpleDateFormat sdf = null;
		Date date = new Date();
		if (dateStr.length() > 10) {
			if (dateStr.contains(T)) {
				dateStr = dateStr.replace(T, EMPTY_STRING);
			}
			sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
		} else {
			sdf = new SimpleDateFormat(YYYY_MM_DD);
		}
		try {
			date = sdf.parse(dateStr);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			return date;
		}
		return date;
	}

	public static String parseDatetime(String str, String pattern) {
		DateFormat formatter = new SimpleDateFormat(pattern);
		String result = null;
		try {
			result = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).format(formatter.parse(str));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
}

