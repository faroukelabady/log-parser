package com.ef;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Properties;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ef.model.LogFileModel;
import com.ef.repository.DatabaseConnector;
import com.ef.service.LogDataService;
import com.ef.util.DbConnectionProperties;

public class LogDataServiceTest {

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
	public void SaveNewLogDataLineShouldSuccess() throws IOException, SQLException, URISyntaxException {
		LogFileModel logFile = createNewLogFileModel(testLog);
		LogDataService logDataHandler = new LogDataService(dbConnector);
		logDataHandler.saveLogData(logFile);
	}

	private LogFileModel createNewLogFileModel(Path logFilePath) throws IOException {
		LogFileModel logFileModel = new LogFileModel();
		logFileModel.setId(1);
		logFileModel.setFilePath(logFilePath);
		logFileModel.setName(logFilePath.getFileName().toString());
		logFileModel.setLastUpdated(retrieveFileModifiedTime(logFilePath));
		return logFileModel;
	}

	private LocalDateTime retrieveFileModifiedTime(Path logFilePath) throws IOException {
		FileTime fileTime = Files.getLastModifiedTime(logFilePath);
		LocalDateTime current = LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault());
		return current;
	}

}
