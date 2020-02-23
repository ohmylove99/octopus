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
import org.octopus.api.dto.AppDto;
import org.octopus.api.entity.AppEntity;
import org.octopus.api.service.AppService;
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
public class AppControllerIT {
	@Autowired
	private TestRestTemplate restTemplate;
	private RestTemplate patchRestTemplate;

	@MockBean
	private AppService mockService;

	@MockBean
	private ModelMapper mockMapper;

	private ModelMapper modelMapper = new ModelMapper();

	@Before
	public void init() {
		this.patchRestTemplate = restTemplate.getRestTemplate();
		HttpClient httpClient = HttpClientBuilder.create().build();
		this.patchRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
	}

	@Test
	public void health() throws Exception {

		String expected = "{\"status\":\"UP\"}";

		ResponseEntity<String> response = restTemplate.getForEntity("/api/app/health", String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void findById() throws Exception {
		// given
		AppEntity entity1 = AppEntity//
				.builder()//
				.name("MyApp")//
				.shortName("MyShortApp")//
				.appId("123")//
				.contact("abc@gmail.com")//
				.build();
		entity1.setId("1");
		AppDto appDto1 = modelMapper.map(entity1, AppDto.class);
		// when
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any()))
				.thenReturn(appDto1);
		when(mockService.find(org.mockito.ArgumentMatchers.isA(String.class))).thenReturn(Optional.of(entity1));

		ResponseEntity<String> response = restTemplate.getForEntity("/api/app/1", String.class);
		String expected = "{\"id\":\"1\",\"appId\":\"123\",\"shortName\":\"MyShortApp\",\"name\":\"MyApp\",\"contact\":\"abc@gmail.com\"}";
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void findByAllPage() throws Exception {
		// given
		AppEntity entity1 = AppEntity//
				.builder()//
				.name("MyApp")//
				.shortName("MyShortApp")//
				.appId("123")//
				.contact("abc@gmail.com")//
				.build();
		entity1.setId("1");
		AppDto appDto1 = modelMapper.map(entity1, AppDto.class);
		AppEntity entity2 = AppEntity//
				.builder()//
				.name("MyApp2")//
				.shortName("MyShortApp2")//
				.appId("456")//
				.contact("def@gmail.com")//
				.build();
		entity2.setId("2");

		List<AppEntity> entities = Arrays.asList(entity1, entity2);
		PageImpl<AppEntity> expectedPageEntities = new PageImpl<AppEntity>(entities);

		// when
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any()))
				.thenReturn(appDto1);
		when(mockService.findAll(org.mockito.ArgumentMatchers.isA(Pageable.class))).thenReturn(expectedPageEntities);

		ResponseEntity<RestPage<AppDto>> response = restTemplate.exchange("/api/app/page", HttpMethod.GET, null,
				getPageTypeReference());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

