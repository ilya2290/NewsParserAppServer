/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */

package com.example.newsparser.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for defining beans used in the application.
 */
@Configuration(proxyBeanMethods = false)
public class Config {

    /**
     * Configures and provides an ObjectMapper bean.
     *
     * @return A configured ObjectMapper instance with support for Java 8 date and time API.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper;
    }

    /**
     * Configures and provides a RestTemplate bean.
     *
     * @return A new RestTemplate instance for making REST API calls.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
