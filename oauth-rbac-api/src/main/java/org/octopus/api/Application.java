package org.octopus.api;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.octopus.OctopusConstants;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class Application implements InitializingBean {

	private final Environment env;

	public Application(Environment env) {
		this.env = env;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
		if (activeProfiles.contains("dev") && activeProfiles.contains("prod")) {
			log.error("You have misconfigured your application! It should not run "
					+ "with both the 'dev' and 'prod' profiles at the same time.");
		}
		if (activeProfiles.contains("dev") && activeProfiles.contains("cloud")) {
			log.error("You have misconfigured your application! It should not "
					+ "run with both the 'dev' and 'cloud' profiles at the same time.");
		}
	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		addDefaultProfile(app);
		Environment env = app.run(args).getEnvironment();
		logApplicationStartup(env);
	}

	private static void addDefaultProfile(SpringApplication app) {
		Map<String, Object> defProperties = new HashMap<>();
		defProperties.put(OctopusConstants.SPRING_PROFILE_DEFAULT, OctopusConstants.SPRING_PROFILE_DEVELOPMENT);
		app.setDefaultProperties(defProperties);
	}

	private static void logApplicationStartup(Environment env) {
		String protocol = "http";
		String serverPort = env.getProperty("server.port");
		String contextPath = getContextPath(env);
		String hostAddress = getHostAddress();
		String applicationName = env.getProperty("spring.application.name");
		log.info(
				"\n----------------------------------------------------------\n\t"
						+ "Application '{}' is running! Access URLs:\n\t"//
						+ "Local: \t\t{}://localhost:{}{}\n\t"//
						+ "External: \t{}://{}:{}{}\n\t"
						+ "Profile(s): \t{}\n----------------------------------------------------------",
				applicationName, 
				protocol, 
				serverPort, 
				contextPath, 
				protocol, 
				hostAddress, 
				serverPort, 
				contextPath,
				env.getActiveProfiles());
	}

	private static String getHostAddress() {
		String hostAddress = "localhost";
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			log.warn("The host name could not be determined, using `localhost` as fallback");
		}
		return hostAddress;
	}

	private static String getContextPath(Environment env) {
		String contextPath = env.getProperty("server.servlet.context-path");
		if (StringUtils.isBlank(contextPath)) {
			contextPath = "/";
		}
		return contextPath;
	}

}
