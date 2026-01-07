package com.management.venue.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.management.venue.converters.Converter;
import com.management.venue.entites.TimeSlot;
import com.management.venue.exceptions.StapuBoxException;
import com.management.venue.pojo.TimeSlotData;
import com.management.venue.repositories.TimeSlotRepository;
import com.management.venue.utility.EncryptionService;

@Service
public class TimeSlotService {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TimeSlotService.class);

	@Autowired
	private TimeSlotRepository slotRepository; // Using your Generic Pattern

	@Autowired
	private EncryptionService encryptionService;

	@Autowired
	private Converter<TimeSlot, TimeSlotData> timeSlotDataConverter;

	@Autowired
	private Converter<TimeSlotData, TimeSlot> reverseTimeSlotDataConverter;

	public TimeSlotData upsertVenueSlot(String venuePkStr, TimeSlotData slotData) {

		try {
			Long venuePk = Long.valueOf(encryptionService.decode(venuePkStr));
			TimeSlot slot = reverseTimeSlotDataConverter.convert(slotData, null, null);

			boolean isOverlapping = slotRepository.existsOverlap(venuePk, slot.getStartTime(), slot.getEndTime());

			if (isOverlapping) {
				log.warn("Slot overlap rejected for Venue ID: {}", venuePk);
				throw new StapuBoxException("Overlap detected: A slot already exists in this time range.", "409", null);
			}
			return timeSlotDataConverter.convert(slotRepository.save(slot), slotData, null);
		} catch (StapuBoxException e) {
			// log the error
			log.error("Error upserting slot: {}", e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			// log the error
			log.error("Error upserting slot: {}", e.getMessage(), e);
			throw new StapuBoxException("Exception occured while upserting slot", "500", e);
		}
	}

}