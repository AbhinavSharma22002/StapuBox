package com.management.venue.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SportData {
	
	@JsonProperty("sport_id")
	private Integer sportId;

	@JsonProperty("sport_name")
	private String sportName;

	@JsonProperty("sport_code")
	private String sportCode;

	public SportData() {
	}

	public Integer getSportId() {
		return sportId;
	}

	public void setSportId(Integer sportId) {
		this.sportId = sportId;
	}

	public String getSportName() {
		return sportName;
	}

	public void setSportName(String sportName) {
		this.sportName = sportName;
	}

	public String getSportCode() {
		return sportCode;
	}

	public void setSportCode(String sportCode) {
		this.sportCode = sportCode;
	}
}