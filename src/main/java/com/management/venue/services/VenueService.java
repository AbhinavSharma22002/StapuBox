package com.management.venue.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.management.venue.converters.Converter;
import com.management.venue.entites.Venue;
import com.management.venue.exceptions.StapuBoxException;
import com.management.venue.pojo.TimeSlotData;
import com.management.venue.pojo.VenueData;
import com.management.venue.repositories.VenueRepository;
import com.management.venue.utility.EncryptionService;

import jakarta.transaction.Transactional;

@Service
public class VenueService {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(VenueService.class);
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	@Autowired
	private VenueRepository venueRepo;

	@Autowired
	private EncryptionService encryptionService;

	@Autowired
	private Converter<Venue, VenueData> venueDataConverter;

	@Autowired
	private Converter<VenueData, Venue> reverseVenueDataConverter;

	@Transactional
	public VenueData upsertVenue(VenueData venueData) {
		try {
			return venueDataConverter.convert(venueRepo.save(reverseVenueDataConverter.convert(venueData, null, null)),
					venueData, null);
		} catch (StapuBoxException e) {
			// log the error
			log.error("Error upserting Venue: {}", e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			// log the error
			log.error("Error upserting Venue: {}", e.getMessage(), e);
			throw new StapuBoxException("Exception occured while upserting Venue", "500", e);
		}
	}

	public List<VenueData> getAllVenues() {
		try {
			return venueRepo.findByParameters(CollectionUtils.newHashMap(0)).stream()
					.map(v -> venueDataConverter.convert(v, null, null)).toList();

		} catch (StapuBoxException e) {
			// log the error
			log.error("Error fetching venues: {}", e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			// log the error
			log.error("Error fetching venues: {}", e.getMessage(), e);
			throw new StapuBoxException("Exception occured while fetching Venues", "500", e);
		}
	}

	public VenueData getVenueByPk(String pk) {
		try {
			return venueDataConverter.convert(venueRepo.findByParameters(Map.of("pk", encryptionService.decode(pk)))
					.stream().findFirst().orElseThrow(() -> new RuntimeException("Venue not found: " + pk)), null,
					null);
		} catch (StapuBoxException e) {
			// log the error
			log.error("Error fetching Venue with pk {}: {}", pk, e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			// log the error
			log.error("Error fetching Venue with pk {}: {}", pk, e.getMessage(), e);
			throw new StapuBoxException("Exception occured while fetching Venue with pk " + pk, "500", e);
		}
	}

	@Transactional
	public void removeVenue(String pk) {
		try {
			venueRepo.delete(Long.valueOf(encryptionService.decode(pk)));
		} catch (StapuBoxException e) {
			// log the error
			log.error("Error deleting Venue: {}", e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			// log the error
			log.error("Error deleting Venue: {}", e.getMessage(), e);
			throw new StapuBoxException("Exception occured while deleting Venue with pk " + pk, "500", e);
		}
	}

	public List<VenueData> getAvailableVenues(TimeSlotData slotData) {
		try {
			return venueRepo
					.findAvailableVenues(slotData.getVenue().getSportId(),
							LocalDateTime.parse(slotData.getStartTime(), formatter),
							LocalDateTime.parse(slotData.getStartTime(), formatter))
					.stream().map(v -> venueDataConverter.convert(v, null, null)).toList();

		} catch (StapuBoxException e) {
			// log the error
			log.error("Error fetching available venues: {}", e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			// log the error
			log.error("Error fetching available venues: {}", e.getMessage(), e);
			throw new StapuBoxException("Exception occured while fetching available Venues", "500", e);
		}
	}
}