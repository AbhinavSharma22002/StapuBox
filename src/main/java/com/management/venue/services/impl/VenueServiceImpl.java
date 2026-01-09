package com.management.venue.services.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.management.venue.converters.services.Converter;
import com.management.venue.entites.Venue;
import com.management.venue.logger.BaseLogger;
import com.management.venue.pojo.VenueData;
import com.management.venue.repositories.VenueRepository;
import com.management.venue.services.VenueService;
import com.management.venue.utility.EncryptionService;

import jakarta.transaction.Transactional;

@Service
public class VenueServiceImpl extends BaseLogger implements VenueService {

	@Autowired
	private VenueRepository venueRepo;

	@Autowired
	private EncryptionService encryptionService;

	@Autowired
	private Converter<Venue, VenueData> venueDataConverter;

	@Autowired
	private Converter<VenueData, Venue> reverseVenueDataConverter;

	@Override
	@Transactional
	public VenueData upsertVenue(VenueData venueData) {	
		return venueDataConverter.convert(venueRepo.save(reverseVenueDataConverter.convert(venueData, null, null)),
				venueData, null);
	}

	@Override
	public List<VenueData> getAllVenues() {
		return venueRepo.findByParameters(CollectionUtils.newHashMap(0)).stream()
				.map(v -> venueDataConverter.convert(v, null, null)).toList();

	}

	@Override
	public VenueData getVenueByPk(String pk) {
		return venueDataConverter.convert(venueRepo.findByParameters(Map.of("pk", encryptionService.decode(pk)))
				.stream().findFirst().orElseThrow(() -> new RuntimeException("Venue not found: " + pk)), null, null);
	}
	
	@Override
	@Transactional
	public void removeVenue(String pk) {
		venueRepo.delete(Long.valueOf(encryptionService.decode(pk)));
	}
}