package org.octopus.api.lt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hamcrest.CoreMatchers;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.octopus.api.dto.AppDto;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.JsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class AppControllerLiveTest {
	private RestTemplate restTemplate = new RestTemplate();
	private RestTemplate patchRestTemplate = new RestTemplate();
	private final static String BASE_URL = "http://localhost:8080";

	@Before
	public void init() {
		HttpClient httpClient = HttpClientBuilder.create().build();
		this.patchRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
	}

	@Test
	public void when_health_then_200() throws JSONException {
		String expected = "{\"status\":\"UP\"}";

		ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL + "/api/app/health", String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void when_findById_then_found_200() throws JSONException {
		String expected = "{\"id\":\"1\",\"appId\":\"403\",\"shortName\":\"app1\",\"name\":\"application1\",\"contact\":\"a@gmail.com\"}";

		ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL + "/api/app/1", String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test(expected = HttpClientErrorException.class)
	public void when_api_app_findById_then_notfound_404() {
		restTemplate.getForEntity(BASE_URL + "/api/app/100", String.class);
	}

	@Test
	public void when_create_then_ok_201() throws JSONException {
		String json = "{\"appId\":\"4031\",\"shortName\":\"app1\",\"name\":\"application1\",\"contact\":\"a@gmail.com\"}";
		AppDto dto = AppDto.builder().appId("4031").shortName("app1").name("application1").contact("a@gmail.com")
				.build();

		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);

		HttpEntity<AppDto> entity = new HttpEntity<AppDto>(dto, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL + "/api/app", entity, String.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
		assertThat(response.getHeaders().getLocation().toString(), CoreMatchers.containsString("/api/app"));

		JSONAssert.assertEquals(json, response.getBody(),
				new CustomComparator(JSONCompareMode.LENIENT, new Customization("id", (o1, o2) -> true)));
	}

	@Test
	public void when_create_then_ok_201_with_warninglog() throws JSONException {
		// when with setter id
		String json = "{\"id\":\"1\",\"appId\":\"4031\",\"shortName\":\"app1\",\"name\":\"application1\",\"contact\":\"a@gmail.com\"}";

		AppDto dto = AppDto.builder().id("testsetid").appId("4031").shortName("app1").name("application1")
				.contact("a@gmail.com").build();

		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);

		HttpEntity<AppDto> entity = new HttpEntity<AppDto>(dto, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL + "/api/app", entity, String.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
		String id = JsonPath.parse(response.getBody()).read("$.id");
		assertThat(response.getHeaders().getLocation().toString(), CoreMatchers.containsString("/api/app/" + id));
		JSONAssert.assertEquals(json, response.getBody(),
				new CustomComparator(JSONCompareMode.LENIENT, new Customization("id", (o1, o2) -> true)));
	}

	@Test
	public void when_update_then_ok_200() throws JSONException {
		// when with setter id

		AppDto dto = AppDto.builder().appId("appid").shortName("app1").name("application1").contact("a@gmail.com")
				.build();

		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);

		HttpEntity<AppDto> entity = new HttpEntity<AppDto>(dto, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL + "/api/app", entity, String.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
		String id = JsonPath.parse(response.getBody()).read("$.id");

		// do update
		AppDto dtoUpdating = AppDto.builder().name("application1 to new name - application2").contact("a@gmail.com")
				.build();

		HttpEntity<AppDto> entity2 = new HttpEntity<AppDto>(dtoUpdating, headers);
		// PUT method will use new DTO to override old value
		ResponseEntity<?> response2 = restTemplate.exchange(BASE_URL + "/api/app/" + id, HttpMethod.PUT, entity2,
				String.class);
		// AppDto.class);

		assertEquals(HttpStatus.ACCEPTED, response2.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response2.getHeaders().getContentType());
		String expected = "{\"id\":\"" + id
				+ "\",\"name\":\"application1 to new name - application2\",\"contact\":\"a@gmail.com\"}";
		JSONAssert.assertEquals(expected, response2.getBody().toString(), false);
	}

	@Test
	public void when_patch_then_ok_200() throws JSONException {
		// when with setter id

		AppDto dto = AppDto.builder().appId("appid").shortName("app1").name("application1").contact("a@gmail.com")
				.build();

		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);

		HttpEntity<AppDto> entity = new HttpEntity<AppDto>(dto, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL + "/api/app", entity, String.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
		String id = JsonPath.parse(response.getBody()).read("$.id");

		// do patch
		// patch method will patch certain fields
		AppDto dtoUpdating = AppDto.builder().name("new app name").build();

		HttpEntity<AppDto> entity2 = new HttpEntity<AppDto>(dtoUpdating, headers);

		ResponseEntity<?> response2 = patchRestTemplate.exchange(BASE_URL + "/api/app/" + id, HttpMethod.PATCH, entity2,
				String.class);
		// AppDto.class);

		assertEquals(HttpStatus.ACCEPTED, response2.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response2.getHeaders().getContentType());
		String expected = "{\"id\":\"" + id + "\",\"name\":\"new app name\"}";
		JSONAssert.assertEquals(expected, response2.getBody().toString(), false);

		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "new app name");

		HttpEntity<Map<String, String>> entity3 = new HttpEntity<Map<String, String>>(map, headers);

		ResponseEntity<?> response3 = patchRestTemplate.exchange(BASE_URL + "/api/app/" + id, HttpMethod.PATCH, entity3,
				String.class);
		// AppDto.class);

		assertEquals(HttpStatus.ACCEPTED, response3.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response3.getHeaders().getContentType());
		String expected3 = "{\"id\":\"" + id + "\",\"name\":\"new app name\"}";
		JSONAssert.assertEquals(expected3, response3.getBody().toString(), false);
	}

	@Test
	public void when_patch_unsupportedfield_then_fail() throws JSONException {
		// when with setter id
		AppDto dto = AppDto.builder().appId("appid").shortName("app1").name("application1").contact("a@gmail.com")
				.build();

		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);

		HttpEntity<AppDto> entity = new HttpEntity<AppDto>(dto, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL + "/api/app", entity, String.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
		String id = JsonPath.parse(response.getBody()).read("$.id");

		Map<String, String> map = new HashMap<String, String>();
		map.put("shortname", "new short app name");

		HttpEntity<Map<String, String>> entity3 = new HttpEntity<Map<String, String>>(map, headers);

		try {
			patchRestTemplate.exchange(BASE_URL + "/api/app/" + id, HttpMethod.PATCH, entity3, String.class);
			fail();
		} catch (HttpClientErrorException e) {
			assertThat(e.getMessage(), CoreMatchers.containsString("Field [shortname] update is not allow."));
		}
	}

	@Test
	public void when_delete_accept_202() throws JSONException {
		// when with setter id
		AppDto dto = AppDto.builder().appId("appid").shortName("app1").name("application1").contact("a@gmail.com")
				.build();

		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);

		HttpEntity<AppDto> entity = new HttpEntity<AppDto>(dto, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL + "/api/app", entity, String.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
		String id = JsonPath.parse(response.getBody()).read("$.id");

		HttpEntity<?> entity1 = new HttpEntity<>(headers);

		ResponseEntity<String> response1 = restTemplate.exchange(BASE_URL + "/api/app/" + id, HttpMethod.DELETE,
				entity1, String.class);
		assertEquals(HttpStatus.ACCEPTED, response1.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response1.getHeaders().getContentType());
		JSONAssert.assertEquals(response.getBody().toString(), response1.getBody().toString(), false);
	}

	@Test
	public void when_delete_notfound_then_fail_404() throws JSONException {
		// when with setter id
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		HttpEntity<?> entity1 = new HttpEntity<>(headers);

		try {
			restTemplate.exchange(BASE_URL + "/api/app/abc1", HttpMethod.DELETE, entity1, String.class);
			fail();
		} catch (HttpClientErrorException e) {
			assertThat(e.getMessage(), CoreMatchers.containsString("Entity(AppEntity) Identity(abc1) not found"));
		}
	}
}
