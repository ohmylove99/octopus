package org.octopus.api.it;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.octopus.api.dto.RoleDto;
import org.octopus.api.entity.RoleEntity;
import org.octopus.api.service.RoleService;
import org.octopus.api.web.rest.RestPage;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // for restTemplate
@ActiveProfiles("test")
public class RoleControllerIT {
	@Autowired
	private TestRestTemplate restTemplate;
	private RestTemplate patchRestTemplate;

	@MockBean
	private RoleService mockService;

	@MockBean
	private ModelMapper mockMapper;

	private ModelMapper modelMapper = new ModelMapper();

	private RoleEntity entity1;
	private RoleEntity entity2;
	private RoleEntity entity3;

	@Before
	public void init() {
		this.patchRestTemplate = restTemplate.getRestTemplate();
		HttpClient httpClient = HttpClientBuilder.create().build();
		this.patchRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

		// given
		entity1 = RoleEntity//
				.builder()//
				.rolename("rolename1")//
				.id("1")//
				.build();
		entity2 = RoleEntity//
				.builder()//
				.rolename("rolename2")//
				.id("2")//
				.build();
		entity3 = RoleEntity//
				.builder()//
				.rolename("rolename3")//
				.id("3")//
				.build();
	}

	@Test
	public void health() throws Exception {
		String expected = "{\"status\":\"UP\"}";

		ResponseEntity<String> response = restTemplate.getForEntity("/api/role/health", String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void findById() throws Exception {
		// given
		RoleDto RoleDto1 = modelMapper.map(entity1, RoleDto.class);
		// when
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any()))
				.thenReturn(RoleDto1);
		when(mockService.find(org.mockito.ArgumentMatchers.isA(String.class))).thenReturn(Optional.of(entity1));

		ResponseEntity<String> response = restTemplate.getForEntity("/api/role/1", String.class);
		String expected = "{\"id\":\"1\",\"rolename\":\"rolename1\"}";
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void findByAllPage() throws Exception {
		// given
		List<RoleEntity> entities = Arrays.asList(entity1, entity2, entity3);
		PageImpl<RoleEntity> expectedPageEntities = new PageImpl<RoleEntity>(entities);

		// when
		RoleDto RoleDto1 = modelMapper.map(entity1, RoleDto.class);
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any()))
				.thenReturn(RoleDto1);
		when(mockService.findAll(org.mockito.ArgumentMatchers.isA(Pageable.class))).thenReturn(expectedPageEntities);

