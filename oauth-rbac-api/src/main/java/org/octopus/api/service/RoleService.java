package org.octopus.api.service;

import org.apache.commons.lang3.StringUtils;
import org.octopus.api.entity.RoleEntity;
import org.octopus.api.repository.RoleRepository;
import org.octopus.api.service.common.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RoleService extends AbstractService<RoleEntity, String> {
	@Autowired
	public void setRepository(RoleRepository roleRepository) {
		this.repository = roleRepository;
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
		RoleEntity newEntity = repository.saveAndFlush(entity);
		return newEntity;
	}

	private RoleEntity update(RoleEntity entity) {
		RoleEntity newEntity = repository.saveAndFlush(entity);
		return newEntity;
	}
}
