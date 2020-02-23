package org.octopus.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;
import org.octopus.api.entity.audit.AbstractAuditEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)

@Entity(name = "Component")
@Table(name = "oauth_rbac_component")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Audited
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditEntity.class, isAudited = true), })
public class ComponentEntity extends AbstractAuditEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3332811595945326825L;
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@GeneratedValue(generator = "system-uuid")
	private String id;
	

	@Column(length = 128)
	@Size(min = 3, max = 20)
	@NotEmpty(message = "Please provide a name")
	private String name;

	@Column(length = 256)
	private String description;

	@Column(length = 256)
	private String contact;
}
