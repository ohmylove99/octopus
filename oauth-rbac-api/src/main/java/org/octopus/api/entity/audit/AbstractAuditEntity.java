package org.octopus.api.entity.audit;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@CreatedBy
	@Column(name = "created_by", nullable = true, length = 50, updatable = false)
	protected String createdBy;

	@CreatedDate
	@Column(name = "created_date", updatable = false)
	//@Temporal(TemporalType.TIMESTAMP)
	protected Instant createdDate = Instant.now();

	@LastModifiedBy
	@Column(name = "last_modified_by", length = 50)
	protected String lastModifiedBy;

	@LastModifiedDate
	@Column(name = "last_modified_date")
	//@Temporal(TemporalType.TIMESTAMP)
	protected Instant lastModifiedDate = Instant.now();

	//@Column(name = "version")
	//@Version
	//protected Long version;
}
