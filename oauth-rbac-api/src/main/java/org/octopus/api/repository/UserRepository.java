package org.octopus.api.repository;

import org.octopus.api.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
@Repository
public interface UserRepository
		extends JpaRepository<UserEntity, String>, RevisionRepository<UserEntity, String, Integer> {
	//
}