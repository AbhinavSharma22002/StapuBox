package com.management.venue.repositories.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.transaction.annotation.Transactional;

import com.management.venue.entites.Primary;
import com.management.venue.exceptions.StapuBoxException;
import com.management.venue.logger.BaseLogger;
import com.management.venue.repositories.service.GenericDaoService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Transactional
public abstract class GenericDaoImpl<T extends Primary> extends BaseLogger implements GenericDaoService<T> {

	@PersistenceContext
	protected EntityManager entityManager;

	protected final Class<T> entityClass;

	public GenericDaoImpl(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	public T save(T entity) throws StapuBoxException {
		try {
			if (null == entity.getPk()) {
				entityManager.persist(entity);
				return entity;
			} else {
				// PARTIAL UPDATE LOGIC:
				// Fetch the current state from the DB
				T existingEntity = entityManager.find(entityClass, entity.getPk());
				if (existingEntity == null) {
					throw new StapuBoxException("Entity not found for update", "404", null);
				}
				// Copy only non-null properties from 'entity' to 'existingEntity'
				this.copyNonNullProperties(entity, existingEntity);
				return entityManager.merge(entity);
			}
		} catch (Exception e) {
			log.error("Error upserting {}: {}", entityClass.getSimpleName(), e.getMessage(), e);
			throw new StapuBoxException("Exception occurred while upserting " + entityClass.getSimpleName(), "500", e);
		}
	}

	@Override
	public List<T> findByParameters(Map<String, Object> params) throws StapuBoxException {
		try {
			StringBuilder jpql = new StringBuilder("SELECT e FROM ").append(entityClass.getSimpleName())
					.append(" e WHERE 1=1 ");

			params.forEach((key, value) -> jpql.append(" AND e.").append(key).append(" = :").append(key));

			TypedQuery<T> query = entityManager.createQuery(jpql.toString(), entityClass);
			params.forEach(query::setParameter);

			return query.getResultList();
		} catch (Exception e) {
			// log the error
			log.error("Error finding {}: {}", entityClass.getSimpleName(), e.getMessage(), e);
			throw new StapuBoxException("Exception occured while finding Venue", "500", e);
		}
	}

	@Override
	public void delete(Long pk) throws StapuBoxException {
		try {
			T entity = entityManager.find(entityClass, pk);
			if (entity != null) {
				entityManager.remove(entity);
			}
		} catch (Exception e) {
			// log the error
			log.error("Error deleting {}: {}", entityClass.getSimpleName(), e.getMessage(), e);
			throw new StapuBoxException("Exception occured while deleting Venue", "500", e);
		}
	}

	/**
	 * Utility to copy properties that are NOT null. This is the heart of the
	 * "Partial Update" logic.
	 */
	private void copyNonNullProperties(Object source, Object target) {
		BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
	}

	private String[] getNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<>();
		for (java.beans.PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null)
				emptyNames.add(pd.getName());
		}
		return emptyNames.toArray(new String[0]);
	}
}