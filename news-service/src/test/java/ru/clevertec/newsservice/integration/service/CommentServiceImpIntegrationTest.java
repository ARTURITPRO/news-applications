package ru.clevertec.newsservice.integration.service;

import com.github.tomakehurst.wiremock.client.WireMock;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import ru.clevertec.newsservice.dao.Comment;
import ru.clevertec.newsservice.dto.CommentDTO;
import ru.clevertec.newsservice.integration.PostgreSQLContainerIntegrationTest;
import ru.clevertec.newsservice.integration.util.WireMockExtension;
import ru.clevertec.newsservice.integration.util.WireMockUtil;
import ru.clevertec.newsservice.service.impl.CommentServiceImpl;

import java.io.IOException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ru.clevertec.newsservice.util.Constants.*;
import static ru.clevertec.newsservice.util.TestData.getCommentDTO;

@Profile("test")
@Tag("Integration test")
@RequiredArgsConstructor
@ExtendWith(WireMockExtension.class)
class CommentServiceImpIntegrationTest extends PostgreSQLContainerIntegrationTest {

    @Autowired
    private CommentServiceImpl commentService;

    @Test
    void findAllCommentWithPagingAndSorting() {

        List<Comment> resultList = commentService.findAll("%S%", PAGE_NO, PAGE_SIZE, SORTING);
        assertThat(resultList.size()).isEqualTo(6);
    }

    @Test
    void findByIdComment() {

        Comment comment = commentService.findById(ID);
        assertThat(comment.getId()).isEqualTo(ID);
        assertThat(comment.getText()).isEqualTo("I agree, completely!");
        assertThat(comment.getUserName()).isEqualTo("Elena");
    }

    @Test
    void findByIdNonExistentComment() {

        assertThatThrownBy(() -> commentService.findById(220L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findAllCommentsByNewsId() {

        List<Comment> commentList = commentService.findAllByNewsId(ID,
                PAGE_NO, PAGE_SIZE, SORTING);
        assertThat(commentList.size()).isEqualTo(10);
    }

    @Test
    void findAllCommentsByNewsIdShouldThrowEntityNotFoundException() {

        assertThatThrownBy(() -> commentService.findAllByNewsId(66L, PAGE_NO, PAGE_SIZE, SORTING))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void saveComment() throws IOException {

        stubFor(WireMock.get(urlPathMatching("/api/auth/user/" + TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(WireMockUtil.buildUserAdminResponse())));

        CommentDTO commentDto = getCommentDTO();
        Comment createdComment = commentService.save(ID, commentDto, TOKEN);
        assertThat(createdComment.getText()).isEqualTo("Text1");
    }

    @Test
    void updateComment() throws IOException {

        stubFor(WireMock.get(urlPathMatching("/api/auth/user/" + TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(WireMockUtil.buildUserAdminResponse())));

        CommentDTO commentDto = getCommentDTO();
        Comment result = commentService.update(ID, commentDto, TOKEN);
        assertThat(result.getText()).isEqualTo("Text1");
    }

    @Test
    void updateNonExistentCommentShouldThrowEntityNotFoundException() throws IOException {

        stubFor(WireMock.get(urlPathMatching("/api/auth/user/" + TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(WireMockUtil.buildUserAdminResponse())));

        assertThatThrownBy(() -> commentService.update(230L, getCommentDTO(), TOKEN))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deleteCommentById() throws IOException {

        stubFor(WireMock.get(urlPathMatching("/api/auth/user/" + TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(WireMockUtil.buildUserAdminResponse())));

        Integer sizeBefore = commentService.findAll(SEARCH_STR, 0, 200, SORTING).size();
        commentService.delete(20L, TOKEN);
        Integer sizeAfter = commentService.findAll(SEARCH_STR, 0, 200, SORTING).size();
        assertThat(sizeAfter).isLessThan(sizeBefore);
    }

    @Test
    void deleteNonExistentCommentShouldThrowEntityNotFoundException() throws IOException {

        stubFor(WireMock.get(urlPathMatching("/api/auth/user/" + TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(WireMockUtil.buildUserAdminResponse())));

        assertThatThrownBy(() -> commentService.delete(230L, TOKEN))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
