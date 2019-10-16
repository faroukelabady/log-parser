package com.ef;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.ef.util.ArgumentParser;

public class ArgumentsTest {

	ArgumentParser parser;

	@Before
	public void setup() {
		parser = new ArgumentParser();
	}

	@Test
	public void argumentsBuildShouldSuccessfully() {
		String[] arguments = { "--accesslog=/home/onecard/access.log", "--startDate=2017-01-01.00:00:00",
				"--duration=daily", "--threshold=500" };
		assertThat(parser.isParsingSuccess(arguments)).isTrue();
	}

	@Test
	public void argumentsBuildShouldFailWhenMissingStartDate() {
		String[] arguments = { "--accesslog=/home/onecard/access.log",
				"--duration=daily", "--threshold=500"};
		assertThat(parser.isParsingSuccess(arguments)).isFalse();
	}
	
	@Test
	public void argumentsBuildShouldFailWhenMissingDuration() {
		String[] arguments = { "--accesslog=/home/onecard/access.log", "--startDate=2017-01-01.00:00:00",
				 "--threshold=500"};
		assertThat(parser.isParsingSuccess(arguments)).isFalse();
	}
	
	
	@Test
	public void argumentsBuildShouldSuccessWhenMissingThreshold() {
		String[] arguments = { "--accesslog=/home/onecard/access.log", "--startDate=2017-01-01.00:00:00",
				"--duration=daily"};
		assertThat(parser.isParsingSuccess(arguments)).isTrue();
	}
	
	@Test
	public void argumentsBuildShouldFailWithInvalidStartDate() {
		String[] arguments = { "--accesslog=/home/onecard/access.log", "--startDate=2017-01 01 00:00:00",
				"--duration=daily", "--threshold=500" };
		assertThat(parser.isParsingSuccess(arguments)).isFalse();
	}
	
	@Test
	public void argumentsBuildShouldFailWithInvalidDuration() {
		String[] arguments = { "--accesslog=/home/onecard/access.log", "--startDate=2017-01-01.00:00:00",
				"--duration=yearly", "--threshold=500" };
		assertThat(parser.isParsingSuccess(arguments)).isFalse();
	}
	
	@Test
	public void argumentsBuildShouldFailWithInvalidThreshold() {
		String[] arguments = { "--accesslog=/home/onecard/access.log", "--startDate=2017-01-01.00:00:00",
				"--duration=yearly", "--threshold=-500" };
		assertThat(parser.isParsingSuccess(arguments)).isFalse();
	}
	
	@Test
	public void argumentsBuildShouldFailWithWrongSeparator() {
		String[] arguments = { "--accesslog /home/onecard/access.log", "--startDate 2017-01-01.00:00:00",
				"--duration yearly", "--threshold -500" };
		assertThat(parser.isParsingSuccess(arguments)).isFalse();
	}

}
