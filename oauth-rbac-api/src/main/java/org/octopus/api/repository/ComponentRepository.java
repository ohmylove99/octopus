package org.octopus.api.repository;

import java.util.List;

import org.octopus.api.entity.ComponentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(collectionResourceRel = "components", path = "components")
@Repository
public interface ComponentRepository
		extends JpaRepository<ComponentEntity, String>, RevisionRepository<ComponentEntity, String, Integer> {
	List<ComponentEntity> findByName(String name);

	@Query("Select c from Component c where c.description like:description")
	List<ComponentEntity> findByDescription(@Param("description") String description);

	@Query("Select c from Component c where c.contact like:contact")
	List<ComponentEntity> findByContact(@Param("contact") String contact);
}