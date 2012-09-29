package com.chenjw.tool.beancopy.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	private static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd/";

	public static String toLocaleString(Date date, String dateFormat) {
		if (date == null) {
			return "";
		}
		if (dateFormat == null || dateFormat.length() == 0) {
			return new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(date);
		}
		return new SimpleDateFormat(dateFormat).format(date);
	}

	public static Date parseDate(String date, String dateformat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateformat);

		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}
}
