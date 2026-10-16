package com.example.usermanagement.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Configuration de la couche PERSISTANCE :
 *  - DataSource (HikariCP + H2)
 *  - EntityManagerFactory (JPA / Hibernate)
 *  - TransactionManager (JpaTransactionManager)
 *
 *  Aucun scan de composants ici : c'est le rôle de RootConfig.
 */

@Configuration
@PropertySource("classpath:db.properties")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.example.usermanagement.repository")
public class PersistenceConfig {

    // ═══════════════════════════════════════════════
    //  Injection des propriétés depuis db.properties
    // ═══════════════════════════════════════════════

    @Value("${jdbc.url}")
    private String jdbcUrl;

    @Value("${jdbc.driver}")
    private String jdbcDriver;

    @Value("${jdbc.username}")
    private String jdbcUsername;

    @Value("${jdbc.password}")
    private String jdbcPassword;

    @Value("${jdbc.pool.maxSize}")
    private int poolMaxSize;

    @Value("${jdbc.pool.minIdle}")
    private int poolMinIdle;

    @Value("${jdbc.pool.timeout}")
    private long poolTimeout;

    @Value("${hibernate.dialect}")
    private String hibernateDialect;

    @Value("${hibernate.hbm2ddl.auto}")
    private String hibernateHbm2ddl;

    @Value("${hibernate.show_sql}")
    private String hibernateShowSql;

    @Value("${hibernate.format_sql}")
    private String hibernateFormatSql;

    // ═══════════════════════════════════════════════
    //  1. DataSource (HikariCP + H2)
    // ═══════════════════════════════════════════════

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setDriverClassName(jdbcDriver);
        config.setUsername(jdbcUsername);
        config.setPassword(jdbcPassword);
        config.setMaximumPoolSize(poolMaxSize);
        config.setMinimumIdle(poolMinIdle);
        config.setConnectionTimeout(poolTimeout);
        return new HikariDataSource(config);
    }

    // ═══════════════════════════════════════════════
    //  2. EntityManagerFactory (JPA / Hibernate)
    // ═══════════════════════════════════════════════

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.example.usermanagement.entity");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties props = new Properties();
        props.setProperty("hibernate.dialect", hibernateDialect);
        props.setProperty("hibernate.hbm2ddl.auto", hibernateHbm2ddl);
        props.setProperty("hibernate.show_sql", hibernateShowSql);
        props.setProperty("hibernate.format_sql", hibernateFormatSql);
        emf.setJpaProperties(props);

        return emf;
    }

    // ═══════════════════════════════════════════════
    //  3. TransactionManager
    // ═══════════════════════════════════════════════

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}