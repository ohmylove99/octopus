package org.octopus;

public interface OctopusDefaults {
	interface Swagger {
		String TITLE = "Application API";
		String DESCRIPTION = "API documentation";
		String VERSION = "0.0.1";
		String TERMS_OF_SERVICEURL = null;
		String CONTACT_NAME = null;
		String CONTACT_URL = null;
		String CONTACT_EMAIL = null;
		String LICENSE = null;
		String LICENSE_URL = null;
		String DEFAULT_INCLUDE_PATTERN = "/api/.*";
		String HOST = null;
		String[] PROTOCOLS = {};
		boolean USE_DEFAULT_RESPONSE_MESSAGES = true;
	}
}
