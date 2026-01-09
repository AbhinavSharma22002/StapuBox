package com.management.venue.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.management.venue.converters.services.Converter;
import com.management.venue.entites.TimeSlot;
import com.management.venue.exceptions.StapuBoxException;
import com.management.venue.logger.BaseLogger;
import com.management.venue.pojo.TimeSlotData;
import com.management.venue.repositories.TimeSlotRepository;
import com.management.venue.services.TimeSlotService;
import com.management.venue.utility.EncryptionService;

@Service
public class TimeSlotServiceImpl extends BaseLogger implements TimeSlotService {

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
		TimeSlot slot = reverseTimeSlotDataConverter.convert(slotData, null, null);

		boolean isOverlapping = slotRepository.existsOverlap(venuePk, slot.getStartTime(), slot.getEndTime());

		if (isOverlapping) {
			log.warn("Slot overlap rejected for Venue ID: {}", venuePk);
			throw new StapuBoxException("Overlap detected: A slot already exists in this time range.", "409", null);
		}
		return timeSlotDataConverter.convert(slotRepository.save(slot), slotData, null);
	}

}