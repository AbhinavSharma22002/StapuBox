package com.management.venue.services;

import com.management.venue.pojo.BookingData;
import com.management.venue.pojo.TimeSlotData;

public interface BookingService {
	
	public BookingData bookSlot(TimeSlotData slotData);

	public BookingData getBookingByPk(String bookingPkStr);

	public void cancelBooking(String bookingPkStr);

}