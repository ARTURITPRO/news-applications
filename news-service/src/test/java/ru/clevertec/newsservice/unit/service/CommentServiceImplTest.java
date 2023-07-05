package ru.clevertec.newsservice.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.clevertec.exception.exceptions.PermissionException;
import ru.clevertec.exception.exceptions.ResourceNotFoundException;
import ru.clevertec.newsservice.client.dto.User;
import ru.clevertec.newsservice.dao.Comment;
import ru.clevertec.newsservice.dao.News;
import ru.clevertec.newsservice.dto.CommentDTO;
import ru.clevertec.newsservice.mapper.CommentMapper;
import ru.clevertec.newsservice.repository.CommentRepository;
import ru.clevertec.newsservice.repository.NewsRepository;
import ru.clevertec.newsservice.service.impl.CommentServiceImpl;
import ru.clevertec.newsservice.util.UserUtility;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ru.clevertec.newsservice.controller.constants.Constants.SORT_BY;
import static ru.clevertec.newsservice.util.Constants.*;
import static ru.clevertec.newsservice.util.TestData.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private UserUtility userUtility;

    @Mock
    CommentMapper commentMapper;

    @Test
    void checkFindAll() {

        Pageable paging = PageRequest.of(PAGE_NO, PAGE_SIZE, Sort.by(SORT_BY));

        Comment comment = getCommentList().get(0);

        doReturn(new PageImpl<>(getCommentList()))
                .when(commentRepository).findAll(paging);

        List<Comment> commentList = commentService.findAll(null, PAGE_NO, PAGE_SIZE, SORT_BY);

        verify(commentRepository).findAll(paging);

        assertThat(commentList.get(0).getId()).isEqualTo(comment.getId());
        assertThat(commentList.get(0).getText()).isEqualTo(comment.getText());
        assertThat(commentList.get(0).getUserName()).isEqualTo(comment.getUserName());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    void checkFindById(Long id) {
        Comment comment = getComment();

        doReturn(Optional.of(comment))
                .when(commentRepository).findById(id);

        Comment result = commentService.findById(id);

        verify(commentRepository).findById(anyLong());
        assertThat(result).isEqualTo(comment);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    void checkFindByIdShouldThrowResourceNotFoundException(Long id) {
        doThrow(ResourceNotFoundException.class)
                .when(commentRepository).findById(anyLong());
        assertThatThrownBy(() -> commentService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(commentRepository).findById(anyLong());
    }

    @Test
    void checkFindAllByNewsId() {
        Comment comment = getCommentList().get(0);

        doReturn(new PageImpl<>(getCommentList()))
                .when(commentRepository).findAllByNewsId(ID, PageRequest.of(PAGE_NO, PAGE_SIZE, Sort.by(SORT_BY)));
        doReturn(Boolean.TRUE)
                .when(newsRepository).existsById(ID);

        List<Comment> commentList = commentService.findAllByNewsId(ID,
                PAGE_NO, PAGE_SIZE, SORT_BY);

        verify(newsRepository).existsById(ID);
        verify(commentRepository).findAllByNewsId(ID, PageRequest.of(PAGE_NO,
                PAGE_SIZE, Sort.by(SORT_BY)));

        assertThat(commentList.get(0)).isEqualTo(comment);
        assertThat(commentList.get(0).getId()).isEqualTo(comment.getId());
        assertThat(commentList.get(0).getText()).isEqualTo(comment.getText());
        assertThat(commentList.get(0).getUserName()).isEqualTo(comment.getUserName());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    void checkFindAllByNewsIdShouldThrowResourceNotFoundException() {
        doThrow(ResourceNotFoundException.class)
                .when(newsRepository).existsById(anyLong());
        assertThatThrownBy(() -> commentService.findAllByNewsId(ID, PAGE_NO, PAGE_SIZE, SORT_BY))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(newsRepository).existsById(ID);
    }

    @Test
    void checkSave() {
        CommentDTO commentDTO = getCommentDTO();
        Comment comment = getComment();
        User user = getUserAdmin();
        News news = getNews();

        when(commentMapper.commentDTOToComment(commentDTO)).thenReturn(comment);

        when(commentRepository.save(comment)).thenReturn(comment);
        doReturn(Optional.of(getNews()))
                .when(newsRepository).findById(ID);
        doReturn(user)
                .when(userUtility).getUserByToken(TOKEN);
        System.out.println(commentDTO.toString());
        Comment result = commentService.save(ID, commentDTO, TOKEN);
        verify(newsRepository).findById(ID);
        verify(commentRepository).save(comment);
        verify(userUtility).getUserByToken(anyString());
        assertThat(result.getText()).isEqualTo(commentDTO.getText());

    }

    @Test
    void checkUpdate() {
        Comment comment = getComment();
        User user = getUserAdmin();

        doReturn(Optional.of(comment))
                .when(commentRepository).findById(ID);

        when(commentRepository.saveAndFlush(comment)).thenReturn(comment);
        doReturn(user)
                .when(userUtility).getUserByToken(TOKEN);

        Comment result = commentService.update(ID, getCommentDTO(), TOKEN);

        verify(commentRepository).findById(anyLong());
        verify(commentRepository).saveAndFlush(comment);
        assertThat(result.getText()).isEqualTo(comment.getText());

    }

    @Test
    void checkUpdateShouldThrowResourceNotFoundException() {
        User user = getUserAdmin();
        doReturn(user)
                .when(userUtility).getUserByToken(TOKEN);
        doThrow(ResourceNotFoundException.class)
                .when(commentRepository).findById(anyLong());
        assertThatThrownBy(() -> commentService.update(ID, getCommentDTO(), TOKEN))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(commentRepository).findById(anyLong());
        verify(userUtility).getUserByToken(anyString());
    }

    @Test
    void checkDeleteById() {
        User user = getUserAdmin();
        Comment comment = getComment();

        doReturn(Optional.of(comment))
                .when(commentRepository).findById(ID);
        doReturn(user)
                .when(userUtility).getUserByToken(TOKEN);
        doNothing()
                .when(commentRepository).deleteById(ID);

        commentService.delete(ID, TOKEN);

        verify(commentRepository, times(1)).findById(anyLong());
        verify(userUtility).getUserByToken(anyString());
        verify(commentRepository).deleteById(anyLong());
    }

    @Test
    void checkDeleteShouldThrowResourceNotFoundException() {
        User user = getUserAdmin();
        doReturn(user)
                .when(userUtility).getUserByToken(TOKEN);
        doThrow(ResourceNotFoundException.class)
                .when(commentRepository).findById(anyLong());
        assertThatThrownBy(() -> commentService.delete(ID, TOKEN))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(commentRepository).findById(anyLong());
        verify(userUtility).getUserByToken(anyString());
    }

    @Test
    void checkDeleteShouldThrowPermissionExceptionForUserWithoutRole() {
        User user = getUserWithoutRole();

        doReturn(user)
                .when(userUtility).getUserByToken(TOKEN);

        assertThatThrownBy(() -> commentService.delete(ID, TOKEN))

                .isInstanceOf(PermissionException.class)
                .hasMessage("No permission to perform operation");
        verify(userUtility).getUserByToken(anyString());
    }
}
