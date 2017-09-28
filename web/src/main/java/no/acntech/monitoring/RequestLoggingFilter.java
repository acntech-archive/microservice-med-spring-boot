package no.acntech.monitoring;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

public class RequestLoggingFilter extends AbstractRequestLoggingFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingFilter.class); // NOSONAR

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        LOGGER.debug("request=[method={};{}", request.getMethod(), message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        // NOSONAR
    }

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return LOGGER.isDebugEnabled();
    }
}
