package com.ef;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Properties;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ef.model.LogFileModel;
import com.ef.repository.DatabaseConnector;
import com.ef.service.LogFileService;
import com.ef.util.DbConnectionProperties;

public class LogFileServiceTest {

	public static DatabaseConnector dbConnector;
	public static Path testLog;

	@BeforeClass
	public static void setup() throws URISyntaxException {
		dbConnector = DatabaseConnector.getInstance();
		Properties config = DbConnectionProperties.readProperties();
		dbConnector.connectToDatabase(config.getProperty("jdbc.url"), config.getProperty("jdbc.username"),
				config.getProperty("jdbc.password"));
		testLog = Paths.get(LogFileServiceTest.class.getResource("/access.log").toURI());
	}

	@Before
	public void init() {
		dbConnector.createNewPreparedStatement("clear_data.sql");
		dbConnector.executePreparedStatementUpdate();
	}

	@Test
	public void SaveNewLogShouldSuccess() throws IOException, SQLException, URISyntaxException {

		LogFileService logFileService = new LogFileService(dbConnector);
		LogFileModel model = logFileService.checkLogFileInDatabase(testLog);

		assertThat(model).isNotNull();
		assertThat(model.isUpdated()).isFalse();
		assertThat(model.isNoChange()).isFalse();

	}

	@Test
	public void UpdateNewLogShouldSetUpdatedFlag() throws IOException, SQLException, URISyntaxException {

		LogFileService logFileService = new LogFileService(dbConnector);
		LogFileModel model = logFileService.checkLogFileInDatabase(testLog);

		FileTime time = FileTime.from(Instant.now());
		Files.setLastModifiedTime(testLog, time);
		model = logFileService.checkLogFileInDatabase(testLog);

		assertThat(model).isNotNull();
		assertThat(model.isUpdated()).isTrue();
		assertThat(model.isNoChange()).isFalse();
	}

	@Test
	public void UpdateSameLogShouldSetNoChangeFlag() throws IOException, SQLException, URISyntaxException {

		LogFileService logFileService = new LogFileService(dbConnector);
		LogFileModel model = logFileService.checkLogFileInDatabase(testLog);

		FileTime time = FileTime.from(Instant.now());
		Files.setLastModifiedTime(testLog, time);
		model = logFileService.checkLogFileInDatabase(testLog);

		assertThat(model).isNotNull();
		assertThat(model.isUpdated()).isFalse();
		assertThat(model.isNoChange()).isTrue();
	}

}
