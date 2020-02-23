package org.octopus.api.metrics;

import javax.mail.MessagingException;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JavaMailHealthIndicator extends AbstractHealthIndicator {
	private JavaMailSenderImpl javaMailSender;

	public JavaMailHealthIndicator() {

	}

	public JavaMailHealthIndicator(JavaMailSenderImpl javaMailSender) {
		Assert.notNull(javaMailSender, "javaMailSender must not be null");
		this.javaMailSender = javaMailSender;
	}

	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		log.debug("Initializing JavaMail health indicator");
		try {
			javaMailSender//
					.getSession()//
					.getTransport()//
					.connect(javaMailSender.getHost(), //
							javaMailSender.getPort(), //
							javaMailSender.getUsername(), //
							javaMailSender.getPassword()//
					);
			builder.up().withDetail("email server", "UP");
		} catch (MessagingException e) {
			log.debug("Cannot connect to e-mail server. Error: {}", e.getMessage());
			builder.down().withDetail("email server", "DOWN:" + e.getMessage());
		} catch (Exception e1) {
			log.debug("Cannot connect to e-mail server. Error: {}", e1.getMessage());
			builder.down().withDetail("email server", "DOWN:" + e1.getMessage());
		}
	}
}
