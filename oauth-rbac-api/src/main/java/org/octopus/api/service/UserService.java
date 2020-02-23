package org.octopus.api.service;

import org.apache.commons.lang3.StringUtils;
import org.octopus.api.entity.UserEntity;
import org.octopus.api.repository.UserRepository;
import org.octopus.api.service.common.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService extends AbstractService<UserEntity, String> {
	@Autowired
	public void setRepository(UserRepository roleRepository) {
		this.repository = roleRepository;
	}

	public UserEntity save(UserEntity entity) {
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

	private UserEntity create(UserEntity entity) {
		UserEntity newEntity = repository.saveAndFlush(entity);
		return newEntity;
	}

	private UserEntity update(UserEntity entity) {
		UserEntity newEntity = repository.saveAndFlush(entity);
		return newEntity;
	}
}
