package com.logViz.dataservice.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.TimeZone;

@Configuration
@PropertySource("classpath:queries.properties")
public class ApplicationConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer init() {
        return builder -> builder.timeZone(TimeZone.getDefault());
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        return builder
                .setConnectTimeout(Duration.ofMillis(60000))
                .setReadTimeout(Duration.ofMillis(60000))
                .build();
    }

}
