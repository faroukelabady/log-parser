package com.ef.model;

import java.time.LocalDateTime;

public class SearchCriteria {

	private LocalDateTime startDateTime;

	private LocalDateTime endDateTime;

	private String duration;

	private Integer threhold;

	public SearchCriteria() {
	}

	public void buildSearchCriteria(LocalDateTime startDateTime, String duration, Integer threhold) {
		this.startDateTime = startDateTime;
		this.duration = duration;
		this.threhold = threhold;
		calculateEndDateTime();

	}

	private void calculateEndDateTime() {
		if (duration.equalsIgnoreCase(DURATION_TIME.HOURLY.toString())) {
			endDateTime = startDateTime.plusHours(1);
			setDefaultThresholdIfNoneSupplied(200);
		} else if (duration.equalsIgnoreCase(DURATION_TIME.DAILY.toString())) {
			endDateTime = startDateTime.plusDays(1);
			setDefaultThresholdIfNoneSupplied(500);
			
		} 
	}

	private void setDefaultThresholdIfNoneSupplied(int defaultThreshold) {
		if (threhold == null) {
			threhold = defaultThreshold;
		}
	}

	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}

	public String getDuration() {
		return duration;
	}

	public int getThrehold() {
		return threhold;
	}

}
