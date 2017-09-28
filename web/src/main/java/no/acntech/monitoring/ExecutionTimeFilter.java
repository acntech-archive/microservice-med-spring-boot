package no.acntech.monitoring;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter som logger total responstid for hver request
 */
public class ExecutionTimeFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionTimeFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        filterChain.doFilter(request, response);
        long elapsed = System.currentTimeMillis() - startTime;
        LOGGER.debug("elapsed_time_ms={}", elapsed);
    }
}
