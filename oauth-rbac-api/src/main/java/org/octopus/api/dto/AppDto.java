package org.octopus.api.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * Why need DTO - it can help hidden domain details
 * https://stackoverflow.com/questions/39397147/difference-between-entity-and-dto
 * 
 * @author joshualeng
 *
 */
public class AppDto {
	private String id;

	@Size(min = 0, max = 6)
	private String appId;

	@Size(min = 0, max = 10)
	private String shortName;

	@Size(min = 2, max = 128)
	@NotEmpty(message = "Please provide a name")
	private String name;

	@Size(min = 0, max = 256)
	private String contact;
}
