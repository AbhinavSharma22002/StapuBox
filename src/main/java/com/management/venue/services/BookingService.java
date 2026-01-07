package com.management.venue.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.management.venue.converters.Converter;
import com.management.venue.entites.Booking;
import com.management.venue.entites.TimeSlot;
import com.management.venue.enums.BookingStatus;
import com.management.venue.exceptions.StapuBoxException;
import com.management.venue.pojo.BookingData;
import com.management.venue.pojo.TimeSlotData;
import com.management.venue.repositories.GenericRepository;
import com.management.venue.utility.EncryptionService;

import jakarta.transaction.Transactional;

@Service
public class BookingService {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(VenueService.class);

	@Autowired
	private GenericRepository<Booking> bookingRepository;

	@Autowired
	private GenericRepository<TimeSlot> slotRepository;

	@Autowired
	private EncryptionService encryptionService;

	@Autowired
	private Converter<Booking, BookingData> bookingDataConverter;

	// 1. Book a Slot
	@Transactional
	public BookingData bookSlot(TimeSlotData slotData) {
		try {
			// Decode the Slot PK from the request
			TimeSlot slot = slotRepository
					.findByParameters(Map.of("pk", Long.valueOf(encryptionService.decode(slotData.getPk()))))
					.getFirst();

			if (slot == null)
				throw new StapuBoxException("Slot not found", "404", null);

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
		} catch (StapuBoxException e) {
			// log the error
			log.error("Error while booking a slot: {}", e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			// log the error
			log.error("Error while booking a slot: {}", e.getMessage(), e);
			throw new StapuBoxException("Exception occured while booking a slot", "500", e);
		}

	}

	// 2. View a Booking
	public BookingData getBookingByPk(String bookingPkStr) {
		try {
			Booking booking = bookingRepository
					.findByParameters(Map.of("pk", Long.valueOf(encryptionService.decode(bookingPkStr)))).getFirst();

			if (booking == null)
				throw new StapuBoxException("Booking not found", "404", null);
			// Return the encrypted PK of the Booking record
			return bookingDataConverter.convert(booking, null, null);
		} catch (StapuBoxException e) {
			// log the error
			log.error("Error while fetching a booking: {}", e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			// log the error
			log.error("Error while fetching a booking: {}", e.getMessage(), e);
			throw new StapuBoxException("Exception occured while fetching a booking", "500", e);
		}
	}

	// 3. Cancel a Booking
	@Transactional
	public void cancelBooking(String bookingPkStr) {

		try {
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
		} catch (StapuBoxException e) {
			// log the error
			log.error("Error while fetching a booking: {}", e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			// log the error
			log.error("Error while fetching a booking: {}", e.getMessage(), e);
			throw new StapuBoxException("Exception occured while fetching a booking", "500", e);
		}

	}

}