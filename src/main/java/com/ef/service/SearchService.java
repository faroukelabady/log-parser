package com.ef.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import com.ef.model.SearchCriteria;
import com.ef.repository.DatabaseConnector;
import com.ef.repository.SqlFile;

public class SearchService {
	
	private DatabaseConnector dbConnector;

	public SearchService(DatabaseConnector dbConnector) {
		this.dbConnector = dbConnector;
	}
	
	
	public ResultSet searchLogData(SearchCriteria criteria) {
		dbConnector.createNewPreparedStatement(SqlFile.SEARCH_LOG_DATA, criteria.getStartDateTime(),
				criteria.getEndDateTime(), criteria.getThrehold());
		return dbConnector.executePreparedStatementQuery();
	}
	
	public void insertSearchResults(SearchCriteria criteria, ResultSet rs) throws SQLException {
		dbConnector.disableAutoCommit();
		dbConnector.createNewPreparedStatement(SqlFile.INSERT_SEARCH_RESULTS);
		LocalDateTime now = LocalDateTime.now();
		while (rs.next()) {
			System.out.println(rs.getString("ip"));
			dbConnector.setPreparedStatementValue(1, criteria.getStartDateTime());
			dbConnector.setPreparedStatementValue(2, criteria.getEndDateTime());
			dbConnector.setPreparedStatementValue(3, criteria.getDuration());
			dbConnector.setPreparedStatementValue(4, rs.getString("ip"));
			dbConnector.setPreparedStatementValue(5, rs.getString("ip_count"));
			dbConnector.setPreparedStatementValue(6, criteria.getThrehold());
			dbConnector.setPreparedStatementValue(7, now);
			dbConnector.setPreparedStatementValue(8, "Too many connections");
			dbConnector.addToBatch();
		}
		dbConnector.excuteBatch();
		dbConnector.commit();
	}
	

}
