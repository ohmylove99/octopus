package org.octopus.api.web.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.octopus.api.dto.AppDto;
import org.octopus.api.entity.AppEntity;
import org.octopus.api.exception.EntityNotFoundException;
import org.octopus.api.exception.EntityUnSupportedFieldPatchException;
import org.octopus.api.service.AppService;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/app")
@Getter
@Setter
@Slf4j
public class AppController {
	@Autowired
	private AppService service;
	@Autowired
	private ModelMapper modelMapper;

	@RequestMapping(value = "/health", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> health() {
		return ResponseEntity.ok().body("{\"status\" : \"UP\"}");
	}

	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<RestPage<AppDto>> findAll(Pageable pageable) {
		// Get DB Result
		Page<AppEntity> pageEntity = service.findAll(pageable);
		// Do Convert
		Page<AppDto> pageDto = toPageDtos(pageEntity);
		// Wrap to RestPage
		RestPage<AppDto> pageRest = new RestPage<AppDto>(pageDto.getContent());
		// Return ResponseEntity
		return ResponseEntity.ok().body(pageRest);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseEntity<List<AppDto>> findAllContent(Pageable pageable) {
		// Get DB Result
		Page<AppEntity> pageEntity = service.findAll(pageable);
		// Do Convert
		List<AppDto> dtos = toDtos(pageEntity.getContent());
		// Return ResponseEntity
		return ResponseEntity.ok().body(dtos);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<AppDto> findById(@PathVariable String id) {
		// Get DB Result
		Optional<AppEntity> findEntity = service.find(id);
		// Id should be fetch from URL and do reset
		if (findEntity.isPresent()) {
			AppEntity entity = findEntity.get();
			AppDto dto = toDto(entity);
			return ResponseEntity.ok().body(dto);
		} else {
			throw new EntityNotFoundException(AppEntity.class.getSimpleName(), id.toString());
		}
	}

	// Save
	// @PostMapping()
	@RequestMapping(method = RequestMethod.POST)
	// return 201 instead of 200
	public ResponseEntity<AppDto> create(@Valid @RequestBody AppDto dto) {
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
	public ResponseEntity<AppDto> createOrUpdate(@RequestBody AppDto dto, @PathVariable String id) {
		// Convert to Entity
		if (StringUtils.isNotEmpty(dto.getId())) {
			log.warn("do't need set dto id, please use post method instead, remove id :" + dto.getId()
					+ " and using id :" + id);
			dto.setId(StringUtils.EMPTY);
		}
		// Find Entity from DB
		Optional<AppEntity> findEntity = service.find(id);
		// Id should be fetch from URL and do reset
		if (findEntity.isPresent()) {
			// Do update
			AppEntity newEntity = toEntity(dto);
			newEntity.setId(id);
			return updateEntity(newEntity);
		} else {
			// Do create, else you can throw the not found exception.
			// So even it's convenient, but not suggested
			// return createEntity(dto);
			throw new EntityNotFoundException(AppEntity.class.getSimpleName(), id.toString());
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
	public ResponseEntity<AppDto> patch(@RequestBody Map<String, String> update, @PathVariable String id) {

		Optional<AppEntity> findEntity = service.find(id);

		if (findEntity.isPresent()) {
			AppEntity finalEntity = findEntity.map(x -> {
				ObjectMapper oMapper = new ObjectMapper();
				try {
					AppEntity entity = oMapper.convertValue(update, AppEntity.class);
					entity.setId(id);
					AppEntity rtnEntity = service.save(entity);
					return rtnEntity;
				} catch (Exception e) {
					throw new EntityUnSupportedFieldPatchException(update.keySet());
				}
			}).orElseThrow(() -> {
				throw new EntityUnSupportedFieldPatchException(update.keySet());
			});
			AppDto newDto = toDto(finalEntity);
			return ResponseEntity.accepted().body(newDto);
		} else {
			throw new EntityNotFoundException(AppEntity.class.getSimpleName(), id.toString());
		}
	}

	// @DeleteMapping("/{id}")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<AppDto> delete(@PathVariable String id) {
		Optional<AppEntity> entity = service.find(id);

		if (entity.isPresent()) {
			service.remove(id);
			// Convert to DTO
			AppDto newDto = toDto(entity.get());
			// Return ResponseEntity
			return ResponseEntity.accepted().body(newDto);
		} else {
			throw new EntityNotFoundException(AppEntity.class.getSimpleName(), id.toString());
		}
	}

	// ========= Private Method

	private ResponseEntity<AppDto> createEntity(AppDto dto) {
		// DO Create
		AppEntity entity = service.save(toEntity(dto));
		// Build URI
		String id = entity.getId();
		UriComponents uriComponents = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id);
		// Convert to DTO
		AppDto newDto = toDto(entity);
		// Return ResponseEntity
		return ResponseEntity.created(uriComponents.toUri()).body(newDto);
	}

	private ResponseEntity<AppDto> updateEntity(AppEntity entity) {
		// DO Modify
		AppEntity newEntity = service.save(entity);
		// Convert to DTO
		AppDto newDto = toDto(newEntity);
		// Return ResponseEntity
		return ResponseEntity.accepted().body(newDto);
	}

	private AppDto toDto(AppEntity entity) {
		AppDto dto = // MapperUtils.map(modelMapper, entity, AppDto.class);
				modelMapper.map(entity, AppDto.class);
		return dto;
	}

	private AppEntity toEntity(AppDto dto) {
		AppEntity entity = // MapperUtils.map(modelMapper, dto, AppEntity.class);
				modelMapper.map(dto, AppEntity.class);
		return entity;
	}

	private List<AppDto> toDtos(List<AppEntity> entities) {
		if (entities == null || entities.isEmpty()) {
			return new ArrayList<AppDto>();
		}
		List<AppDto> list = entities.stream().map(entity -> modelMapper.map(entity, AppDto.class))
				.collect(Collectors.toList());
		return list;
	}

	private Page<AppDto> toPageDtos(Page<AppEntity> entities) {
		if (entities == null) {
			return new PageImpl<AppDto>(new ArrayList<AppDto>());
		}
		Page<AppDto> page = entities.map(entity -> modelMapper.map(entity, AppDto.class));
		return page;
	}
}
