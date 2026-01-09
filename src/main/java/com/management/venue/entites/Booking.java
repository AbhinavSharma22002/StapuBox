package com.management.venue.entites;

import com.management.venue.enums.BookingStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "bookings", uniqueConstraints = {
	    @UniqueConstraint(columnNames = {"slot_id"})
	})
public class Booking extends Primary {

	@Column(unique = true, nullable = false)
	private String bookingId;

	@ManyToOne 
	@JoinColumn(name = "slot_id", nullable = false)
	private TimeSlot slot;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private BookingStatus status;

	public String getBookingId() {
		return bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	public TimeSlot getSlot() {
		return slot;
	}

	public void setSlot(TimeSlot slot) {
		this.slot = slot;
	}

	public BookingStatus getStatus() {
		return status;
	}

	public void setStatus(BookingStatus status) {
		this.status = status;
	}

}
