package com.ef.model;

import java.nio.file.Path;
import java.time.LocalDateTime;

public class LogFileModel {
	
	private int id;
	
	private String name;
	
	private Path filePath;
	
	private LocalDateTime lastUpdated;
	
	private boolean updated;
	
	private boolean noChange;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Path getFilePath() {
		return filePath;
	}

	public void setFilePath(Path filePath) {
		this.filePath = filePath;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}
	
	public boolean isNoChange() {
		return noChange;
	}

	public void setNoChange(boolean noChange) {
		this.noChange = noChange;
	}
	
}
