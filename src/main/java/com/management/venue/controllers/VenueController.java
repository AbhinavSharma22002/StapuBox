package com.management.venue.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.venue.pojo.ResponseData;
import com.management.venue.pojo.VenueData;
import com.management.venue.services.VenueService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/venues")
public class VenueController {

    @Autowired
    private VenueService venueService;

    @PostMapping("/upsert-venue")
    public ResponseEntity<ResponseData<?>> upsertVenue(@RequestBody VenueData venueData) {
    	//without any validation
        return ResponseEntity.ok(new ResponseData<>(venueService.upsertVenue(venueData),"Success","200"));
    }

    @GetMapping
    public ResponseEntity<ResponseData<?>> listVenues() {
        return ResponseEntity.ok(new ResponseData<>(venueService.getAllVenues(), "Success", "200"));
    }

    @PostMapping
    public ResponseEntity<ResponseData<?>> getVenue(@RequestBody Map<String,Object> body) {
    	if(!body.containsKey("pk")) {
            return ResponseEntity.ok(new ResponseData<>(null,"Failed to find Venue with provided Id.","400"));    		
    	}
        return ResponseEntity.ok(new ResponseData<>(venueService.getVenueByPk(body.get("pk").toString()),"Success","200"));        
    }

    @PostMapping("/delete-venue")
    public ResponseEntity<ResponseData<?>> deleteVenue(@RequestBody Map<String,Object> body) {
    	if(!body.containsKey("pk")) {
            return ResponseEntity.ok(new ResponseData<>(null,"Failed to find Venue with provided Id.","400"));    		
    	}
    	venueService.removeVenue(body.get("pk").toString());
        return ResponseEntity.ok(new ResponseData<>(body.get("pk").toString(),"Success","200"));
    }
}
