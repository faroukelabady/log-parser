package com.ef;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.ef.model.LogFileModel;
import com.ef.model.SearchCriteria;
import com.ef.repository.DatabaseConnector;
import com.ef.service.LogDataService;
import com.ef.service.LogFileService;
import com.ef.service.SearchService;
import com.ef.util.ArgumentParser;
import com.ef.util.DbConnectionProperties;

public class Parser {

	private static ArgumentParser arguments;

	public static void main(String[] args) throws SQLException, IOException {
		arguments = new ArgumentParser();
		if (arguments.isParsingSuccess(args)) {
			processLogFile();
		}
	}

	private static void processLogFile() throws SQLException, IOException {

		DatabaseConnector dbConnector = connectToDatabase();
		if(arguments.getLogFilePath() != null) {
			
			if (!isLogFileExistAndReadable(arguments.getLogFilePath())) {
				throw new RuntimeException("Failed to read log file, please check path & accessibility");
			}
			
			LogFileService handler = new LogFileService(dbConnector);
			LogFileModel logFile= handler.checkLogFileInDatabase(arguments.getLogFilePath());
			
			LogDataService logDataHandler = new LogDataService(dbConnector);
			logDataHandler.saveLogData(logFile);
		}
		
		SearchCriteria criteria = new SearchCriteria();
		criteria.buildSearchCriteria(arguments.getStartDate(), arguments.getDuration(), arguments.getThreshold());
		
		SearchService searchService = new SearchService(dbConnector);
		ResultSet results =  searchService.searchLogData(criteria);
		searchService.insertSearchResults(criteria, results);

		dbConnector.closeDatabaseStatementAndConnection();
	}

	private static boolean isLogFileExistAndReadable(Path logPath) {
		return Files.isReadable(logPath);
	}
	
	private static DatabaseConnector connectToDatabase() {
		DatabaseConnector dbConnector = DatabaseConnector.getInstance();
		Properties config = DbConnectionProperties.readProperties();
		dbConnector.connectToDatabase(config.getProperty("jdbc.url"), config.getProperty("jdbc.username"),
				config.getProperty("jdbc.password"));
		return dbConnector;
	}

}
