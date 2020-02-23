package org.octopus.api.repository.common;

import java.util.List;

public interface IOperations<T, ID> {
	// CRUD - Create
	void create(final T entity);

	// CRUD - Read
	T findById(final ID id);

	List<T> findAll();

	// CRUD - Update
	T update(final T entity);

	// CRUD - Delete
	void delete(final T entity);

	void deleteById(final ID id);
}
