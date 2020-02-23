package org.octopus.api.config;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

/**
 * This is fix for local with Chinese
 * @author joshualeng
 *
 */
@Configuration
public class LocaleValidatorConfig {
    @Bean
    LocaleResolver localeResolver() {
        // Force english for error messages
        return new FixedLocaleResolver(Locale.ENGLISH);
    }
}