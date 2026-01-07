package com.management.venue.reconverters;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.management.venue.converters.Converter;
import com.management.venue.entites.TimeSlot;
import com.management.venue.pojo.TimeSlotData;
import com.management.venue.repositories.VenueRepository;
import com.management.venue.utility.EncryptionService;

import io.micrometer.common.util.StringUtils;

@Service
public class ReverseTimeSlotConverter implements Converter<TimeSlotData, TimeSlot> {

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	@Autowired
	protected EncryptionService encryptionService;

	@Autowired
	private VenueRepository venueRepo;

	@Override
	public TimeSlot convert(TimeSlotData source, TimeSlot target, Map<String, Object> params) {
		if (null == target) {
			target = new TimeSlot();
		}
		if (StringUtils.isNotBlank(source.getPk())) {
			target.setPk(Long.valueOf(encryptionService.decode(source.getPk())));
		}
		if (null != source.getVenue() && StringUtils.isNotBlank(source.getVenue().getPk())) {
			target.setVenue(venueRepo.findByParameters(Map.of("pk", encryptionService.decode(source.getVenue().getPk())))
					.stream().findFirst()
					.orElseThrow(() -> new RuntimeException("Venue not found: " + source.getVenue().getPk())));
		}
		if (StringUtils.isNotBlank(source.getStartTime())) {
			target.setStartTime(
					LocalDateTime.parse(source.getStartTime(), formatter)
					);
		}
		if (StringUtils.isNotBlank(source.getEndTime())) {
			target.setEndTime(
					LocalDateTime.parse(source.getEndTime(), formatter)
					);
		}
		
		return target;
	}

}
