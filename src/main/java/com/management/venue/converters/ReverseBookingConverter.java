package com.management.venue.converters;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.management.venue.converters.services.Converter;
import com.management.venue.entites.Booking;
import com.management.venue.pojo.BookingData;
import com.management.venue.repositories.TimeSlotRepository;
import com.management.venue.utility.EncryptionService;

import io.micrometer.common.util.StringUtils;

@Service
public class ReverseBookingConverter implements Converter<BookingData, Booking> {

	@Autowired
	protected EncryptionService encryptionService;

	@Autowired
	private TimeSlotRepository slotRepo;

	@Override
	public Booking convert(BookingData source, Booking target, Map<String, Object> params) {
		if (null == target) {
			target = new Booking();
		}
		if (StringUtils.isNotBlank(source.getPk())) {
			target.setPk(Long.valueOf(encryptionService.decode(source.getPk())));
		}
		if (StringUtils.isNotBlank(source.getBookingId())) {
			target.setBookingId(source.getBookingId());
		}
		if (null != source.getSlot() && StringUtils.isNotBlank(source.getSlot().getPk())) {
			target.setSlot(slotRepo.findByParameters(Map.of("pk", encryptionService.decode(source.getSlot().getPk())))
					.stream().findFirst()
					.orElseThrow(() -> new RuntimeException("Time Slot not found: " + source.getSlot().getPk())));
		}
		return target;
	}

}
