package com.example.usermanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

/**
 * Contexte MÉTIER (parent) :
 *  - Scanne les @Service, @Repository, @Component
 *  - Exclut les @Controller (gérés par WebConfig)
 *  - Importe la configuration de persistance
 *  - Fournit le bean PasswordEncoder (BCrypt)
 */


@Configuration
@ComponentScan(
        basePackages = "com.example.usermanagement",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = Controller.class
        )
)
@Import(PersistenceConfig.class)
public class RootConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}