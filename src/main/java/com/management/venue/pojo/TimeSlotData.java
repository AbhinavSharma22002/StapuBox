package com.management.venue.pojo;

public class TimeSlotData extends PrimaryData {

	private VenueData venue;

	private String startTime;
	
	private String endTime;
	
	private Boolean available;

	public VenueData getVenue() {
		return venue;
	}

	public void setVenue(VenueData venue) {
		this.venue = venue;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Boolean isAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}
	
	
}
