package com.management.venue.repositories;

import org.springframework.stereotype.Repository;

import com.management.venue.entites.Venue;

@Repository
public class VenueRepository extends GenericRepositoryImpl<Venue> {
	public VenueRepository() {
		super(Venue.class);
	}
}