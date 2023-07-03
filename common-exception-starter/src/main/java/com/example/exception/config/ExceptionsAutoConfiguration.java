package com.example.exception.config;

import com.example.exception.aspect.handler.RestExceptionHandler;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
@EnableConfigurationProperties(ExceptionsProperties.class)
@ConditionalOnClass(ExceptionsProperties.class)
@ConditionalOnProperty(prefix = "app.common.logging", name = "enabled", havingValue = "true")
public class ExceptionsAutoConfiguration {

    @PostConstruct
    void init(){
        log.info("ExceptionsAutoConfiguration initialized");
    }

}
