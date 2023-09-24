package com.demo.demandfarm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class RequestLoggingConfig {
    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(true);
        filter.setBeforeMessagePrefix("REQUEST STARTED: ");
        filter.setAfterMessagePrefix("REQUEST ENDED : ");
        filter.setIncludeClientInfo(true);
        filter.setHeaderPredicate(header -> !header.equalsIgnoreCase("Authorization"));
        return filter;
    }
}
