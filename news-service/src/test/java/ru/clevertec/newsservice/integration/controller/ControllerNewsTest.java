package ru.clevertec.newsservice.integration.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import ru.clevertec.newsservice.controller.NewsController;
import ru.clevertec.newsservice.dto.NewsDTO;
import ru.clevertec.newsservice.integration.PostgreSQLContainerIntegrationTest;
import ru.clevertec.newsservice.integration.util.WireMockExtension;
import ru.clevertec.newsservice.integration.util.WireMockUtil;

import java.io.IOException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;
import static ru.clevertec.newsservice.util.Constants.*;
import static ru.clevertec.newsservice.util.TestData.getNewsDto;

@ActiveProfiles("test")
@Tag("Integration test")
@RequiredArgsConstructor
@ExtendWith(WireMockExtension.class)
@TestConstructor(autowireMode = ALL)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerNewsTest extends PostgreSQLContainerIntegrationTest {

    private  final NewsController  newsController;

    @Test
    void findNewsById() {

        ResponseEntity<NewsDTO> response = newsController.findById(ID);
        NewsDTO newsDTO = response.getBody();
        assertNotNull(newsDTO);
        assertEquals(ID, newsDTO.getId());
    }

    @Test
    void findAllNews() {

        ResponseEntity<List<NewsDTO>> response = newsController.findAll(null, 0, 20, "id");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<NewsDTO> newsDTO = response.getBody();
        assertNotNull(newsDTO);
        assertEquals(ID, newsDTO.get(0).getId());
        assertEquals(20, newsDTO.get(19).getId());
    }

    @Test
    void saveNews() throws IOException {

        stubFor(WireMock.get(urlPathMatching("/api/auth/user/" + TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(WireMockUtil.buildUserAdminResponse())));

        ResponseEntity<NewsDTO> response = newsController.create(getNewsDto(), TOKEN);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        NewsDTO newsDTO = response.getBody();
        assertNotNull(newsDTO);
        assertEquals(21, newsDTO.getId());
        assertEquals(TEXT_1, newsDTO.getText());
    }

    @Test
    void updateNews() throws IOException {

        stubFor(WireMock.get(urlPathMatching("/api/auth/user/" + TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(WireMockUtil.buildUserAdminResponse())));

        ResponseEntity<NewsDTO> response = newsController.update(1L, getNewsDto(), TOKEN);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        NewsDTO newsDTO = response.getBody();
        assertNotNull(newsDTO);
        assertEquals(1, newsDTO.getId());
        assertEquals(TEXT_1, newsDTO.getText());
    }

    @Test
    void deleteNews() throws IOException {

        stubFor(WireMock.get(urlPathMatching("/api/auth/user/" + TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(WireMockUtil.buildUserAdminResponse())));

        newsController.delete(20L, TOKEN);
        ResponseEntity<List<NewsDTO>> response = newsController.findAll(null, 0, 20, "id");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<NewsDTO> newsDTOList = response.getBody();
        assertNotNull(newsDTOList);
        assertEquals(19, newsDTOList.size());
    }

}
