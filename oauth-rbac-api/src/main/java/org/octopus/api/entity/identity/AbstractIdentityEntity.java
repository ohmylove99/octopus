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
 * This is suitable for DB2, MySQL, MS SQL Server, Sybase and HypersonicSQL
 * database Reference:
 * https://docs.jboss.org/hibernate/orm/3.5/reference/en/html/mapping.html#mapping-declaration-id
 * 
 * @author joshualeng
 *
 */
public abstract class AbstractIdentityEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
}
