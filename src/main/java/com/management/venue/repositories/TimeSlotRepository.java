package com.management.venue.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.management.venue.entites.TimeSlot;
import com.management.venue.exceptions.StapuBoxException;
import com.management.venue.repositories.service.impl.GenericDaoImpl;

@Repository
public class TimeSlotRepository extends GenericDaoImpl<TimeSlot> {

	public TimeSlotRepository() {
		super(TimeSlot.class);
	}
	

	public List<TimeSlot> findAvailableVenues(String sportId, LocalDateTime start, LocalDateTime end)
			throws StapuBoxException {
		/* Query logic:
		 1. Matches the sportId
		 2. Joins with TimeSlot to find records where isAvailable is true
		 3. Ensures the slot covers the requested time range
		*/
		try {
			// We select the TimeSlot (ts) and FETCH the associated Venue (v)
	        String jpql = "SELECT ts FROM TimeSlot ts " +
	                      "JOIN FETCH ts.venue v " +
	                      "WHERE (:sportId IS NULL OR v.sportId = :sportId) " +
	                      "AND (:start IS NULL OR ts.startTime >= :start) " +
	                      "AND (:end IS NULL OR ts.endTime <= :end) " +
	                      "AND ts.isAvailable = true";

	        return entityManager.createQuery(jpql, TimeSlot.class)
	                .setParameter("sportId", sportId)
	                .setParameter("start", start)
	                .setParameter("end", end)
	                .getResultList();
		} catch (Exception e) {
			log.error("Error finding available time slot {}: {}", entityClass.getSimpleName(), e.getMessage(), e);
			throw new StapuBoxException("Error finding available time slot", "500", e);
		}
	}
	
	// Conflict Check Formula: (StartA < EndB) AND (EndA > StartB)
	public boolean existsOverlap(Long venuePk, LocalDateTime requestedStart, LocalDateTime requestedEnd)
			throws StapuBoxException {
		try {
			String jpql = "SELECT COUNT(s) FROM TimeSlot s " + "WHERE s.venue.pk = :venuePk "
					+ "AND s.startTime < :reqEnd " + "AND s.endTime > :reqStart";

			Long count = entityManager.createQuery(jpql, Long.class).setParameter("venuePk", venuePk)
					.setParameter("reqStart", requestedStart).setParameter("reqEnd", requestedEnd).getSingleResult();

			return count > 0;
		} catch (Exception e) {
			log.error("Error validating time slot availability {}: {}", entityClass.getSimpleName(), e.getMessage(), e);			
			throw new StapuBoxException("Error validating time slot availability", "500", e);
		}
	}
}