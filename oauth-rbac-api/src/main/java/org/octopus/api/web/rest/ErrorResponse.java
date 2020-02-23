package org.octopus.api.web.rest;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
	public ErrorResponse(String message, List<String> details) {
		super();
		this.message = message;
		this.details = details;
	}

	// General error message about nature of error
	private String message;

	// Specific errors in API request processing
	private List<String> details;
}
