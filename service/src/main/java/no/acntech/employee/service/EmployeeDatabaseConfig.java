package no.acntech.employee.service;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
@EntityScan(basePackageClasses = {Jsr310JpaConverters.class},
        basePackages = "no.acntech.employee.domain")
public class EmployeeDatabaseConfig {

}
