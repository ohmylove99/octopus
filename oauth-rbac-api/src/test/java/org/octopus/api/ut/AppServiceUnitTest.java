package org.octopus.api.ut;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.octopus.api.entity.AppEntity;
import org.octopus.api.repository.AppRepository;
import org.octopus.api.service.AppService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * 
 * @author joshualeng
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class AppServiceUnitTest {

	@Mock
	private AppRepository repository;

	@InjectMocks
	private AppService service;

	@Mock
	private Pageable pageableMock;

	@Mock
	private Page<AppEntity> appPage;

	@Test
	public void whenFindAllPagethenReturnPage() {
		// given
		AppEntity entity1 = AppEntity//
				.builder()//
				.id("1")//
				.name("MyApp")//
				.shortName("MyShortApp")//
				.appId("123")//
				.contact("abc@gmail.com")//
				.build();
		AppEntity entity2 = AppEntity//
				.builder()//
				.id("2")//
				.name("MyApp2")//
				.shortName("MyShortApp2")//
				.appId("456")//
				.contact("def@gmail.com")//
				.build();
		AppEntity entity3 = AppEntity//
				.builder()//
				.id("3")//
				.name("MyApp3")//
				.shortName("MyShortApp3")//
				.appId("789")//
				.contact("def@hotmail.com")//
				.build();
		List<AppEntity> entities = Arrays.asList(entity1, entity2, entity3);
		PageImpl<AppEntity> expectedPage = new PageImpl<AppEntity>(entities);
		// when
		when(repository.findAll(org.mockito.ArgumentMatchers.isA(Pageable.class))).thenReturn(expectedPage);

		Pageable pageable = PageRequest.of(0, 2);// anyhow return all
		Page<AppEntity> actualPage = service.findAll(pageable);
		// then
		assertEquals(3, actualPage.getContent().size());
		assertEquals(expectedPage, actualPage);
	}
}
