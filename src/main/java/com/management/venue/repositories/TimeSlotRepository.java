package com.management.venue.repositories;

import org.springframework.stereotype.Repository;

import com.management.venue.entites.TimeSlot;

@Repository
public class TimeSlotRepository extends GenericRepositoryImpl<TimeSlot> {

	public TimeSlotRepository() {
		super(TimeSlot.class);
	}
}