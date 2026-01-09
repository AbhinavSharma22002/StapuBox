package com.management.venue.repositories;

import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

import com.management.venue.entites.TimeSlot;
import com.management.venue.exceptions.StapuBoxException;
import com.management.venue.repositories.service.impl.GenericDaoImpl;

@Repository
public class TimeSlotRepository extends GenericDaoImpl<TimeSlot> {

	public TimeSlotRepository() {
		super(TimeSlot.class);
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