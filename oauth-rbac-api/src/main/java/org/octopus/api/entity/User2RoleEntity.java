package org.octopus.api.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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

@Entity(name = "User2Role")
@Table(name = "oauth_rbac_user2role")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Audited
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditEntity.class, isAudited = true), })
public class User2RoleEntity extends AbstractAuditEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5048698585781365030L;

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@GeneratedValue(generator = "system-uuid")
	private String id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@ManyToOne
	@JoinColumn(name = "role_id")
	private RoleEntity role;
}
