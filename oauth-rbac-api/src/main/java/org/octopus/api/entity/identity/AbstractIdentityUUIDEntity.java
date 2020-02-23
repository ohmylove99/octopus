package org.octopus.api.entity.identity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

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
 * This is suitable for all type of data base
 * 
 * @author joshualeng
 *
 */
public abstract class AbstractIdentityUUIDEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 348904867014938397L;
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@GeneratedValue(generator = "system-uuid")
	private String id;
}
