package com.management.venue.services;

import java.util.List;

import com.management.venue.pojo.TimeSlotData;
import com.management.venue.pojo.VenueData;

public interface VenueService {

	public VenueData upsertVenue(VenueData venueData);

	public List<VenueData> getAllVenues();

	public VenueData getVenueByPk(String pk);

	public void removeVenue(String pk);

	public List<VenueData> getAvailableVenues(TimeSlotData slotData);
}