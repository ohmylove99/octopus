package org.octopus.api.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.octopus.api.entity.RoleEntity;
import org.octopus.api.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RoleService {
	@Autowired
	private RoleRepository roleRepository;

	public Optional<RoleEntity> find(String id) {
		return roleRepository.findById(id);
	}

	public List<RoleEntity> findAll() {
		return roleRepository.findAll();
	}

	public Page<RoleEntity> findAll(Pageable pageable) {
		return roleRepository.findAll(pageable);
	}

	public void remove(String id) {
		roleRepository.deleteById(id);
	}

	public RoleEntity save(RoleEntity entity) {
		if (StringUtils.isEmpty(entity.getId())) {
			// do create
			log.info("creating entity...");
			return create(entity);
		} else {
			// do need check entity exists? let's handle it in controller layer
			// not found, do create? do not fix the logic without clear method name
			log.info("updating entity...");
			return update(entity);
		}
	}

	private RoleEntity create(RoleEntity entity) {
		RoleEntity newEntity = roleRepository.saveAndFlush(entity);
		return newEntity;
	}

	private RoleEntity update(RoleEntity entity) {
		RoleEntity newEntity = roleRepository.saveAndFlush(entity);
		return newEntity;
	}
}
