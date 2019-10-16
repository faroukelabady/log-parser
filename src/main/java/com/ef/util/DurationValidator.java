package com.ef.util;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import com.ef.model.DURATION_TIME;

public class DurationValidator implements IParameterValidator {

	@Override
	public void validate(String name, String duration) throws ParameterException {

		if (!duration.equalsIgnoreCase(DURATION_TIME.HOURLY.toString())
				&& !duration.equalsIgnoreCase(DURATION_TIME.DAILY.toString())) {
			throw new ParameterException("Parameter " + name + " should be only (hourly or daily)");
		}
	}

}
