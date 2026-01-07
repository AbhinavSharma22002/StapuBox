package com.management.venue.repositories;

import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

import com.management.venue.entites.TimeSlot;
import com.management.venue.exceptions.StapuBoxException;

@Repository
public class TimeSlotRepository extends GenericRepositoryImpl<TimeSlot> {

	public TimeSlotRepository() {
		super(TimeSlot.class);
	}

	public boolean existsOverlap(Long venuePk, LocalDateTime requestedStart, LocalDateTime requestedEnd)
			throws StapuBoxException {
		try {
			String jpql = "SELECT COUNT(s) FROM TimeSlot s " + "WHERE s.venue.pk = :venuePk "
					+ "AND s.startTime < :reqEnd " + "AND s.endTime > :reqStart";

			Long count = entityManager.createQuery(jpql, Long.class).setParameter("venuePk", venuePk)
					.setParameter("reqStart", requestedStart).setParameter("reqEnd", requestedEnd).getSingleResult();

			return count > 0;
		} catch (Exception e) {
			// Using the 'log' inherited from @Slf4j in the parent (if made protected)
			// or just throw the custom exception
			throw new StapuBoxException("Error validating time slot availability", "500", e);
		}
	}
}