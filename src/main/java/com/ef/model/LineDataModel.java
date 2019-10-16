package com.ef.model;

import java.time.LocalDateTime;

import com.ef.util.Utilities;

public class LineDataModel {
	
	private LocalDateTime dateTime;
	
	private String ip;
	
	private String request;
	
	private String status;
	
	private String userAgent;
	
	public LineDataModel(String line) {
		String[] data = line.split("\\|");
		this.dateTime = Utilities.StringToLocalDateTime(data[0], "yyyy-MM-dd HH:mm:ss.SSS");
		this.ip = data[1];
		this.request = Utilities.trimDoubleQuotation(data[2]); 
		this.status = data[3];
		this.userAgent = Utilities.trimDoubleQuotation(data[4]);
	}
	
	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public String getIp() {
		return ip;
	}

	public String getRequest() {
		return request;
	}

	public String getStatus() {
		return status;
	}

	public String getUserAgent() {
		return userAgent;
	}

}
