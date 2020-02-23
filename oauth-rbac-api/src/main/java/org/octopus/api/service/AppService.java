package org.octopus.api.service;

import org.apache.commons.lang3.StringUtils;
import org.octopus.api.entity.AppEntity;
import org.octopus.api.repository.AppRepository;
import org.octopus.api.service.common.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AppService extends AbstractService<AppEntity, String> {
	@Autowired
	public void setRepository(AppRepository appRepository) {
		this.repository = appRepository;
	}

	@Override
	public AppEntity save(AppEntity entity) {
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

	private AppEntity create(AppEntity entity) {
		AppEntity newEntity = repository.saveAndFlush(entity);
		return newEntity;
	}

	private AppEntity update(AppEntity entity) {
		AppEntity newEntity = repository.saveAndFlush(entity);
		return newEntity;
	}
}
