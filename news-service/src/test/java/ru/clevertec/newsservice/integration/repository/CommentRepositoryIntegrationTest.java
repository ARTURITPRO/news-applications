package ru.clevertec.newsservice.integration.repository;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.clevertec.newsservice.dao.Comment;
import ru.clevertec.newsservice.integration.PostgreSQLContainerIntegrationTest;
import ru.clevertec.newsservice.repository.CommentRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.clevertec.newsservice.util.Constants.*;
import static ru.clevertec.newsservice.util.TestData.getComment;

@Tag("Integration test")
class CommentRepositoryIntegrationTest extends PostgreSQLContainerIntegrationTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void findAllCommentWithPagingAndSorting() {
        Pageable paging = PageRequest.of(PAGE_NO, PAGE_SIZE, Sort.by(SORTING));
        Page<Comment> pagedResult = commentRepository.findAll(paging);
        assertThat(pagedResult.getContent().size()).isEqualTo(10);
    }

    @Test
    void findByIdComment() {
        Optional<Comment> commentData = commentRepository.findById(ID);
        assertThat(commentData.get().getId()).isEqualTo(ID);
        assertThat(commentData.get().getText()).isEqualTo("I agree, completely!");
        assertThat(commentData.get().getUserName()).isEqualTo("Elena");
    }

    @Test
    void findByIdNonExistentComment() {
        Optional<Comment> commentData = commentRepository.findById(250L);
        assertThat(commentData).isEmpty();
    }

    @Test
    void findAllCommentsByNewsId() {
        Page<Comment> pagedResult = commentRepository.findAllByNewsId(ID,
                PageRequest.of(PAGE_NO, PAGE_SIZE, Sort.by(SORTING)));
        assertThat(pagedResult.getContent().size()).isEqualTo(10);
    }

    @Test
    void saveComment() {
        Comment comment = getComment();
        Comment createdComment = commentRepository.save(comment);
        assertThat(createdComment.getText()).isEqualTo("Text1");
        assertThat(createdComment.getUserName()).isEqualTo("User1");
    }

    @Test
    void updateComment() {
        Comment comment = getComment();
        commentRepository.save(comment);
        comment.setText("MyText");
        comment.setUserName("MyUserName");
        Comment updatedComment = commentRepository.save(comment);
        assertThat(updatedComment.getText()).isEqualTo("MyText");
        assertThat(updatedComment.getUserName()).isEqualTo("MyUserName");
    }

    @Test
    void deleteCommentById() {
        Integer sizeBefore = commentRepository.findAll(PageRequest.of(PAGE_NO, PAGE_SIZE + 190, Sort.by(SORTING)))
                .getContent().size();
        commentRepository.deleteById(20L);
        Integer sizeAfter = commentRepository.findAll(PageRequest.of(PAGE_NO, PAGE_SIZE + 190, Sort.by(SORTING)))
                .getContent().size();
        assertThat(sizeAfter).isLessThan(sizeBefore);
    }
}
