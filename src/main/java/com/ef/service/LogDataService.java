package com.ef.service;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

import com.ef.model.LineDataModel;
import com.ef.model.LogFileModel;
import com.ef.repository.DatabaseConnector;
import com.ef.repository.SqlFile;

public class LogDataService {

	DatabaseConnector dbConnector;

	public LogDataService(DatabaseConnector dbConnector) {
		this.dbConnector = dbConnector;
	}

	public void saveLogData(LogFileModel logFile) throws SQLException {
		Stream<String> lines = getLogFileLines(logFile);
		if(logFile.isNoChange()) {
			lines.close();
			return;
		}
		if(logFile.isUpdated()) {
			int lastSavedLineNumber = findLastLineNumberSaved(logFile);
			lines = lines.skip(lastSavedLineNumber);
		}
		int[] lineNumber = { 1 }; // little hack to track the line count
		createInsertLogDataBatchScript();
		lines.map(line -> new LineDataModel(line))
				.forEach(lineData -> {
					createNewLineBatch(logFile, lineNumber[0], lineData);
					lineNumber[0]++;
				});
		lines.close();
		saveBatch();
	}

	private Stream<String> getLogFileLines(LogFileModel logFile) {
		try {
			return Files.lines(logFile.getFilePath());
		} catch (IOException e) {
			throw new RuntimeException("error opening file", e);
		}
	}
	
	public int findLastLineNumberSaved(LogFileModel logFile) throws SQLException {
		dbConnector.createNewPreparedStatement(SqlFile.GET_MAX_LINE_NUMBER);
		dbConnector.setPreparedStatementValues(logFile.getId());
		ResultSet results = dbConnector.executePreparedStatementQuery();
		results.next();
		return results.getInt(1);
	}

	private void createInsertLogDataBatchScript() {
		dbConnector.createNewPreparedStatement(SqlFile.INSERT_LOG_DATA);
		dbConnector.disableAutoCommit();
	}

	private void createNewLineBatch(LogFileModel logFile, int lineNumber, LineDataModel lineData) {
		Object[] params = { lineNumber, logFile.getId(), lineData.getDateTime(), lineData.getIp(),
				lineData.getRequest(), lineData.getStatus(), lineData.getUserAgent() };
		dbConnector.setPreparedStatementValues(params);
		dbConnector.addToBatch();
	}
	
	private void saveBatch() {
		dbConnector.excuteBatch();
		dbConnector.commit();
	}
	

}
