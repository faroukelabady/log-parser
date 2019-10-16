package com.ef.util;

import java.time.LocalDateTime;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

public class LocalDateTimeConverter implements IStringConverter<LocalDateTime> {

	private static final String DATE_TIME_PATTERN = "yyyy-MM-dd.HH:mm:ss";

	@Override
	public LocalDateTime convert(String value) {
		try {
			return Utilities.StringToLocalDateTime(value, DATE_TIME_PATTERN);
		} catch (Exception e) {
			throw new ParameterException("invalid date format");
		}
	}

}
