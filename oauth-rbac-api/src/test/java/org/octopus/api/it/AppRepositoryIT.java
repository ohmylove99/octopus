package org.octopus.api.it;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Optional;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.octopus.api.entity.AppEntity;
import org.octopus.api.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@RunWith(SpringRunner.class)
@DataJpaTest
//@Import(ApplicationConfigurationTest.class)
@ActiveProfiles("test")
public class AppRepositoryIT {
	/**
	 * This is used by test with enabling enversRevisionRepository Factory Bean
	 * 
	 */
	@TestConfiguration
	@EnableJpaAuditing
	@EnableJpaRepositories(basePackages = "org.octopus.api.repository", repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
	static class AppServicelTestContextConfiguration {
	}

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private AppRepository appRepository;

	@Autowired
	private PlatformTransactionManager platformTransactionManager;

	@Before
	public void setUp() {
		appRepository.deleteAll();
		// given
		AppEntity entity = AppEntity//
				.builder()//
				.name("MyApp")//
				.shortName("MyShortApp")//
				.appId("123")//
				.contact("abc@gmail.com")//
				.build();
		entityManager.persist(entity);
		entityManager.flush();
	}

	@Test
	public void test_findAll() {
		// given

		// when
		List<AppEntity> apps = appRepository.findAll();

		// then
		assertEquals(1, apps.size());
	}

	@Test
	public void test_findByName() {
		// given

		// when
		List<AppEntity> apps = appRepository.findByName("MyApp");
		assertEquals(1, apps.size());

		// then
		assertThat(apps).extracting(AppEntity::getName).containsOnly("MyApp");
	}

	@Test
	public void test_findByShortName() {

		List<AppEntity> apps = appRepository.findByShortName("MyShortApp");
		assertEquals(1, apps.size());

		assertThat(apps).extracting(AppEntity::getShortName).containsOnly("MyShortApp");
	}

	@Test
	public void test_findByAppId() {

		List<AppEntity> apps = appRepository.findByAppId("123");
		assertEquals(1, apps.size());

		assertThat(apps).extracting(AppEntity::getAppId).containsOnly("123");
	}

	@Test
	public void test_findByContact() {

		List<AppEntity> apps = appRepository.findByContact("%gmail%");
		assertEquals(1, apps.size());

		assertThat(apps).extracting(AppEntity::getContact).contains("abc@gmail.com");
	}

	// Reversion Test
	@SuppressWarnings("unchecked")
	@Test
	public void test_reversion_findRevisions_byId() {
		TransactionStatus status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition(
				org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW));

		AppEntity app = appRepository.save(AppEntity.builder().appId("123").name("TestApp").build());

		this.entityManager.flush();
		this.platformTransactionManager.commit(status);

		AuditReader auditReader = AuditReaderFactory.get(entityManager.getEntityManager());

		@SuppressWarnings("rawtypes")
		List result = auditReader.createQuery().forRevisionsOfEntity(AppEntity.class, true, true).getResultList();
		assertThat(result).isNotEmpty();//

		TransactionStatus status2 = platformTransactionManager.getTransaction(new DefaultTransactionDefinition(
				org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW));
		Revisions<Integer, AppEntity> revisions = appRepository.findRevisions(app.getId());
		// @formatter:off
		
		assertThat(revisions).isNotEmpty();
		assertThat(revisions).hasSize(1);
		assertThat(revisions)
				.allSatisfy(revision -> assertThat(revision.getEntity()).extracting(AppEntity::getId, AppEntity::getName).containsExactly(app.getId(), app.getName()));
		// @formatter:on

		appRepository.deleteById(app.getId());
		this.entityManager.flush();
		this.platformTransactionManager.commit(status2);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_reversion_update_increasesRevisionNumber() {
		TransactionStatus status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition(
				org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW));

		AppEntity app = appRepository.save(AppEntity.builder().appId("1234").name("test app").build());

		appRepository.save(app);

		this.entityManager.flush();
		this.platformTransactionManager.commit(status);

		TransactionStatus status2 = platformTransactionManager.getTransaction(new DefaultTransactionDefinition(
				org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW));

		app.setName("test app2");
		appRepository.save(app);

		this.entityManager.flush();
		this.platformTransactionManager.commit(status2);

		AuditReader auditReader = AuditReaderFactory.get(entityManager.getEntityManager());

		@SuppressWarnings("rawtypes")
		List result = auditReader.createQuery().forRevisionsOfEntity(AppEntity.class, true, true).getResultList();
		assertThat(result).isNotEmpty();

		Optional<Revision<Integer, AppEntity>> revisions = appRepository.findLastChangeRevision(app.getId());
		// @formatter:off
		assertThat(revisions).isPresent()//.hasValueSatisfying(rev -> assertThat(rev.getRevisionNumber()).hasValue(2))
				.hasValueSatisfying(rev -> {
					assertThat(rev.getEntity()).extracting(AppEntity::getName).hasToString("test app2");
				});
		// More Reference:
		// https://www.codota.com/code/java/methods/org.assertj.core.api.OptionalAssert/hasValueSatisfying
		// @formatter:on
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_reversion_removeEntity_RevisionNumber_eq_2() {
		TransactionStatus status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition(
				org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW));

		AppEntity app = appRepository.save(AppEntity.builder().appId("1234").name("test app").build());

		appRepository.save(app);

		this.entityManager.flush();
		this.platformTransactionManager.commit(status);

		TransactionStatus status2 = platformTransactionManager.getTransaction(new DefaultTransactionDefinition(
				org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW));

		app.setName("test app2");
		appRepository.delete(app);

		this.entityManager.flush();
		this.platformTransactionManager.commit(status2);

		AuditReader auditReader = AuditReaderFactory.get(entityManager.getEntityManager());

		@SuppressWarnings("rawtypes")
		List result = auditReader.createQuery().forRevisionsOfEntity(AppEntity.class, true, true).getResultList();
		assertThat(result).isNotEmpty();

		platformTransactionManager.getTransaction(new DefaultTransactionDefinition(
				org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW));
		Revisions<Integer, AppEntity> revisions = appRepository.findRevisions(app.getId());

		assertThat(revisions).hasSize(2);
		assertThat(revisions.getLatestRevision().getEntity()) //
				.isNotNull() //
				.extracting(AppEntity::getName) //
				.isNull();
	}
}
