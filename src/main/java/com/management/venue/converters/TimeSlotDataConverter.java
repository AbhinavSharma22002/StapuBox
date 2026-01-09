package com.management.venue.converters;

import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.management.venue.converters.services.Converter;
import com.management.venue.entites.TimeSlot;
import com.management.venue.entites.Venue;
import com.management.venue.pojo.TimeSlotData;
import com.management.venue.pojo.VenueData;

@Service
public class TimeSlotDataConverter extends PrimaryDataConverter implements Converter<TimeSlot,TimeSlotData>  {
	
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private Converter<Venue,VenueData> venueDataConverter;
	
	@Override
	public TimeSlotData convert(TimeSlot source, TimeSlotData target, Map<String, Object> params) {
		if(null==target) {
			target = new TimeSlotData();
		}
		convertPrimaryFields(source,target);
		if(null!=source.getStartTime()) {
			target.setStartTime(source.getStartTime().format(formatter));
		}
		if(null!=source.getEndTime()) {
			target.setEndTime(source.getEndTime().format(formatter));
		}
		if(null!=source.getVenue()) {
			target.setVenue(venueDataConverter.convert(source.getVenue(), null, params));
		}
		target.setAvailable(source.isAvailable());
		
		return target;
	}

	
}
