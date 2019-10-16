package com.ef.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.ef.model.LogFileModel;
import com.ef.repository.DatabaseConnector;
import com.ef.repository.SqlFile;

public class LogFileService {

	private DatabaseConnector dbConnector;

	public LogFileService(DatabaseConnector dbConnector) {
		this.dbConnector = dbConnector;
	}

	public LogFileModel checkLogFileInDatabase(Path logFilePath) throws IOException, SQLException {
		LogFileModel logFileFS = createNewLogFileModel(logFilePath);
		LogFileModel logFileDB = getLogFileFromDataBase(logFilePath);
		if (logFileDB != null) {
			if (!isLogFileUpdated(logFileFS, logFileDB)) {
				updateLogFileRecord(logFileFS.getLastUpdated(), logFileDB.getId());
				logFileDB.setLastUpdated(logFileFS.getLastUpdated());
				logFileDB.setUpdated(true);
			} else {
				logFileDB.setNoChange(true);
			}
		} else {
			logFileDB = createNewLogFileRecord(logFileFS);
		}
		return logFileDB;
	}

	private LogFileModel createNewLogFileModel(Path logFilePath) throws IOException {
		LogFileModel logFileModel = new LogFileModel();
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

	private LogFileModel getLogFileFromDataBase(Path logFilePath) throws SQLException {
		Object[] params = { logFilePath.toString() };
		dbConnector.createNewPreparedStatement(SqlFile.GET_LOG_FILE, params);
		ResultSet result = dbConnector.executePreparedStatementQuery();
		if (isLogFileAlreadyStored(result)) {
			return createNewLogFileModel(result);
		}
		return null;
	}

	private LogFileModel createNewLogFileModel(ResultSet result) throws SQLException {
		LogFileModel logFileModel = new LogFileModel();
		logFileModel.setId(result.getInt("id"));
		logFileModel.setFilePath(Paths.get(result.getString("path")));
		logFileModel.setName(result.getString("name"));
		logFileModel.setLastUpdated(result.getObject("last_update_time", LocalDateTime.class));
		return logFileModel;
	}

	private boolean isLogFileAlreadyStored(ResultSet result) throws SQLException {
		return result.first();
	}

	private boolean isLogFileUpdated(LogFileModel fileSystemLog, LogFileModel databaseRecord) {
		return fileSystemLog.getLastUpdated().equals(databaseRecord.getLastUpdated());
	}

	private void updateLogFileRecord(LocalDateTime updatedTime, int id) {
		Object[] params = { updatedTime, id };
		dbConnector.createNewPreparedStatement(SqlFile.UPDATE_LOG_FILE, params);
		dbConnector.executePreparedStatementUpdate();
	}

	private LogFileModel createNewLogFileRecord(LogFileModel logFileModel) throws SQLException {
		Object[] params = { logFileModel.getName(), logFileModel.getFilePath().toString(),
				logFileModel.getLastUpdated() };
		dbConnector.createNewPreparedStatement(SqlFile.INSERT_LOG_FILE, params);
		ResultSet result = dbConnector.executePreparedStatementUpdate();
		result.next();
		logFileModel.setId(result.getInt(1));
		return logFileModel;
	}

}
