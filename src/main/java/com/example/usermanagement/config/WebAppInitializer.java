package com.example.usermanagement.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;

/**
 * Bootstrap de l'application — remplace web.xml.
 *
 * Tomcat détecte automatiquement cette classe au démarrage et appelle onStartup().
 *
 * 1. Crée le ROOT CONTEXT (métier)  → RootConfig
 * 2. Crée le WEB CONTEXT (web)      → WebConfig (enfant du Root)
 * 3. Enregistre le DispatcherServlet sur "/"
 */

public class WebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) {

        // ═══════════════════════════════════════════════
        //  1. ROOT CONTEXT (métier)
        //     → services, repositories, mapper, persistance
        // ═══════════════════════════════════════════════

        AnnotationConfigWebApplicationContext rootContext =
                new AnnotationConfigWebApplicationContext();
        rootContext.register(RootConfig.class);

        // Lie le cycle de vie du RootContext à l'application
        servletContext.addListener(new ContextLoaderListener(rootContext));

        // ═══════════════════════════════════════════════
        //  2. WEB CONTEXT (web)
        //     → controllers, Spring MVC
        //     → enfant du RootContext
        // ═══════════════════════════════════════════════

        AnnotationConfigWebApplicationContext webContext =
                new AnnotationConfigWebApplicationContext();
        webContext.register(WebConfig.class);
        webContext.setParent(rootContext);   // ← HIÉRARCHIE CRUCIALE

        // ═══════════════════════════════════════════════
        //  3. DispatcherServlet sur "/"
        // ═══════════════════════════════════════════════

        DispatcherServlet dispatcherServlet = new DispatcherServlet(webContext);

        ServletRegistration.Dynamic registration =
                servletContext.addServlet("dispatcher", dispatcherServlet);

        registration.setLoadOnStartup(1);
        registration.addMapping("/");
    }
}