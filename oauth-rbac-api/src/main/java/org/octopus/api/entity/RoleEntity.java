package org.octopus.api.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;
import org.octopus.api.entity.audit.AbstractAuditEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = { "user2roles" })

@Entity(name = "Role")
@Table(name = "oauth_rbac_role")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Audited
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditEntity.class, isAudited = true), })
public class RoleEntity extends AbstractAuditEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8806995635619017025L;

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@GeneratedValue(generator = "system-uuid")
	private String id;

	private String rolename;

	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
	@JsonIgnore
	Set<User2RoleEntity> user2roles;
}
