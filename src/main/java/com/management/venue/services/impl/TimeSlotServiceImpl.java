package com.management.venue.services.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.management.venue.converters.services.Converter;
import com.management.venue.entites.TimeSlot;
import com.management.venue.exceptions.StapuBoxException;
import com.management.venue.logger.BaseLogger;
import com.management.venue.pojo.TimeSlotData;
import com.management.venue.pojo.VenueData;
import com.management.venue.repositories.TimeSlotRepository;
import com.management.venue.services.TimeSlotService;
import com.management.venue.utility.EncryptionService;

import io.micrometer.common.util.StringUtils;

@Service
public class TimeSlotServiceImpl extends BaseLogger implements TimeSlotService {

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private TimeSlotRepository slotRepository;

	@Autowired
	private EncryptionService encryptionService;

	@Autowired
	private Converter<TimeSlot, TimeSlotData> timeSlotDataConverter;

	@Autowired
	private Converter<TimeSlotData, TimeSlot> reverseTimeSlotDataConverter;

	@Override
	public TimeSlotData upsertVenueSlot(String venuePkStr, TimeSlotData slotData) {

		Long venuePk = Long.valueOf(encryptionService.decode(venuePkStr));
		VenueData vd = new VenueData();
		vd.setPk(venuePkStr);
		slotData.setVenue(vd);
		TimeSlot slot = reverseTimeSlotDataConverter.convert(slotData, null, null);

		boolean isOverlapping = slotRepository.existsOverlap(venuePk, slot.getStartTime(), slot.getEndTime());

		if (isOverlapping) {
			log.warn("Slot overlap rejected for Venue ID: {}", venuePk);
			throw new StapuBoxException("Overlap detected: A slot already exists in this time range.", "409", null);
		}
		return timeSlotDataConverter.convert(slotRepository.save(slot), slotData, null);
	}
	

	@Override
	public List<TimeSlotData> getAvailableVenues(TimeSlotData criteria) {
	    // 1. Extract Sport ID optionally
	    String sportId = (criteria.getVenue() != null) ? criteria.getVenue().getSportId() : null;

	    // 2. Parse Dates safely (Helper method to return null if string is empty)
	    LocalDateTime start = parseDateTimeSafely(criteria.getStartTime());
	    LocalDateTime end = parseDateTimeSafely(criteria.getEndTime());

	    // 3. Call the DAO with optional parameters
	    return slotRepository.findAvailableVenues(sportId, start, end)
	            .stream()
	            .map(v -> timeSlotDataConverter.convert(v, null, null))
	            .toList();
	}
	
	/**
	 * Safely parses a date string into a LocalDateTime object.
	 * Returns null if the string is empty, null, or malformed.
	 * This allows the DAO to treat the filter as optional.
	 */
	private LocalDateTime parseDateTimeSafely(String dateStr) {
	    // 1. Handle empty or null strings immediately
	    if (StringUtils.isEmpty(dateStr)) {
	        return null;
	    }

	    try {
	        // 2. Attempt parsing using the standard format
	        // Ensure 'formatter' is defined as: 
	        // DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
	        return LocalDateTime.parse(dateStr, formatter);
	        
	    } catch (java.time.format.DateTimeParseException e) {
	        log.warn("Invalid date format received: [{}]. Expected format: yyyy-MM-dd HH:mm:ss. Filter will be ignored.", dateStr);
	        return null;
	    }
	}

}