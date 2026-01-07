package com.management.venue.converters;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.management.venue.entites.Booking;
import com.management.venue.entites.TimeSlot;
import com.management.venue.pojo.BookingData;
import com.management.venue.pojo.TimeSlotData;

import io.micrometer.common.util.StringUtils;

@Service
public class BookingDataConverter extends PrimaryDataConverter implements Converter<Booking,BookingData>  {
	
	@Autowired
	private Converter<TimeSlot,TimeSlotData> timeSlotDataConverter;
	
	@Override
	public BookingData convert(Booking source, BookingData data, Map<String, Object> params) {
		if(null==data) {
			data = new BookingData();
		}
		convertPrimaryFields(source,data);
		if(StringUtils.isNotBlank(source.getBookingId())) {
			data.setBookingId(source.getBookingId());
		}
		if(null!=source.getSlot()) {
			data.setSlot(timeSlotDataConverter.convert(source.getSlot(), null, params));
		}
		if(null!=source.getStatus()) {
			data.setStatus(source.getStatus().name());
		}
		
		return data;
	}

	
}
