package com.ef.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utilities {
	
	public static LocalDateTime StringToLocalDateTime(String value, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		LocalDateTime dateTime = LocalDateTime.parse(value, formatter);
		return dateTime;
	}
	
	public static String trimDoubleQuotation(String value) {
		return value.replaceAll("^\"|\"$", "");
	}

	public static String readScriptFromResources(String fileName) {
		InputStream input = Utilities.class.getResourceAsStream("/" + fileName);
		BufferedReader in = new BufferedReader(new InputStreamReader(input));
		String str;
		StringBuilder sb = new StringBuilder();
		try {
			while ((str = in.readLine()) != null) {
				sb.append(str + "\n ");
			}
			in.close();
		} catch (IOException ioe) {
			throw new RuntimeException("couldn't read resource or error closing file.", ioe);
		}

		return sb.toString();
	}

}
