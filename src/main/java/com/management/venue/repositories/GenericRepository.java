package com.management.venue.repositories;

import java.util.List;
import java.util.Map;

import com.management.venue.entites.Primary;
import com.management.venue.exceptions.StapuBoxException;

public interface GenericRepository<T extends Primary> {
	
	T save(T entity) throws StapuBoxException;

	List<T> findByParameters(Map<String, Object> params) throws StapuBoxException;

	void delete(Long pk) throws StapuBoxException;
}