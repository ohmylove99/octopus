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
import org.octopus.api.dto.UserDto;
import org.octopus.api.entity.UserEntity;
import org.octopus.api.entity.UserType;
import org.octopus.api.service.UserService;
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
public class UserControllerIT {
	@Autowired
	private TestRestTemplate restTemplate;
	private RestTemplate patchRestTemplate;

	@MockBean
	private UserService mockService;

	@MockBean
	private ModelMapper mockMapper;

	private ModelMapper modelMapper = new ModelMapper();

	private UserEntity entity1;
	private UserEntity entity2;
	private UserEntity entity3;

	@Before
	public void init() {
		this.patchRestTemplate = restTemplate.getRestTemplate();
		HttpClient httpClient = HttpClientBuilder.create().build();
		this.patchRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

		// given
		entity1 = UserEntity//
				.builder()//
				.username("Username1")//
				.usertype(UserType.FUNCTIONALID)//
				.id("1")//
				.build();
		entity2 = UserEntity//
				.builder()//
				.username("Username2")//
				.usertype(UserType.FUNCTIONALID)//
				.id("2")//
				.build();
		entity3 = UserEntity//
				.builder()//
				.username("Username3")//
				.usertype(UserType.PEOPLE)//
				.id("3")//
				.build();
	}

	@Test
	public void health() throws Exception {
		String expected = "{\"status\":\"UP\"}";

		ResponseEntity<String> response = restTemplate.getForEntity("/api/user/health", String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void findById() throws Exception {
		// given
		UserDto UserDto1 = modelMapper.map(entity1, UserDto.class);
		// when
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any()))
				.thenReturn(UserDto1);
		when(mockService.find(org.mockito.ArgumentMatchers.isA(String.class))).thenReturn(Optional.of(entity1));

		ResponseEntity<String> response = restTemplate.getForEntity("/api/user/1", String.class);
		String expected = "{\"id\":\"1\",\"username\":\"Username1\"}";
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void findByAllPage() throws Exception {
		// given
		List<UserEntity> entities = Arrays.asList(entity1, entity2, entity3);
		PageImpl<UserEntity> expectedPageEntities = new PageImpl<UserEntity>(entities);

		// when
		UserDto UserDto1 = modelMapper.map(entity1, UserDto.class);
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any()))
				.thenReturn(UserDto1);
		when(mockService.findAll(org.mockito.ArgumentMatchers.isA(Pageable.class)))
				.thenReturn(expectedPageEntities);

		ResponseEntity<RestPage<UserDto>> response = restTemplate.exchange("/api/user/page", HttpMethod.GET, null,
				getPageTypeReference());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
	}

	@Test
	public void findByAllList() throws Exception {
		// given

		List<UserEntity> entities = Arrays.asList(entity1, entity2);
		PageImpl<UserEntity> expectedPageEntities = new PageImpl<UserEntity>(entities);

		// when
		UserDto UserDto1 = modelMapper.map(entity1, UserDto.class);
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any()))
				.thenReturn(UserDto1);
		when(mockService.findAll(org.mockito.ArgumentMatchers.isA(Pageable.class)))
				.thenReturn(expectedPageEntities);

		ResponseEntity<?> response = restTemplate.getForEntity("/api/user/list", List.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
	}

	@Test
	public void create() throws Exception {
		// given
		UserDto UserDto1 = modelMapper.map(entity1, UserDto.class);

		// when
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(UserDto.class)))
				.thenReturn(UserDto1);
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(UserEntity.class)))
				.thenReturn(entity1);

		when(mockService.save(org.mockito.ArgumentMatchers.isA(UserEntity.class))).thenReturn(entity1);

		ResponseEntity<UserDto> response = restTemplate.postForEntity("/api/user", UserDto1, UserDto.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
	}

	@Test
	public void createOrUpdate() throws Exception {
		// given
		UserDto UserDto1 = modelMapper.map(entity1, UserDto.class);

		// when
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(UserDto.class)))
				.thenReturn(UserDto1);
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(UserEntity.class)))
				.thenReturn(entity1);

		when(mockService.find("2")).thenReturn(Optional.empty());
		when(mockService.save(org.mockito.ArgumentMatchers.isA(UserEntity.class))).thenReturn(entity1);
		// Do Create
		HttpEntity<UserDto> httpEntity = new HttpEntity<UserDto>(UserDto1);
		ResponseEntity<UserDto> response = restTemplate.exchange("/api/user/2", HttpMethod.PUT, httpEntity,
				UserDto.class);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

		// Do Update
		when(mockService.find("1")).thenReturn(Optional.of(entity1));

		ResponseEntity<UserDto> response2 = restTemplate.exchange("/api/user/1", HttpMethod.PUT, httpEntity,
				UserDto.class);
		assertEquals(HttpStatus.ACCEPTED, response2.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response2.getHeaders().getContentType());

	}

	@Test
	public void when_patch_notfound_then404() throws Exception {
		// given
		UserDto UserDto1 = modelMapper.map(entity1, UserDto.class);

		// when
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(UserDto.class)))
				.thenReturn(UserDto1);
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(UserEntity.class)))
				.thenReturn(entity1);

		when(mockService.find(org.mockito.ArgumentMatchers.isA(String.class))).thenReturn(Optional.empty());
		// when(mockService.createOrUpdate(org.mockito.ArgumentMatchers.isA(UserEntity.class))).thenReturn(entity1);
		// Do Create
		HttpEntity<UserDto> httpEntity = new HttpEntity<UserDto>(UserDto1);
		ResponseEntity<UserDto> response = patchRestTemplate.exchange("/api/user/1?name=abc", HttpMethod.PATCH,
				httpEntity, UserDto.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void when_patch_unsupportfield_then405() throws Exception {
		// given
		UserDto UserDto1 = modelMapper.map(entity1, UserDto.class);

		// when
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(UserDto.class)))
				.thenReturn(UserDto1);
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(UserEntity.class)))
				.thenReturn(entity1);

		when(mockService.find(org.mockito.ArgumentMatchers.isA(String.class))).thenReturn(Optional.of(entity1));
		when(mockService.save(org.mockito.ArgumentMatchers.isA(UserEntity.class))).thenReturn(entity2);
		//

		String patchInJson = "{\"name1\":\"abc\"}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> httpEntity = new HttpEntity<>(patchInJson, headers);

		ResponseEntity<UserDto> response = patchRestTemplate.exchange("/api/user/1?name1=abc", HttpMethod.PATCH,
				httpEntity, UserDto.class);
		assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
	}

	@Test
	public void when_patch_ok_202() throws Exception {
		// given
		UserDto UserDto1 = modelMapper.map(entity1, UserDto.class);

		// when
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(UserDto.class)))
				.thenReturn(UserDto1);
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(UserEntity.class)))
				.thenReturn(entity1);

		when(mockService.find(org.mockito.ArgumentMatchers.isA(String.class))).thenReturn(Optional.of(entity1));
		when(mockService.save(org.mockito.ArgumentMatchers.isA(UserEntity.class))).thenReturn(entity2);
		//

		String patchInJson = "{\"username\":\"abc\"}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> httpEntity = new HttpEntity<>(patchInJson, headers);

		ResponseEntity<UserDto> response = patchRestTemplate.exchange("/api/user/1?name1=abc", HttpMethod.PATCH,
				httpEntity, UserDto.class);
		assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
	}

	private ParameterizedTypeReference<RestPage<UserDto>> getPageTypeReference() {
		return new ParameterizedTypeReference<RestPage<UserDto>>() {
		};
	}
}
