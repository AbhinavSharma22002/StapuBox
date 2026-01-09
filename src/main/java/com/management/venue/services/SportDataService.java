package com.management.venue.services;

import org.springframework.stereotype.Service;

@Service
public interface SportDataService  {

	boolean validateSportData(String sportId);
	
}