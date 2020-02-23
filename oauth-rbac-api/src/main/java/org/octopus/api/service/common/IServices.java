package org.octopus.api.service.common;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IServices<T, ID> {
	// CRUD - Create/Update
	// Object save(final T entity);

	// CRUD - Create/Update
	T save(final T entity);

	// CRUD - Read
	Optional<T> find(final ID id);

	List<T> findAll();

	Page<T> findAll(Pageable pageable);

	// CRUD - Delete
	void remove(final ID id);
}
