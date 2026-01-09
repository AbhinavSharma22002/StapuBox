package com.management.venue.services;

import java.util.List;

import com.management.venue.pojo.TimeSlotData;

public interface TimeSlotService {

	TimeSlotData upsertVenueSlot(String venuePkStr, TimeSlotData slotData);

	public List<TimeSlotData> getAvailableVenues(TimeSlotData slotData);

}