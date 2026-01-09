package com.management.venue.repositories;

import org.springframework.stereotype.Repository;

import com.management.venue.entites.Venue;
import com.management.venue.repositories.service.impl.GenericDaoImpl;

@Repository
public class VenueRepository extends GenericDaoImpl<Venue> {
	public VenueRepository() {
		super(Venue.class);
	}
}