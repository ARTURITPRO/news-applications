package ru.clevertec.newsservice.unit.mapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.clevertec.newsservice.dto.CommentDTO;
import ru.clevertec.newsservice.dao.Comment;
import ru.clevertec.newsservice.mapper.CommentMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.clevertec.newsservice.util.TestData.*;

class MapperCommentTest {

    private static CommentMapper commentMapper;

    private Comment comment = getComment();
    private CommentDTO commentDto = getCommentDTO();

    @BeforeAll
    static void CreateCommentMapper() {
        commentMapper = new CommentMapper();
    }

    @Test
    void DtoCommentToNews() {
        Comment actual = commentMapper.commentDTOToComment(commentDto);
        assertThat(actual.getText()).isEqualTo(commentDto.getText());
    }

    @Test
    void CommentToCommentDto() {
        CommentDTO actual = commentMapper.commentToCommentDTO(comment);
        assertThat(actual.getId()).isEqualTo(comment.getId());
        assertThat(actual.getText()).isEqualTo(comment.getText());
        assertThat(actual.getUserName()).isEqualTo(comment.getUserName());
    }
}
