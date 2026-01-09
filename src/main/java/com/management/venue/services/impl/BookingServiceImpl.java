package com.management.venue.services.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.management.venue.converters.services.Converter;
import com.management.venue.entites.Booking;
import com.management.venue.entites.TimeSlot;
import com.management.venue.enums.BookingStatus;
import com.management.venue.exceptions.StapuBoxException;
import com.management.venue.logger.BaseLogger;
import com.management.venue.pojo.BookingData;
import com.management.venue.pojo.TimeSlotData;
import com.management.venue.repositories.service.GenericDaoService;
import com.management.venue.services.BookingService;
import com.management.venue.utility.EncryptionService;

import jakarta.transaction.Transactional;

@Service
public class BookingServiceImpl extends BaseLogger implements BookingService {

	@Autowired
	private GenericDaoService<Booking> bookingRepository;

	@Autowired
	private GenericDaoService<TimeSlot> slotRepository;

	@Autowired
	private EncryptionService encryptionService;

	@Autowired
	private Converter<Booking, BookingData> bookingDataConverter;

	// 1. Book a Slot
	@Override
	@Transactional
	public BookingData bookSlot(TimeSlotData slotData) {
		// Decode the Slot PK from the request
		TimeSlot slot = slotRepository
				.findByParameters(Map.of("pk", Long.valueOf(encryptionService.decode(slotData.getPk())))).getFirst();

		if (slot == null)
			throw new StapuBoxException("Slot not found", "404", null);
		
		// Check Availability (Prevents double booking at the logic level)
	    if (!slot.isAvailable()) {
	        log.warn("Booking Attempt Failed: Slot {} is already booked.", slot.getPk());
	        throw new StapuBoxException("This slot has already been booked by another user.", "409", null);
	    }

		// 1. Update Slot Availability
		slot.setAvailable(false);
		slotRepository.save(slot);

		// 2. Create New Booking
		Booking booking = new Booking();
		// Generate a readable booking reference (e.g., BK-12345)
		booking.setBookingId("BK-" + System.currentTimeMillis());
		booking.setSlot(slot);
		booking.setStatus(BookingStatus.CREATED);

		// Return the encrypted PK of the Booking record
		return bookingDataConverter.convert(bookingRepository.save(booking), null, null);

	}

	// 2. View a Booking
	@Override
	public BookingData getBookingByPk(String bookingPkStr) {
		Booking booking = bookingRepository
				.findByParameters(Map.of("pk", Long.valueOf(encryptionService.decode(bookingPkStr)))).getFirst();

		if (booking == null)
			throw new StapuBoxException("Booking not found", "404", null);
		// Return the encrypted PK of the Booking record
		return bookingDataConverter.convert(booking, null, null);

	}

	// 3. Cancel a Booking
	@Override
	@Transactional
	public void cancelBooking(String bookingPkStr) {

		Booking booking = bookingRepository
				.findByParameters(Map.of("pk", Long.valueOf(encryptionService.decode(bookingPkStr)))).getFirst();

		if (booking == null)
			throw new StapuBoxException("Booking not found", "404", null);

		// 1. Mark booking as CANCELLED
		booking.setStatus(BookingStatus.CANCELLED);
		bookingRepository.save(booking);

		// 2. Make the slot available again
		TimeSlot slot = booking.getSlot();
		slot.setAvailable(true);
		slotRepository.save(slot);

	}

}