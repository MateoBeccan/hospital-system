package com.mycompany.hospital;

import com.mycompany.hospital.config.AsyncSyncConfiguration;
import com.mycompany.hospital.config.DatabaseTestcontainer;
import com.mycompany.hospital.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        HospitalApp.class,
        JacksonConfiguration.class,
        AsyncSyncConfiguration.class,
        com.mycompany.hospital.config.JacksonHibernateConfiguration.class,
    }
)
@ImportTestcontainers(DatabaseTestcontainer.class)
public @interface IntegrationTest {}
