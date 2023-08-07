package ru.clevertec.newsservice.integration.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import ru.clevertec.newsservice.controller.CommentController;
import ru.clevertec.newsservice.dto.CommentDTO;
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
import static ru.clevertec.newsservice.util.TestData.getCommentDTO;

@ActiveProfiles("test")
@Tag("Integration test")
@RequiredArgsConstructor
@ExtendWith(WireMockExtension.class)
@TestConstructor(autowireMode = ALL)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerCommentTest extends PostgreSQLContainerIntegrationTest {

   private final TestRestTemplate restTemplate;
    private final CommentController commentController;

    @LocalServerPort
    private int port;

    @Test
    void findPersonByIdComment() {

      //  ResponseEntity<CommentDTO> response = commentController.findById(ID);
        ResponseEntity<CommentDTO> response = restTemplate.getForEntity("http://localhost:" + port + "/news_applications/v1/comment/1", CommentDTO.class);
        CommentDTO comment = response.getBody();
        assertNotNull(comment);
        assertEquals(ID, comment.getId());
    }

    @Test
    void findAllComment() {

        ResponseEntity<List<CommentDTO>> response = commentController.findAll(null, 0, 200, "id");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<CommentDTO> comment = response.getBody();
        assertNotNull(comment);
        assertEquals(ID, comment.get(0).getId());
        assertEquals(200, comment.get(199).getId());
    }

    @Test
    void findAllCommentsByNewsId() throws Exception {

        ResponseEntity<List<CommentDTO>> response = commentController
                .findAllCommentsByNewsId(ID, 0, 20, "id");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<CommentDTO> comment = response.getBody();
        assertNotNull(comment);
        assertEquals(10, comment.size());
    }

    @Test
    void saveComment() throws IOException {

        stubFor(WireMock.get(urlPathMatching("/api/auth/user/" + TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(WireMockUtil.buildUserAdminResponse())));

        ResponseEntity<CommentDTO> response = commentController.create(2L, getCommentDTO(), TOKEN);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        CommentDTO comment = response.getBody();
        assertNotNull(comment);
        assertEquals(201, comment.getId());
        assertEquals(TEXT_1, comment.getText());
    }

    @Test
    void updateComment() throws IOException {

        stubFor(WireMock.get(urlPathMatching("/api/auth/user/" + TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(WireMockUtil.buildUserAdminResponse())));

        ResponseEntity<CommentDTO> response = commentController.update(1L, getCommentDTO(), TOKEN);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        CommentDTO comment = response.getBody();
        assertNotNull(comment);
        assertEquals(1, comment.getId());
        assertEquals(TEXT_1, comment.getText());
    }

    @Test
    void deleteComment() throws IOException {

        stubFor(WireMock.get(urlPathMatching("/api/auth/user/" + TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(WireMockUtil.buildUserAdminResponse())));

        commentController.delete(200L, TOKEN);
        ResponseEntity<List<CommentDTO>> response = commentController.findAll(null, 0, 200, "id");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<CommentDTO> comment = response.getBody();
        assertNotNull(comment);
        assertEquals(199, comment.size());
    }

}
