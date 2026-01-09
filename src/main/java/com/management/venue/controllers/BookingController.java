package com.management.venue.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.venue.logger.BaseLogger;
import com.management.venue.pojo.ResponseData;
import com.management.venue.pojo.TimeSlotData;
import com.management.venue.pojo.ValidationData;
import com.management.venue.services.BookingService;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/api/bookings")
public class BookingController extends BaseLogger {

	@Autowired
	private BookingService bookingService;

	@PostMapping
	public ResponseEntity<ResponseData<?>> bookSlot(@RequestBody TimeSlotData slotData) {
		// book a slot
		if (null == slotData || StringUtils.isEmpty(slotData.getPk())) {
			return ResponseEntity.ok(new ResponseData<>(
					new ArrayList<ValidationData>().add(new ValidationData("pk", "Slot Pk is required!")),
					"Validation Failed!", "400"));
		}
		return ResponseEntity.ok(new ResponseData<>(bookingService.bookSlot(slotData), "Success", "200"));
	}

	@GetMapping("/{bookingPk}")
	public ResponseEntity<ResponseData<?>> viewBooking(@PathVariable String bookingPk) {
		// view a booking
		return ResponseEntity.ok(new ResponseData<>(bookingService.getBookingByPk(bookingPk), "Success", "200"));
	}

	@PutMapping("/{bookingPk}/cancel")
	public ResponseEntity<ResponseData<?>> cancelSlot(@PathVariable String bookingPk) {
		// cancel a booking
		bookingService.cancelBooking(bookingPk);
		return ResponseEntity.ok(new ResponseData<>(bookingPk, "Success", "200"));
	}

}
