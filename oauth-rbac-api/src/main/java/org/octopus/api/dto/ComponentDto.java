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
public class ComponentDto {
	private String id;

	@Size(min = 2, max = 128)
	@NotEmpty(message = "Please provide a name")
	private String name;

	@Size(min = 0, max = 256)
	private String description;

	@Size(min = 0, max = 256)
	private String contact;
}
