package com.management.venue.pojo;

public class BookingData extends PrimaryData {

	private String bookingId;

	private TimeSlotData slot;

	private String status;

	public String getBookingId() {
		return bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	public TimeSlotData getSlot() {
		return slot;
	}

	public void setSlot(TimeSlotData slot) {
		this.slot = slot;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
