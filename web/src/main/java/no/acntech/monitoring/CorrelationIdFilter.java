package no.acntech.monitoring;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter som henter ut korrelasjons-id fra HTTP-headeren "x-correlation-id" setter den på {@link MDC}
 * slik at den kommer med i alle logginnslag relatert til requesten.
 * <p>
 * Hvis headeren ikke er satt genereres det en unik id.
 */
public class CorrelationIdFilter extends OncePerRequestFilter {

    private static final String CORRELATION_ID_MDC_ATTRIBUTE = "CORR_ID_ATTR";
    private static final String CORRELATION_ID_HTTP_HEADER = "x-correlation-id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String correlationId = getCorrelationId(request);
        MDC.put(CORRELATION_ID_MDC_ATTRIBUTE, correlationId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(CORRELATION_ID_MDC_ATTRIBUTE);
        }
    }

    private String getCorrelationId(HttpServletRequest request) {
        Optional<String> correlationId = Optional.ofNullable(request.getHeader(CORRELATION_ID_HTTP_HEADER));
        return correlationId.orElse(UUID.randomUUID().toString());
    }
}

