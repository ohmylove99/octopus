package org.octopus.api.dto;

import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
/**
 * Why need DTO - it can help hidden domain details
 * https://stackoverflow.com/questions/39397147/difference-between-entity-and-dto
 * 
 * @author joshualeng
 *
 */
public class RoleDto {
	private String id;

	@Size(min = 2, max = 128)
	@NotEmpty(message = "Please provide a username")
	private String rolename;
	// @JsonIgnore
	private Set<UserDto> users;
}
