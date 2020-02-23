package org.octopus.api.config;

import java.text.SimpleDateFormat;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is bean of model mapper
 * 
 * @author joshualeng
 *
 */
@Configuration
public class MapperConfig {
	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(SIMPLE_DATE_FORMAT);
		return mapper;
	}
}
