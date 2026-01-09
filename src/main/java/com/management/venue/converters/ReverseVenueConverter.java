package com.management.venue.converters;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.management.venue.converters.services.Converter;
import com.management.venue.entites.Venue;
import com.management.venue.pojo.VenueData;
import com.management.venue.utility.EncryptionService;

import io.micrometer.common.util.StringUtils;

@Service
public class ReverseVenueConverter implements Converter<VenueData, Venue> {

	@Autowired
	protected EncryptionService encryptionService;

	@Override
	public Venue convert(VenueData source, Venue target, Map<String, Object> params) {
		if (null == target) {
			target = new Venue();
		}
		if (StringUtils.isNotBlank(source.getPk())) {
			target.setPk(Long.valueOf(encryptionService.decode(source.getPk())));
		}
		if (StringUtils.isNotBlank(source.getVenueName())) {
			target.setVenueName(source.getVenueName());
		}
		if (StringUtils.isNotBlank(source.getSportId())) {
			target.setSportId(source.getSportId());
		}

		return target;
	}

}
