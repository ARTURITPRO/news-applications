package ru.clevertec.newsservice.mapper;

import org.springframework.stereotype.Component;
import ru.clevertec.newsservice.dao.Comment;
import ru.clevertec.newsservice.dto.CommentDTO;

/**
 * <d>This class is used for mapping CommentDTO and Comment.</d>
 *
 * @author Artur Malashkov
 * @since 17
 */
@Component
public class CommentMapper {

    public CommentDTO commentToCommentDTO(Comment comment) {
        return CommentDTO.builder().id(comment.getId()).text(comment.getText()).userName(comment.getUserName()).build();
    }

    public Comment commentDTOToComment(CommentDTO commentDTO) {

        return Comment.builder().text(commentDTO.getText()).userName(commentDTO.getUserName()).build();
    }

}
