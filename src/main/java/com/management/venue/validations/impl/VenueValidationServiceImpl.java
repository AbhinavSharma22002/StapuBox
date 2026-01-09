package com.management.venue.validations.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.management.venue.logger.BaseLogger;
import com.management.venue.pojo.ValidationData;
import com.management.venue.pojo.VenueData;
import com.management.venue.services.SportDataService;
import com.management.venue.validations.service.GenericValidationService;

import io.micrometer.common.util.StringUtils;

@Service
public class VenueValidationServiceImpl extends BaseLogger implements GenericValidationService<VenueData> {

	@Autowired
	private SportDataService sportServiceImpl;

	@Override
	public List<ValidationData> validate(VenueData entity) {
		List<ValidationData> exceptions = new ArrayList<>();

		// Determine if this is an Update (PK exists) or Create (No PK)
		boolean isUpdate = StringUtils.isNotEmpty(entity.getPk());

		// 1. Sport Validation (Mandatory for Create, Optional for Update)
		if (StringUtils.isNotEmpty(entity.getSportId())) {
			// Requirement: Sports must be selected from the public API only
			if (!sportServiceImpl.validateSportData(entity.getSportId())) {
				exceptions.add(new ValidationData("sportId", "Sport code is not in the official list"));
			}
		} else if (!isUpdate) {
			// If it's a new Venue, sportId is mandatory
			exceptions.add(new ValidationData("sportId", "Sport code is required for new venues"));
		}

		// 2. Venue Name Validation
		if (StringUtils.isNotEmpty(entity.getVenueName())) {
			// Add any specific name length or character checks here
		} else if (!isUpdate) {
			exceptions.add(new ValidationData("venueName", "Venue Name is required for new venues!"));
		}

		return exceptions;
	}
}