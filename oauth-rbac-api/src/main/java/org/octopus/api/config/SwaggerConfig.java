package org.octopus.api.config;

import java.util.ArrayList;
import java.util.Date;

import org.octopus.api.CommonProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
////@EnableSwagger2
@EnableSwagger2WebMvc
@Import({ SpringDataRestConfiguration.class })
//@Profile("!" + OctopusConstants.SPRING_PROFILE_PRODUCTION)
@Profile("swagger")
@Slf4j
public class SwaggerConfig {
	@Autowired
	private CommonProperties properties;

	@Bean
	public Docket api() {
		log.debug("Starting Swagger");
		StopWatch watch = new StopWatch();
		watch.start();

		ApiInfo apiInfo = new ApiInfo(//
				properties.getSwagger().getTitle(), //
				properties.getSwagger().getDescription(), //
				properties.getSwagger().getVersion(), //
				properties.getSwagger().getTermsOfServiceUrl(), //
				properties.getSwagger().getContact(), //
				properties.getSwagger().getLicense(), //
				properties.getSwagger().getLicenseUrl(), //
				new ArrayList<>());

		Docket docket = new Docket(DocumentationType.SWAGGER_2)//
				.apiInfo(apiInfo)//
				.genericModelSubstitutes(ResponseEntity.class)//
				.forCodeGeneration(true)//
				.genericModelSubstitutes(ResponseEntity.class)//
				.directModelSubstitute(org.joda.time.LocalDate.class, String.class)
				.directModelSubstitute(org.joda.time.LocalDateTime.class, Date.class)
				.directModelSubstitute(org.joda.time.DateTime.class, Date.class)
				.directModelSubstitute(java.time.LocalDate.class, String.class)
				.directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
				.directModelSubstitute(java.time.LocalDateTime.class, Date.class)//
				.select().paths(PathSelectors.regex(properties.getSwagger().getDefaultIncludePattern()))//
				.build();

		watch.stop();
		log.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());
		return docket;
	}
}