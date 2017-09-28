package no.acntech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

import no.acntech.monitoring.CorrelationIdFilter;
import no.acntech.monitoring.ExecutionTimeFilter;
import no.acntech.monitoring.RequestLoggingFilter;

@SpringBootApplication
public class EmployeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean correlationIdFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new CorrelationIdFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

    @Bean
    public FilterRegistrationBean executionTimeFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new ExecutionTimeFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean requestLoggingFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        RequestLoggingFilter filter = new RequestLoggingFilter();
        filter.setBeforeMessagePrefix("");
        filter.setIncludeQueryString(true);
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        return registration;
    }
}
