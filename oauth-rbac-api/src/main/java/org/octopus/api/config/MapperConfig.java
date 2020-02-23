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

	/**
	 * By default mm uses MatchingStrategies.STANDARD which is dangerous. It can
	 * easily choose the wrong mapping and cause strange, hard to find bugs. And
	 * what if next year someone else adds a new column to the database? So don't do
	 * it. Make sure you use STRICT mode:
	 * http://modelmapper.org/user-manual/configuration/
	 * 
	 * @return
	 */
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		//modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(SIMPLE_DATE_FORMAT);
		return mapper;
	}
}
