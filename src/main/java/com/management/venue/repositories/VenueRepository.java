package com.management.venue.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.management.venue.entites.Venue;

@Repository
public class VenueRepository extends GenericRepositoryImpl<Venue> {
	public VenueRepository() {
		super(Venue.class);
	}

	public List<Venue> findAvailableVenues(String sportId, LocalDateTime start, LocalDateTime end) {
		// Query logic:
		// 1. Matches the sportId
		// 2. Joins with TimeSlot to find records where isAvailable is true
		// 3. Ensures the slot covers the requested time range
		String jpql = "SELECT DISTINCT v FROM TimeSlot ts JOIN ts.venue v " + "WHERE v.sportId = :sportId "
				+ "AND ts.isAvailable = true " + "AND ts.startTime <= :start " + "AND ts.endTime >= :end";

		return entityManager.createQuery(jpql, Venue.class).setParameter("sportId", sportId)
				.setParameter("start", start).setParameter("end", end).getResultList();
	}
}