package com.ef.util;

import java.nio.file.Path;
import java.time.LocalDateTime;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.PathConverter;
import com.beust.jcommander.validators.PositiveInteger;

@Parameters(separators = "=")
public class ArgumentParser {

	@Parameter(names = "--accesslog",required = false, converter = PathConverter.class)
	private Path logFilePath;

	@Parameter(names = "--duration",required = true , validateWith = DurationValidator.class, description = "only accepts {daily or hourly} constants")
	private String duration;

	@Parameter(names = "--startDate", required = true, converter = LocalDateTimeConverter.class, description="Format must be yyyy-MM-dd.HH:mm:ss")
	private LocalDateTime startDate;

	@Parameter(names = "--threshold", required = false, validateWith = PositiveInteger.class, description="accepts only + integers")
	private Integer Threshold;

	public boolean isParsingSuccess(String[] args) {
		JCommander commander = JCommander.newBuilder().addObject(this).build();
		try {
			commander.parse(args);
		} catch (ParameterException e) {
			commander.usage();
			return false;
		}
		return true;
	}

	public Path getLogFilePath() {
		return logFilePath;
	}

	public String getDuration() {
		return duration;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public Integer getThreshold() {
		return Threshold;
	}

}