		ResponseEntity<RestPage<RoleDto>> response = restTemplate.exchange("/api/role/page", HttpMethod.GET, null,
				getPageTypeReference());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
	}

	@Test
	public void findByAllList() throws Exception {
		// given

		List<RoleEntity> entities = Arrays.asList(entity1, entity2);
		PageImpl<RoleEntity> expectedPageEntities = new PageImpl<RoleEntity>(entities);

		// when
		RoleDto RoleDto1 = modelMapper.map(entity1, RoleDto.class);
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any()))
				.thenReturn(RoleDto1);
		when(mockService.findAll(org.mockito.ArgumentMatchers.isA(Pageable.class))).thenReturn(expectedPageEntities);

		ResponseEntity<?> response = restTemplate.getForEntity("/api/role/list", List.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
	}

	@Test
	public void create() throws Exception {
		// given
		RoleDto RoleDto1 = modelMapper.map(entity1, RoleDto.class);

		// when
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(RoleDto.class)))
				.thenReturn(RoleDto1);
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(RoleEntity.class)))
				.thenReturn(entity1);

		when(mockService.save(org.mockito.ArgumentMatchers.isA(RoleEntity.class))).thenReturn(entity1);

		ResponseEntity<RoleDto> response = restTemplate.postForEntity("/api/role", RoleDto1, RoleDto.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
	}

	@Test
	public void createOrUpdate() throws Exception {
		// given
		RoleDto RoleDto1 = modelMapper.map(entity1, RoleDto.class);

		// when
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(RoleDto.class)))
				.thenReturn(RoleDto1);
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(RoleEntity.class)))
				.thenReturn(entity1);

		when(mockService.find("2")).thenReturn(Optional.empty());
		when(mockService.save(org.mockito.ArgumentMatchers.isA(RoleEntity.class))).thenReturn(entity1);
		// Do Create
		HttpEntity<RoleDto> httpEntity = new HttpEntity<RoleDto>(RoleDto1);
		ResponseEntity<RoleDto> response = restTemplate.exchange("/api/role/2", HttpMethod.PUT, httpEntity,
				RoleDto.class);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

		// Do Update
		when(mockService.find("1")).thenReturn(Optional.of(entity1));

		ResponseEntity<RoleDto> response2 = restTemplate.exchange("/api/role/1", HttpMethod.PUT, httpEntity,
				RoleDto.class);
		assertEquals(HttpStatus.ACCEPTED, response2.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response2.getHeaders().getContentType());

	}

	@Test
	public void when_patch_notfound_then404() throws Exception {
		// given
		RoleDto RoleDto1 = modelMapper.map(entity1, RoleDto.class);

		// when
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(RoleDto.class)))
				.thenReturn(RoleDto1);
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(RoleEntity.class)))
				.thenReturn(entity1);

		when(mockService.find(org.mockito.ArgumentMatchers.isA(String.class))).thenReturn(Optional.empty());
		// when(mockService.createOrUpdate(org.mockito.ArgumentMatchers.isA(RoleEntity.class))).thenReturn(entity1);
		// Do Create
		HttpEntity<RoleDto> httpEntity = new HttpEntity<RoleDto>(RoleDto1);
		ResponseEntity<RoleDto> response = patchRestTemplate.exchange("/api/role/1?name=abc", HttpMethod.PATCH,
				httpEntity, RoleDto.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void when_patch_unsupportfield_then405() throws Exception {
		// given
		RoleDto RoleDto1 = modelMapper.map(entity1, RoleDto.class);

		// when
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(RoleDto.class)))
				.thenReturn(RoleDto1);
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(RoleEntity.class)))
				.thenReturn(entity1);

		when(mockService.find(org.mockito.ArgumentMatchers.isA(String.class))).thenReturn(Optional.of(entity1));
		when(mockService.save(org.mockito.ArgumentMatchers.isA(RoleEntity.class))).thenReturn(entity2);
		//

		String patchInJson = "{\"name1\":\"abc\"}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> httpEntity = new HttpEntity<>(patchInJson, headers);

		ResponseEntity<RoleDto> response = patchRestTemplate.exchange("/api/role/1?name1=abc", HttpMethod.PATCH,
				httpEntity, RoleDto.class);
		assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
	}

	@Test
	public void when_patch_ok_202() throws Exception {
		// given
		RoleDto RoleDto1 = modelMapper.map(entity1, RoleDto.class);

		// when
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(RoleDto.class)))
				.thenReturn(RoleDto1);
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(RoleEntity.class)))
				.thenReturn(entity1);

		when(mockService.find(org.mockito.ArgumentMatchers.isA(String.class))).thenReturn(Optional.of(entity1));
		when(mockService.save(org.mockito.ArgumentMatchers.isA(RoleEntity.class))).thenReturn(entity2);
		//

		String patchInJson = "{\"rolename\":\"abc\"}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> httpEntity = new HttpEntity<>(patchInJson, headers);

		ResponseEntity<RoleDto> response = patchRestTemplate.exchange("/api/role/1?name1=abc", HttpMethod.PATCH,
				httpEntity, RoleDto.class);
		assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
	}

	private ParameterizedTypeReference<RestPage<RoleDto>> getPageTypeReference() {
		return new ParameterizedTypeReference<RestPage<RoleDto>>() {
		};
	}
}
