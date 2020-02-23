package org.octopus.api.entity.identity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor

@MappedSuperclass
/**
 * 
 * This is suitable for all type of data base But it's low performance with
 * lock, you should never use it <br>
 * https://vladmihalcea.com/why-you-should-never-use-the-table-identifier-generator-with-jpa-and-hibernate/
 * 
 * @author joshualeng
 *
 */
public abstract class AbstractIdentityTableEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private String id;
}
