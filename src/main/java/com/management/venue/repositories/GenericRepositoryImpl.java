package com.management.venue.repositories;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.management.venue.entites.Primary;
import com.management.venue.exceptions.StapuBoxException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Transactional
public abstract class GenericRepositoryImpl<T extends Primary> implements GenericRepository<T> {
	
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GenericRepositoryImpl.class);
			
	@PersistenceContext
	protected EntityManager entityManager;

	protected final Class<T> entityClass;

	public GenericRepositoryImpl(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	public T save(T entity) throws StapuBoxException {
		try {
			if (entity.getPk() == null) {
				entityManager.persist(entity);
				return entity;
			}
			return entityManager.merge(entity);
		} catch (Exception e) {
			// log the error
			log.error("Error upserting {}: {}", entityClass.getSimpleName(), e.getMessage(), e);
			throw new StapuBoxException("Exception occured while upserting Venue", "500", e);
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
}