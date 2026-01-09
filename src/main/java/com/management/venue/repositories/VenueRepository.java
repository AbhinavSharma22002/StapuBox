package com.management.venue.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.management.venue.entites.Venue;
import com.management.venue.exceptions.StapuBoxException;
import com.management.venue.repositories.service.impl.GenericDaoImpl;

@Repository
public class VenueRepository extends GenericDaoImpl<Venue> {
	public VenueRepository() {
		super(Venue.class);
	}

	public List<Venue> findAvailableVenues(String sportId, LocalDateTime start, LocalDateTime end)
			throws StapuBoxException {
		/* Query logic:
		 1. Matches the sportId
		 2. Joins with TimeSlot to find records where isAvailable is true
		 3. Ensures the slot covers the requested time range
		*/
		try {
			String jpql = "SELECT DISTINCT v FROM TimeSlot ts JOIN ts.venue v " + "WHERE v.sportId = :sportId "
					+ "AND ts.isAvailable = true " + "AND ts.startTime <= :start " + "AND ts.endTime >= :end";

			return entityManager.createQuery(jpql, Venue.class).setParameter("sportId", sportId)
					.setParameter("start", start).setParameter("end", end).getResultList();
		} catch (Exception e) {
			log.error("Error finding available venues {}: {}", entityClass.getSimpleName(), e.getMessage(), e);
			throw new StapuBoxException("Error finding available venues", "500", e);
		}
	}
}