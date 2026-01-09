package com.management.venue.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.venue.logger.BaseLogger;
import com.management.venue.pojo.ResponseData;
import com.management.venue.pojo.TimeSlotData;
import com.management.venue.pojo.ValidationData;
import com.management.venue.pojo.VenueData;
import com.management.venue.services.TimeSlotService;
import com.management.venue.services.VenueService;
import com.management.venue.validations.service.GenericValidationService;

@RestController
@RequestMapping("/api/venues")
public class VenueController extends BaseLogger {

	@Autowired
	private VenueService venueService;

	@Autowired
	private GenericValidationService<VenueData> venueValidationService;

	@Autowired
	private GenericValidationService<TimeSlotData> timeSlotValidationService;

	@Autowired
	private TimeSlotService slotService;

	@PostMapping("/upsert-venue")
	public ResponseEntity<ResponseData<?>> upsertVenue(@RequestBody VenueData venueData) {
		List<ValidationData> exceptions = venueValidationService.validate(venueData);
		if (!CollectionUtils.isEmpty(exceptions)) {
			return ResponseEntity.ok(new ResponseData<>(exceptions, "Validation Failed!", "400"));
		}
		return ResponseEntity.ok(new ResponseData<>(venueService.upsertVenue(venueData), "Success", "200"));
	}

	@GetMapping
	public ResponseEntity<ResponseData<?>> listVenues() {
		// list all the venues
		return ResponseEntity.ok(new ResponseData<>(venueService.getAllVenues(), "Success", "200"));
	}

	@PostMapping
	public ResponseEntity<ResponseData<?>> getVenue(@RequestBody Map<String, Object> body) {
		// list a specific venue
		if (!body.containsKey("pk")) {
			return ResponseEntity.ok(new ResponseData<>(null, "Failed to find Venue with provided Id.", "400"));
		}
		return ResponseEntity
				.ok(new ResponseData<>(venueService.getVenueByPk(body.get("pk").toString()), "Success", "200"));
	}

	@PostMapping("/delete-venue")
	public ResponseEntity<ResponseData<?>> deleteVenue(@RequestBody Map<String, Object> body) {
		// delete a specific venue
		if (!body.containsKey("pk")) {
			return ResponseEntity.ok(new ResponseData<>(null, "Failed to find Venue with provided Id.", "400"));
		}
		venueService.removeVenue(body.get("pk").toString());
		return ResponseEntity.ok(new ResponseData<>(body.get("pk").toString(), "Success", "200"));
	}

	@PostMapping("/{venuePk}/slots")
	public ResponseEntity<ResponseData<?>> addVenueSlots(@PathVariable String venuePk,
			@RequestBody TimeSlotData slotData) {
		// Add time slots per venue with no overlaps.
		List<ValidationData> exceptions = timeSlotValidationService.validate(slotData);
		if (!CollectionUtils.isEmpty(exceptions)) {
			return ResponseEntity.ok(new ResponseData<>(exceptions, "Validation Failed!", "400"));
		}
		return ResponseEntity.ok(new ResponseData<>(slotService.upsertVenueSlot(venuePk, slotData), "Success", "200"));
	}

	@GetMapping("/available")
	public ResponseEntity<ResponseData<?>> getAvailableVenues(@RequestBody TimeSlotData slotData) {
		// Fetch available venues for a given time range & sport.
		return ResponseEntity.ok(new ResponseData<>(venueService.getAvailableVenues(slotData), "Success", "200"));
	}

}
