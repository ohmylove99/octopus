package org.octopus.api.service.common;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractService<T, ID> implements IServices<T, ID> {

	protected JpaRepository<T, ID> repository;

	@Override
	public Optional<T> find(ID id) {
		return repository.findById(id);
	}

	@Override
	public List<T> findAll() {
		return repository.findAll();
	}

	@Override
	public Page<T> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public void remove(ID id) {
		repository.deleteById(id);
	}
}
