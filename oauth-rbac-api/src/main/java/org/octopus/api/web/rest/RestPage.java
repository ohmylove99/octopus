package org.octopus.api.web.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * This class to resolve the issue of restTemplate to deserialize issue <br>
 * PageImpl is not designed for (de-)serialization as that would couple its
 * representation to the code and prevent us from being able to make changes to
 * the type that changes the serialization format. Also, the Spring Data core
 * project doesn't even define requirements about the way that serialization
 * should look and is heavily dependent on user-specific API conventions
 * 
 *
 * @param <T>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestPage<T> extends PageImpl<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3245114960399576551L;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public RestPage(@JsonProperty("content") List<T> content, //
			@JsonProperty("number") int number, //
			@JsonProperty("size") int size, //
			@JsonProperty("totalElements") Long totalElements, //
			@JsonProperty("pageable") JsonNode pageable, //
			@JsonProperty("last") boolean last, //
			@JsonProperty("totalPages") int totalPages, //
			@JsonProperty("sort") JsonNode sort, //
			@JsonProperty("first") boolean first, //
			@JsonProperty("numberOfElements") int numberOfElements) {

		super(content, PageRequest.of(number, size), totalElements);
	}

	public RestPage(List<T> content, Pageable pageable, long total) {
		super(content, pageable, total);
	}

	public RestPage(List<T> content) {
		super(content);
	}

	public RestPage() {
		super(new ArrayList<>());
	}
}