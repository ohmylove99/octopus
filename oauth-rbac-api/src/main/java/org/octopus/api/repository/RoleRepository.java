package org.octopus.api.repository;

import org.octopus.api.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(collectionResourceRel = "roles", path = "roles")
@Repository
public interface RoleRepository
		extends JpaRepository<RoleEntity, String>, RevisionRepository<RoleEntity, String, Integer> {
	//
}