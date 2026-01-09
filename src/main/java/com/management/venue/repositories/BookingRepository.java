package com.management.venue.repositories;

import org.springframework.stereotype.Repository;

import com.management.venue.entites.Booking;
import com.management.venue.repositories.service.impl.GenericDaoImpl;

@Repository
public class BookingRepository extends GenericDaoImpl<Booking> {
	public BookingRepository() {
		super(Booking.class);
	}
}