package ru.clevertec.logging.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.logging.aspect.LoggingJPARepositoryAndServiceLayer;
import ru.clevertec.logging.aspect.ControllerLogAspect;

@Slf4j
@Configuration
@EnableConfigurationProperties(LoggingProperties.class)
@ConditionalOnClass(LoggingProperties.class)
@ConditionalOnProperty(prefix = "app.common.logging", name = "enabled", havingValue = "true")
public class LoggingAutoConfiguration {

    @PostConstruct
    void init(){
        log.info("LoggingAutoConfiguration initialized");
    }

    @Bean
    LoggingJPARepositoryAndServiceLayer applicationLogAspect(){
        return new LoggingJPARepositoryAndServiceLayer();
    }

    @Bean
    ControllerLogAspect controllerLogAspect(){
        return new ControllerLogAspect();
    }
}
