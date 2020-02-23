package org.octopus.api.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.octopus.api.entity.UserEntity;
import org.octopus.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
	@Autowired
	private UserRepository userRepository;

	public Optional<UserEntity> find(String id) {
		return userRepository.findById(id);
	}

	public List<UserEntity> findAll() {
		return userRepository.findAll();
	}

	public Page<UserEntity> findAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	public void remove(String id) {
		userRepository.deleteById(id);
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
		UserEntity newEntity = userRepository.saveAndFlush(entity);
		return newEntity;
	}

	private UserEntity update(UserEntity entity) {
		UserEntity newEntity = userRepository.saveAndFlush(entity);
		return newEntity;
	}
}
