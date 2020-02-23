package org.octopus.api.config.jpa;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

//if you are using Spring Security, then use it to find the currently logged-in user. 
public class AuditorAwareImpl implements AuditorAware<String> {
	@Override
	public Optional<String> getCurrentAuditor() {
		return Optional.of("SYSTEM");
		// '/*-' used to suppress the formatting of specific block comments
		/*-
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
		  return null;
		}
		return ((MyUserDetails) authentication.getPrincipal()).getUser();
		*/
	}
}