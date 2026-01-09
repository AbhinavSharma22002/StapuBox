package com.management.venue.services;

import com.management.venue.pojo.TimeSlotData;

public interface TimeSlotService {

	TimeSlotData upsertVenueSlot(String venuePkStr, TimeSlotData slotData);

}