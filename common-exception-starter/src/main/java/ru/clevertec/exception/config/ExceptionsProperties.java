package ru.clevertec.exception.config;


import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Slf4j
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "app.common.logging")
public class ExceptionsProperties {

    /**
     * to enable common logging aop
     */
    private boolean enabled;
    private String level;

    @PostConstruct
    void init() {
        log.info("logging properties initializers : {}" , this);
    }
}
