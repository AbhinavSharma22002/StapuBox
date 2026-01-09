package com.management.venue.converters;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.management.venue.converters.services.Converter;
import com.management.venue.entites.Venue;
import com.management.venue.pojo.VenueData;

import io.micrometer.common.util.StringUtils;

@Service
public class VenueDataConverter extends PrimaryDataConverter implements Converter<Venue,VenueData>  {
	
	@Override
	public VenueData convert(Venue source, VenueData target, Map<String, Object> params) {
		if(null==target) {
			target = new VenueData();
		}
		convertPrimaryFields(source,target);
		
		if(StringUtils.isNotBlank(source.getVenueName())) {
			target.setVenueName(source.getVenueName());
		}
		if(StringUtils.isNotBlank(source.getSportId())) {
			target.setSportId(source.getSportId());
		}
		
		return target;
	}

	
}
