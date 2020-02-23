package org.octopus.api.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.octopus.api.entity.AppEntity;
import org.octopus.api.repository.AppRepository;
import org.octopus.api.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class AppServiceIT {
	@TestConfiguration
	static class AppServicelTestContextConfiguration {
		@Bean
		public AppService appService() {
			AppService service = new AppService();
			return service;
		}
	}

	@Autowired
	private AppService appService;

	@MockBean
	private AppRepository appRepository;

	@Test
	public void test_findAll() {
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
				.name("MyApp2")//
				.shortName("MyShortApp2")//
				.appId("456")//
				.contact("def@gmail.com")//
				.build();
		entity2.setId("2");
		AppEntity entity3 = AppEntity//
				.builder()//
				.name("MyApp3")//
				.shortName("MyShortApp3")//
				.appId("789")//
				.contact("def@hotmail.com")//
				.build();
		entity3.setId("3");
		List<AppEntity> entities = Arrays.asList(entity1, entity2, entity3);
		PageImpl<AppEntity> expectedPage = new PageImpl<AppEntity>(entities);
		// when
		when(appService.getRepository().findAll(org.mockito.ArgumentMatchers.isA(Pageable.class)))
				.thenReturn(expectedPage);

		Pageable pageable = PageRequest.of(0, 2);// anyhow return all
		Page<AppEntity> actualPage = appService.findAll(pageable);
		// then
		assertEquals(3, actualPage.getContent().size());
		assertEquals(expectedPage, actualPage);
	}

	@Test
	public void test_findById() {
		// given
		AppEntity entity1 = AppEntity//
				.builder()//
				.name("MyApp")//
				.shortName("MyShortApp")//
				.appId("123")//
				.contact("abc@gmail.com")//
				.build();
		entity1.setId("1");
		// when
		when(appService.getRepository().findById(org.mockito.ArgumentMatchers.isA(String.class)))
				.thenReturn(Optional.of(entity1));

		Optional<AppEntity> actual = appService.find("1");
		// then
		assertEquals(entity1, actual.get());
	}

	@Test
	public void test_remove() {
		// given
		AppEntity entity1 = AppEntity//
				.builder()//
				.name("MyApp")//
				.shortName("MyShortApp")//
				.appId("123")//
				.contact("abc@gmail.com")//
				.build();
		entity1.setId("1");

		// when
		doNothing().when(appService.getRepository()).deleteById("1");

		appService.remove("1");
		// then
		assertTrue(true);
	}

	@Test
	public void test_createOrUpdate() {
		// given
		AppEntity creatingEntity = AppEntity//
				.builder()//
				.name("MyApp2")//
				.shortName("MyShortApp2")//
				.appId("123")//
				.contact("abc2@gmail.com")//
				.build();

		AppEntity updatingEntity = AppEntity//
				.builder()//
				.name("MyApp1")//
				.shortName("MyShortApp1")//
				.appId("123")//
				.contact("abc1@gmail.com")//
				.build();
		updatingEntity.setId("1");

		// when
		creatingEntity.setId("3");
		when(appService.getRepository().saveAndFlush(org.mockito.ArgumentMatchers.isA(AppEntity.class)))
				.thenReturn(creatingEntity);

		AppEntity createdEntity = appService.save(creatingEntity);
		// then
		assertEquals(creatingEntity, createdEntity);

		when(appService.getRepository().saveAndFlush(org.mockito.ArgumentMatchers.isA(AppEntity.class)))
				.thenReturn(updatingEntity);

		AppEntity updatedEntity = appService.save(updatingEntity);
		// then
		assertEquals(updatingEntity, updatedEntity);
	}
}
