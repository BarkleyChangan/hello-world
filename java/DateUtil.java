package com.post.util;

import java.text.SimpleDateFormat;

public class DateUtil {
	private static ThreadLocal<SimpleDateFormat> tl = new ThreadLocal<SimpleDateFormat>();

	public static SimpleDateFormat getSimpleDateFormat(String datePattern) {
		if (datePattern == null || datePattern.trim().length() == 0) {
			datePattern = "yyyy-MM-dd HH:mm:ss";
		}

		SimpleDateFormat sdf = tl.get();

		if (sdf == null) {
			sdf = new SimpleDateFormat(datePattern);
			tl.set(sdf);
		}

		return sdf;
	}
}