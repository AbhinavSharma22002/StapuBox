package com.management.venue.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.management.venue.exceptions.StapuBoxException;
import com.management.venue.logger.BaseLogger;
import com.management.venue.pojo.SportData;
import com.management.venue.pojo.SportResponseWrapper;
import com.management.venue.services.SportDataService;

@Service
public class SportDataServiceImpl extends BaseLogger implements SportDataService {

	private final String SPORTS_API_URL = "https://stapubox.com/sportslist/";
	private List<SportData> cachedSports = null;

	@Override
	public boolean validateSportData(String sportId) {
		if (cachedSports == null) {
			refreshSportsCache();
		}

		// Check if the sport exists in the list to satisfy the "no hardcoding"
		// constraint
		boolean isValid = cachedSports.stream().anyMatch(s -> s.getSportCode().equalsIgnoreCase(sportId));

		if (!isValid) {
			log.error("Validation Failed: Sport code {} is not in the official list", sportId);
			return true;
		}
		return false;
	}

	private void refreshSportsCache() {
		try {
			RestTemplate restTemplate = new RestTemplate();
			// Fetch the wrapper object instead of the array
			SportResponseWrapper response = restTemplate.getForObject(SPORTS_API_URL, SportResponseWrapper.class);

			if (response != null && response.getData() != null) {
				this.cachedSports = response.getData();
				log.info("Successfully cached {} sports from official API.", cachedSports.size());
			} else {
				throw new StapuBoxException("Empty data received from Sports API", "500", null);
			}
		} catch (Exception e) {
			log.error("Failed to map External API response: {}", e.getMessage());
			throw new StapuBoxException("External validation service unavailable", "503", e);
		}
	}
}