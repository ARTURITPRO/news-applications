package ru.clevertec.newsservice.unit.service;

import jakarta.persistence.EntityNotFoundException;
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
import ru.clevertec.newsservice.dao.News;
import ru.clevertec.newsservice.dto.NewsDTO;
import ru.clevertec.newsservice.mapper.NewsMapper;
import ru.clevertec.newsservice.repository.CommentRepository;
import ru.clevertec.newsservice.repository.NewsRepository;
import ru.clevertec.newsservice.service.impl.NewsServiceImpl;
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
class NewsServiceImplTest {

    @InjectMocks
    private NewsServiceImpl newsService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private UserUtility userUtility;

    @Mock
    NewsMapper newsMapper;

    @Test
    void findAllNews() {

        Pageable paging = PageRequest.of(PAGE_NO, PAGE_SIZE, Sort.by(SORT_BY));

        News news = getNews();

        doReturn(new PageImpl<>(getNewsList()))
                .when(newsRepository).findAll(paging);

        List<News> commentList = newsService.findAll(null, PAGE_NO, PAGE_SIZE, SORT_BY);

        verify(newsRepository).findAll(paging);

        assertThat(commentList.get(0).getId()).isEqualTo(news.getId());
        assertThat(commentList.get(0).getText()).isEqualTo(news.getText());
        assertThat(commentList.get(0).getUserName()).isEqualTo(news.getUserName());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    void findNewsById(Long id) {
        News news = getNews();

        doReturn(Optional.of(news))
                .when(newsRepository).findById(id);

        News result = newsService.findById(id);

        verify(newsRepository).findById(anyLong());
        assertThat(result).isEqualTo(news);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L})
    void findByIdShouldThrowResourceNotFoundException(Long id) {
        doThrow(EntityNotFoundException.class)
                .when(newsRepository).findById(anyLong());
        assertThatThrownBy(() -> newsService.findById(id))
                .isInstanceOf(EntityNotFoundException.class);
        verify(newsRepository).findById(anyLong());
    }

    @Test
    void saveNews() {
        NewsDTO newsDTO = getNewsDto();
        User user = getUserAdmin();
        News news = getNews();

        when(newsMapper.newsDTOtoNews(newsDTO)).thenReturn(news);

        when(newsRepository.save(news)).thenReturn(news);

        doReturn(user)
                .when(userUtility).getUserByToken(TOKEN);
        System.out.println(newsDTO.toString());
        News result = newsService.save(newsDTO, TOKEN);
        verify(newsRepository).save(news);
        verify(userUtility).getUserByToken(anyString());
        assertThat(result.getText()).isEqualTo(newsDTO.getText());

    }

    @Test
    void updateNews() {
        News news = getNews();
        User user = getUserAdmin();

        doReturn(Optional.of(news))
                .when(newsRepository).findById(ID);

        when(newsRepository.saveAndFlush(news)).thenReturn(news);
        doReturn(user)
                .when(userUtility).getUserByToken(TOKEN);

        News news1 = newsService.update(ID, getNewsDto(), TOKEN);

        verify(newsRepository).findById(anyLong());
        verify(newsRepository).saveAndFlush(news);
        assertThat(news1.getText()).isEqualTo(news.getText());

    }

    @Test
    void updateShouldThrowResourceNotFoundException() {
        User user = getUserAdmin();
        doReturn(user)
                .when(userUtility).getUserByToken(TOKEN);
        doThrow(ResourceNotFoundException.class)
                .when(newsRepository).findById(anyLong());
        assertThatThrownBy(() -> newsService.update(ID, getNewsDto(), TOKEN))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(newsRepository).findById(anyLong());
        verify(userUtility).getUserByToken(anyString());
    }

    @Test
    void deleteByIdNews() {
        User user = getUserAdmin();
        News news = getNews();

        doReturn(Optional.of(news))
                .when(newsRepository).findById(ID);
        doReturn(user)
                .when(userUtility).getUserByToken(TOKEN);
        doNothing()
                .when(newsRepository).deleteById(ID);

        newsService.delete(ID, TOKEN);

        verify(newsRepository, times(1)).findById(anyLong());
        verify(userUtility).getUserByToken(anyString());
        verify(newsRepository).deleteById(anyLong());
    }

    @Test
    void deleteShouldThrowResourceNotFoundException() {
        User user = getUserAdmin();
        doReturn(user)
                .when(userUtility).getUserByToken(TOKEN);
        doThrow(ResourceNotFoundException.class)
                .when(newsRepository).findById(anyLong());
        assertThatThrownBy(() -> newsService.delete(ID, TOKEN))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(newsRepository).findById(anyLong());
        verify(userUtility).getUserByToken(anyString());
    }

    @Test
    void deleteShouldThrowPermissionExceptionForUserWithoutRole() {
        User user = getUserWithoutRole();

        doReturn(user)
                .when(userUtility).getUserByToken(TOKEN);

        assertThatThrownBy(() -> newsService.delete(ID, TOKEN))

                .isInstanceOf(PermissionException.class)
                .hasMessage("No permission to perform operation");
        verify(userUtility).getUserByToken(anyString());
    }

}
