package com.management.venue.validations.service;

import java.util.List;

import com.management.venue.pojo.PrimaryData;
import com.management.venue.pojo.ValidationData;

public interface GenericValidationService<T extends PrimaryData> {
	
	List<ValidationData> validate(T entity);
	
}