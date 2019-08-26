package com.cenkc.lxftcs.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * created by cenkc on 8/26/2019
 */
@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper getStandartObjMapper() {
        return new ObjectMapper();
    }

    @Bean("customObjectMapper")
    public ObjectMapper getCustomObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return objectMapper;
    }
}
