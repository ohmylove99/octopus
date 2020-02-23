package org.octopus.api;

import org.octopus.OctopusDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import springfox.documentation.service.Contact;

/**
 * 
 * @author joshualeng
 *
 */
@Configuration
@ConfigurationProperties(prefix = "common", ignoreUnknownFields = false)
public class CommonProperties {
	private final Swagger swagger = new Swagger();

	public Swagger getSwagger() {
		return swagger;
	}

	@Getter
	@Setter
	@ToString
	public static class Swagger {
		private String title = OctopusDefaults.Swagger.TITLE;
		private String description = OctopusDefaults.Swagger.DESCRIPTION;
		private String version = OctopusDefaults.Swagger.VERSION;
		private String termsOfServiceUrl = OctopusDefaults.Swagger.TERMS_OF_SERVICEURL;
		private String contactName = OctopusDefaults.Swagger.CONTACT_NAME;
		private String contactUrl = OctopusDefaults.Swagger.CONTACT_URL;
		private String contactEmail = OctopusDefaults.Swagger.CONTACT_EMAIL;

		private String license = OctopusDefaults.Swagger.LICENSE;
		private String licenseUrl = OctopusDefaults.Swagger.LICENSE_URL;
		private String defaultIncludePattern = OctopusDefaults.Swagger.DEFAULT_INCLUDE_PATTERN;
		private String host = OctopusDefaults.Swagger.HOST;
		private String[] protocols = OctopusDefaults.Swagger.PROTOCOLS;
		private boolean useDefaultResponseMessages = OctopusDefaults.Swagger.USE_DEFAULT_RESPONSE_MESSAGES;

		public Contact getContact() {
			Contact contact = new Contact(getContactName(), getContactUrl(), getContactEmail());
			return contact;
		}
	}
}
