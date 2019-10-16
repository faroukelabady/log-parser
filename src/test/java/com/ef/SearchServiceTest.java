package com.ef;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Properties;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ef.model.LogFileModel;
import com.ef.model.SearchCriteria;
import com.ef.repository.DatabaseConnector;
import com.ef.service.LogDataService;
import com.ef.service.SearchService;
import com.ef.util.DbConnectionProperties;

public class SearchServiceTest {

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
	public void SearchLogDataLineShouldSuccess() throws IOException, SQLException, URISyntaxException {
		LogFileModel logFile = createNewLogFileModel(testLog);
		LogDataService logDataHandler = new LogDataService(dbConnector);
		logDataHandler.saveLogData(logFile);

		SearchCriteria criteria = new SearchCriteria();
		LocalDateTime startDate = LocalDateTime.of(2017, 01, 01, 00, 00);
		criteria.buildSearchCriteria(startDate, "daily", 1);

		SearchService searchService = new SearchService(dbConnector);
		ResultSet results = searchService.searchLogData(criteria);
		assertThat(results.first()).isTrue();

		results.last();
		int size = results.getRow();
		assertThat(size).isEqualTo(3);
	}

	@Test
	public void SaveSearchLogDataLineShouldSuccess() throws IOException, SQLException, URISyntaxException {

		LogFileModel logFile = createNewLogFileModel(testLog);
		LogDataService logDataHandler = new LogDataService(dbConnector);
		logDataHandler.saveLogData(logFile);

		SearchCriteria criteria = new SearchCriteria();
		LocalDateTime startDate = LocalDateTime.of(2017, 01, 01, 00, 00);
		criteria.buildSearchCriteria(startDate, "daily", 1);

		SearchService searchService = new SearchService(dbConnector);
		ResultSet results = searchService.searchLogData(criteria);
		searchService.insertSearchResults(criteria, results);

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
