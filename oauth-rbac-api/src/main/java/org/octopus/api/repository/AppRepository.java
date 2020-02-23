package org.octopus.api.repository;

import java.util.List;

import org.octopus.api.entity.AppEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RepositoryRestResource(collectionResourceRel = "apps", path = "apps")
@Repository
public interface AppRepository
		extends JpaRepository<AppEntity, String>, RevisionRepository<AppEntity, String, Integer> {
	@ApiOperation("Find all apps by appId")
	public List<AppEntity> findByAppId(@Param("appId") @RequestParam @ApiParam(value = "appId") String appId);

	public List<AppEntity> findByShortName(String shortName);

	public List<AppEntity> findByName(String name);

	@Query("Select c from App c where c.contact like:contact")
	public List<AppEntity> findByContact(@Param("contact") String contact);
	// http://localhost:8080/apps/search/findByContact?contact=1
	// http://localhost:8080/apps/search/findByContact?param0=1
}