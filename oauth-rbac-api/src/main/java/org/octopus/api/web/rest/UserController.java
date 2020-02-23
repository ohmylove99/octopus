package org.octopus.api.web.rest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.octopus.api.dto.RoleDto;
import org.octopus.api.dto.UserDto;
import org.octopus.api.entity.User2RoleEntity;
import org.octopus.api.entity.UserEntity;
import org.octopus.api.exception.EntityNotFoundException;
import org.octopus.api.exception.EntityUnSupportedFieldPatchException;
import org.octopus.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/user")
@Getter
@Setter
@Slf4j
public class UserController {
	@Autowired
	private UserService service;
	@Autowired
	private ModelMapper modelMapper;

	@RequestMapping(value = "/health", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> health() {
		return ResponseEntity.ok().body("{\"status\" : \"UP\"}");
	}

	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<RestPage<UserDto>> findAll(Pageable pageable) {
		// Get DB Result
		Page<UserEntity> pageEntity = service.findAll(pageable);
		// Do Convert
		Page<UserDto> pageDto = toPageDtos(pageEntity);
		// Wrap to RestPage
		RestPage<UserDto> pageRest = new RestPage<UserDto>(pageDto.getContent());
		// Return ResponseEntity
		return ResponseEntity.ok().body(pageRest);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseEntity<List<UserDto>> findAllContent(Pageable pageable) {
		// Get DB Result
		Page<UserEntity> pageEntity = service.findAll(pageable);
		// Do Convert
		List<UserDto> dtos = toDtos(pageEntity.getContent());
		// Return ResponseEntity
		return ResponseEntity.ok().body(dtos);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<UserDto> findById(@PathVariable String id) {
		// Get DB Result
		Optional<UserEntity> findEntity = service.find(id);
		// Id should be fetch from URL and do reset
		if (findEntity.isPresent()) {
			UserEntity entity = findEntity.get();
			UserDto dto = toDto(entity);
			return ResponseEntity.ok().body(dto);
		} else {
			throw new EntityNotFoundException(UserEntity.class.getSimpleName(), id.toString());
		}
	}

	// Save
	// @PostMapping()
	@RequestMapping(method = RequestMethod.POST)
	// return 201 instead of 200
	public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto dto) {
		if (StringUtils.isNotEmpty(dto.getId())) {
			log.warn("do't need set dto id with post, ingore the id :" + dto.getId());
			dto.setId(StringUtils.EMPTY);
		}
		return createEntity(dto);
	}

	/**
	 * This method will do create or update
	 * 
	 * @param dto
	 * @param id
	 * @return
	 */
	// @PutMapping("/{id}")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<UserDto> createOrUpdate(@RequestBody UserDto dto, @PathVariable String id) {
		// Convert to Entity
		if (StringUtils.isNotEmpty(dto.getId())) {
			log.warn("do't need set dto id, please use post method instead, remove id :" + dto.getId()
					+ " and using id :" + id);
			dto.setId(StringUtils.EMPTY);
		}
		// Find Entity from DB
		Optional<UserEntity> findEntity = service.find(id);
		// Id should be fetch from URL and do reset
		if (findEntity.isPresent()) {
			// Do update
			UserEntity newEntity = toEntity(dto);
			newEntity.setId(id);
			return updateEntity(newEntity);
		} else {
			// Do create, else you can throw the not found exception.
			// So even it's convenient, but not suggested
			// return createEntity(dto);
			throw new EntityNotFoundException(UserEntity.class.getSimpleName(), id.toString());
		}
	}

	/**
	 * Not All Client Support Path, please use http client in case
	 * 
	 * @param update
	 * @param id
	 * @return
	 */
	// update name only, you can extends via reflection
	// @PatchMapping("/{id}")
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, produces = "application/json")
	public ResponseEntity<UserDto> patch(@RequestBody Map<String, String> update, @PathVariable String id) {

		Optional<UserEntity> findEntity = service.find(id);

		if (findEntity.isPresent()) {
			UserEntity finalEntity = findEntity.map(x -> {
				ModelMapper oMapper = new ModelMapper();
				try {
					UserEntity entity = oMapper.map(update, UserEntity.class);
					entity.setId(id);
					UserEntity rtnEntity = service.save(entity);
					return rtnEntity;
				} catch (Exception e) {
					throw new EntityUnSupportedFieldPatchException(update.keySet());
				}
			}).orElseThrow(() -> {
				throw new EntityUnSupportedFieldPatchException(update.keySet());
			});
			UserDto newDto = toDto(finalEntity);
			return ResponseEntity.accepted().body(newDto);
		} else {
			throw new EntityNotFoundException(UserEntity.class.getSimpleName(), id.toString());
		}
	}

	// @DeleteMapping("/{id}")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<UserDto> delete(@PathVariable String id) {
		Optional<UserEntity> entity = service.find(id);

		if (entity.isPresent()) {
			service.remove(id);
			// Convert to DTO
			//UserDto newDto = toDto(entity.get());
			// Return ResponseEntity
			return ResponseEntity.accepted().build();//.body(newDto);
		} else {
			throw new EntityNotFoundException(UserEntity.class.getSimpleName(), id.toString());
		}
	}

	// ========= Private Method

	private ResponseEntity<UserDto> createEntity(UserDto dto) {
		// DO Create
		UserEntity entity = service.save(toEntity(dto));
		// Build URI
		String id = entity.getId();
		UriComponents uriComponents = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id);
		// Convert to DTO
		UserDto newDto = toDto(entity);
		// Return ResponseEntity
		return ResponseEntity.created(uriComponents.toUri()).body(newDto);
	}

	private ResponseEntity<UserDto> updateEntity(UserEntity entity) {
		// DO Modify
		UserEntity newEntity = service.save(entity);
		// Convert to DTO
		UserDto newDto = toDto(newEntity);
		// Return ResponseEntity
		return ResponseEntity.accepted().body(newDto);
	}

	private UserDto toDto(UserEntity entity) {
		UserDto dto = // MapperUtils.map(modelMapper, entity, UserDto.class);
				modelMapper.map(entity, UserDto.class);

		Set<RoleDto> roles = new HashSet<RoleDto>();

		Set<User2RoleEntity> user2roles = entity.getUser2roles();
		if (entity.getUser2roles() != null) {
			user2roles.forEach(user2role -> roles.add(modelMapper.map(user2role.getRole(), RoleDto.class)));
		}

		dto.setRoles(roles);

		return dto;
	}

	private UserEntity toEntity(UserDto dto) {
		UserEntity entity = // MapperUtils.map(modelMapper, dto, UserEntity.class);
				modelMapper.map(dto, UserEntity.class);
		return entity;
	}

	private List<UserDto> toDtos(List<UserEntity> entities) {
		if (entities == null || entities.isEmpty()) {
			return new ArrayList<UserDto>();
		}
		List<UserDto> list = entities.stream().map(entity -> modelMapper.map(entity, UserDto.class))
				.collect(Collectors.toList());
		return list;
	}

	private Page<UserDto> toPageDtos(Page<UserEntity> entities) {
		if (entities == null) {
			return new PageImpl<UserDto>(new ArrayList<UserDto>());
		}
		Page<UserDto> page = entities.map(entity -> modelMapper.map(entity, UserDto.class));
		return page;
	}
}