		// {"content":[{"id":"1","appId":"403","shortName":"app1","name":"application1","contact":"a@gmail.com"},{"id":"2","appId":"102","shortName":"app2","name":"application2","contact":"b@hotmail.com"},{"id":"3","appId":"104","shortName":"app3","name":"application3","contact":"c@sina.com"}],"number":0,"size":3,"totalElements":3,"pageable":"INSTANCE","last":true,"totalPages":1,"sort":{"sorted":false,"unsorted":true,"empty":true},"first":true,"numberOfElements":3,"empty":false}
		// JSONAssert.assertEquals(expected, response.getBody().toString(), false);
	}

	@Test
	public void findByAllList() throws Exception {
		// given
		AppEntity entity1 = AppEntity//
				.builder()//
				.name("MyApp")//
				.shortName("MyShortApp")//
				.appId("123")//
				.contact("abc@gmail.com")//
				.build();
		entity1.setId("1");
		AppDto appDto1 = modelMapper.map(entity1, AppDto.class);
		AppEntity entity2 = AppEntity//
				.builder()//
				.name("MyApp2")//
				.shortName("MyShortApp2")//
				.appId("456")//
				.contact("def@gmail.com")//
				.build();
		entity2.setId("2");

		List<AppEntity> entities = Arrays.asList(entity1, entity2);
		PageImpl<AppEntity> expectedPageEntities = new PageImpl<AppEntity>(entities);

		// when
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any()))
				.thenReturn(appDto1);
		when(mockService.findAll(org.mockito.ArgumentMatchers.isA(Pageable.class))).thenReturn(expectedPageEntities);

		ResponseEntity<?> response = restTemplate.getForEntity("/api/app/list", List.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

		// [{id=1, appId=123, shortName=MyShortApp, name=MyApp, contact=abc@gmail.com},
		// {id=1, appId=123, shortName=MyShortApp, name=MyApp, contact=abc@gmail.com}]
		// JSONAssert.assertEquals(expected, response.getBody().toString(), false);
	}

	@Test
	public void create() throws Exception {
		// given
		AppEntity entity1 = AppEntity//
				.builder()//
				.name("MyApp")//
				.shortName("MyShortApp")//
				.appId("123")//
				.contact("abc@gmail.com")//
				.build();
		entity1.setId("1");

		AppDto appDto1 = modelMapper.map(entity1, AppDto.class);

		// when
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(AppDto.class)))
				.thenReturn(appDto1);
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(AppEntity.class)))
				.thenReturn(entity1);

		when(mockService.save(org.mockito.ArgumentMatchers.isA(AppEntity.class))).thenReturn(entity1);

		ResponseEntity<AppDto> response = restTemplate.postForEntity("/api/app", appDto1, AppDto.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

		// [{id=1, appId=123, shortName=MyShortApp, name=MyApp, contact=abc@gmail.com},
		// {id=1, appId=123, shortName=MyShortApp, name=MyApp, contact=abc@gmail.com}]
		// JSONAssert.assertEquals(expected, response.getBody().toString(), false);
	}

	@Test
	public void createOrUpdate() throws Exception {
		// given
		AppEntity entity1 = AppEntity//
				.builder()//
				.name("MyApp")//
				.shortName("MyShortApp")//
				.appId("123")//
				.contact("abc@gmail.com")//
				.id("1").build();

		AppDto appDto1 = modelMapper.map(entity1, AppDto.class);

		// when
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(AppDto.class)))
				.thenReturn(appDto1);
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(AppEntity.class)))
				.thenReturn(entity1);

		when(mockService.find("2")).thenReturn(Optional.empty());
		when(mockService.save(org.mockito.ArgumentMatchers.isA(AppEntity.class))).thenReturn(entity1);
		// Do Create
		HttpEntity<AppDto> httpEntity = new HttpEntity<AppDto>(appDto1);
		ResponseEntity<AppDto> response = restTemplate.exchange("/api/app/2", HttpMethod.PUT, httpEntity, AppDto.class);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

		// Do Update
		when(mockService.find("1")).thenReturn(Optional.of(entity1));

		ResponseEntity<AppDto> response2 = restTemplate.exchange("/api/app/1", HttpMethod.PUT, httpEntity,
				AppDto.class);
		assertEquals(HttpStatus.ACCEPTED, response2.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response2.getHeaders().getContentType());

	}

	@Test
	public void when_patch_notfound_then404() throws Exception {
		// given
		AppEntity entity1 = AppEntity//
				.builder()//
				.name("MyApp")//
				.shortName("MyShortApp")//
				.appId("123")//
				.contact("abc@gmail.com")//
				.build();
		entity1.setId("1");

		AppDto appDto1 = modelMapper.map(entity1, AppDto.class);

		// when
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(AppDto.class)))
				.thenReturn(appDto1);
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(AppEntity.class)))
				.thenReturn(entity1);

		when(mockService.find(org.mockito.ArgumentMatchers.isA(String.class))).thenReturn(Optional.empty());
		// when(mockService.createOrUpdate(org.mockito.ArgumentMatchers.isA(AppEntity.class))).thenReturn(entity1);
		// Do Create
		HttpEntity<AppDto> httpEntity = new HttpEntity<AppDto>(appDto1);
		ResponseEntity<AppDto> response = patchRestTemplate.exchange("/api/app/1?name=abc", HttpMethod.PATCH,
				httpEntity, AppDto.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void when_patch_unsupportfield_then405() throws Exception {
		// given
		AppEntity entity1 = AppEntity//
				.builder()//
				.name("MyApp")//
				.shortName("MyShortApp")//
				.appId("123")//
				.contact("abc@gmail.com")//
				.build();
		entity1.setId("1");

		AppEntity entity2 = AppEntity//
				.builder()//
				.name("abc")//
				.shortName("MyShortApp")//
				.appId("123")//
				.contact("abc@gmail.com")//
				.build();
		entity2.setId("1");

		AppDto appDto1 = modelMapper.map(entity1, AppDto.class);

		// when
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(AppDto.class)))
				.thenReturn(appDto1);
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(AppEntity.class)))
				.thenReturn(entity1);

		when(mockService.find(org.mockito.ArgumentMatchers.isA(String.class))).thenReturn(Optional.of(entity1));
		when(mockService.save(org.mockito.ArgumentMatchers.isA(AppEntity.class))).thenReturn(entity2);
		//

		String patchInJson = "{\"name1\":\"abc\"}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> httpEntity = new HttpEntity<>(patchInJson, headers);

		ResponseEntity<AppDto> response = patchRestTemplate.exchange("/api/app/1?name1=abc", HttpMethod.PATCH,
				httpEntity, AppDto.class);
		assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
	}

	@Test
	public void when_patch_ok_202() throws Exception {
		// given
		AppEntity entity1 = AppEntity//
				.builder()//
				.name("MyApp")//
				.shortName("MyShortApp")//
				.appId("123")//
				.contact("abc@gmail.com")//
				.build();
		entity1.setId("1");

		AppEntity entity2 = AppEntity//
				.builder()//
				.name("abc")//
				.shortName("MyShortApp")//
				.appId("123")//
				.contact("abc@gmail.com")//
				.build();
		entity2.setId("1");

		AppDto appDto1 = modelMapper.map(entity1, AppDto.class);

		// when
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(AppDto.class)))
				.thenReturn(appDto1);
		when(mockMapper.map(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(AppEntity.class)))
				.thenReturn(entity1);

		when(mockService.find(org.mockito.ArgumentMatchers.isA(String.class))).thenReturn(Optional.of(entity1));
		when(mockService.save(org.mockito.ArgumentMatchers.isA(AppEntity.class))).thenReturn(entity2);
		//

		String patchInJson = "{\"name\":\"abc\"}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> httpEntity = new HttpEntity<>(patchInJson, headers);

		ResponseEntity<AppDto> response = patchRestTemplate.exchange("/api/app/1?name1=abc", HttpMethod.PATCH,
				httpEntity, AppDto.class);
		assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
	}

	private ParameterizedTypeReference<RestPage<AppDto>> getPageTypeReference() {
		return new ParameterizedTypeReference<RestPage<AppDto>>() {
		};
	}
}
