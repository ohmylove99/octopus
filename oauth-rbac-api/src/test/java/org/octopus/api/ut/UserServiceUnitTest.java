package org.octopus.api.ut;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.octopus.api.entity.RoleEntity;
import org.octopus.api.entity.UserEntity;
import org.octopus.api.entity.UserType;
import org.octopus.api.repository.RoleRepository;
import org.octopus.api.repository.UserRepository;
import org.octopus.api.service.UserService;
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
public class UserServiceUnitTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private RoleRepository roleRepository;

	@InjectMocks
	private UserService service;

	@Mock
	private Pageable pageableMock;

	@Mock
	private Page<UserEntity> userPage;

	@Mock
	private Page<RoleEntity> rolePage;

	@Test
	public void whenFindAllUserWithPageableThenReturnUserPage() {
		// given
		UserEntity entity1 = UserEntity//
				.builder()//
				.id("1")//
				.username("username1")//
				.usertype(UserType.PEOPLE)//
				.build();
		UserEntity entity2 = UserEntity//
				.builder()//
				.id("2")//
				.username("username2")//
				.usertype(UserType.FUNCTIONALID)//
				.build();
		UserEntity entity3 = UserEntity//
				.builder()//
				.id("3")//
				.username("username3")//
				.usertype(UserType.FUNCTIONALID)//
				.build();
		List<UserEntity> entities = Arrays.asList(entity1, entity2, entity3);
		PageImpl<UserEntity> expectedPage = new PageImpl<UserEntity>(entities);
		// when
		when(userRepository.findAll(org.mockito.ArgumentMatchers.isA(Pageable.class))).thenReturn(expectedPage);

		Pageable pageable = PageRequest.of(0, 2);// anyhow return all
		Page<UserEntity> actualPage = service.findAll(pageable);
		// then
		assertEquals(3, actualPage.getContent().size());
		assertEquals(expectedPage, actualPage);
	}

	@Test
	public void whenFindAllUserThenReturnUserList() {
		// given
		UserEntity entity1 = UserEntity//
				.builder()//
				.id("1")//
				.username("username1")//
				.usertype(UserType.PEOPLE)//
				.build();
		UserEntity entity2 = UserEntity//
				.builder()//
				.id("2")//
				.username("username2")//
				.usertype(UserType.FUNCTIONALID)//
				.build();
		UserEntity entity3 = UserEntity//
				.builder()//
				.id("3")//
				.username("username3")//
				.usertype(UserType.FUNCTIONALID)//
				.build();
		List<UserEntity> expectedList = Arrays.asList(entity1, entity2, entity3);
		// when
		when(userRepository.findAll()).thenReturn(expectedList);

		List<UserEntity> actualList = service.findAll();
		// then
		assertEquals(3, actualList.size());
		assertEquals(expectedList, actualList);
	}

	@Test
	public void whenFindUserThenReturn() {
		// given
		UserEntity entity1 = UserEntity//
				.builder()//
				.id("1")//
				.username("username1")//
				.usertype(UserType.PEOPLE)//
				.build();
		// when
		when(userRepository.findById("1")).thenReturn(Optional.of(entity1));

		Optional<UserEntity> actualEntity = service.find("1");
		// then
		assertEquals("username1", actualEntity.get().getUsername());

		// when
		when(userRepository.findById("2")).thenReturn(Optional.empty());

		Optional<UserEntity> actualEntity2 = service.find("2");
		// then
		assertTrue(actualEntity2.isEmpty());
	}

	@Test
	public void whenRemoveUserThenRemove() {
		// given

		// when
		doNothing().when(userRepository).deleteById("1");

		service.remove("1");
		// then
		verify(userRepository, times(1)).deleteById("1");
	}

	@Test
	public void whenSaveUserThenReturnSaved() {
		// given
		UserEntity entity1 = UserEntity//
				.builder()//
				.id("1")//
				.username("username1")//
				.usertype(UserType.PEOPLE)//
				.build();
		// when
		when(userRepository.saveAndFlush(org.mockito.ArgumentMatchers.isA(UserEntity.class))).thenReturn(entity1);

		UserEntity actualEntity = service.save(entity1);
		// then
		assertEquals(entity1.getId(), actualEntity.getId());
		assertEquals(entity1.getUsername(), actualEntity.getUsername());
		verify(userRepository, times(1)).saveAndFlush(entity1);
	}
}
