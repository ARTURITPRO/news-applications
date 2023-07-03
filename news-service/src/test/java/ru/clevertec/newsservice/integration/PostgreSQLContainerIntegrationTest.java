package ru.clevertec.newsservice.integration;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.clevertec.newsservice.NewsServiceApplication;

@Sql({"classpath:db/script.sql"})
@ActiveProfiles("test")
@Transactional
@SpringBootTest(classes = NewsServiceApplication.class)
public abstract class PostgreSQLContainerIntegrationTest {

    private static final String DOCKER_IMAGE = "postgres:latest";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>(DOCKER_IMAGE)
            .withUsername(USERNAME)
            .withPassword(PASSWORD);

    @BeforeAll
    static void init() {
        container.start();
    }

    @DynamicPropertySource
    static void setup(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }
}