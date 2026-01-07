package com.management.venue.repositories;

import org.springframework.stereotype.Repository;

import com.management.venue.entites.Booking;

@Repository
public class BookingRepository extends GenericRepositoryImpl<Booking> {
	public BookingRepository() {
		super(Booking.class);
	}
}