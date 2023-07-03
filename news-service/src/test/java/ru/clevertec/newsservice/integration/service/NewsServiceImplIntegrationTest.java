package ru.clevertec.newsservice.integration.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ru.clevertec.newsservice.dao.News;

import ru.clevertec.newsservice.dto.NewsDTO;
import ru.clevertec.newsservice.integration.PostgreSQLContainerIntegrationTest;

import ru.clevertec.newsservice.service.impl.NewsServiceImpl;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ru.clevertec.newsservice.util.Constants.*;
import static ru.clevertec.newsservice.util.TestData.getNewsDto;

class NewsServiceImplIntegrationTest extends PostgreSQLContainerIntegrationTest {

    @Autowired
    private NewsServiceImpl newsService;

    @Test
    void findAllNewsWithPagingAndSorting() {
        List<News> resultList = newsService.findAll(SEARCH_STR, PAGE_NO, PAGE_SIZE, SORTING);
        assertThat(resultList.size()).isEqualTo(10);
    }

    @Test
    void findAllNewsByTitleContent() {
        String search = "Economy resumes growth";
        List<News> resultList = newsService.findAll(search, PAGE_NO, PAGE_SIZE, SORTING);
        assertThat(resultList.size()).isEqualTo(1);
    }

    @Test
    void findAllNewsByTextContent() {
        String search = "After several months of decline, the economy is finally showing signs of recovery";
        List<News> resultList = newsService.findAll(search, PAGE_NO, PAGE_SIZE, SORTING);
        assertThat(resultList.size()).isEqualTo(1);
    }


    @Test
    void findByIdNews() {
        News news = newsService.findById(ID);
        assertThat(news.getId()).isEqualTo(ID);
        assertThat(news.getTitle()).isEqualTo("Economy resumes growth");
    }

    @Test
    void findByIdNonExistentNewsShouldThrowEntityNotFoundException() {
        assertThatThrownBy(() -> newsService.findById(66L))
                .isInstanceOf(EntityNotFoundException.class);
    }

//    @Test
//    void saveNews() throws IOException {
//        NewsDTO newsDto = getNewsDto();
//        News createdNews = newsService.save(newsDto);
//        assertThat(createdNews.getTitle()).isEqualTo("Title1");
//        assertThat(createdNews.getText()).isEqualTo("Text1");
//    }


    @Test
    void updateNews() throws IOException {
        NewsDTO newsDto = getNewsDto();
        News result = newsService.update(ID, newsDto);
        assertThat(result.getTitle()).isEqualTo(newsDto.getTitle());
        assertThat(result.getText()).isEqualTo(newsDto.getText());
    }

    @Test
    void updateNonExistentNewsShouldThrowEntityNotFoundException() throws IOException {

        assertThatThrownBy(() -> newsService.update(60L, getNewsDto()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deleteByIdNews() throws IOException {
        Integer sizeBefore = newsService.findAll(SEARCH_STR, PAGE_NO, 20, SORTING).size();
        newsService.delete(9L );
        Integer sizeAfter = newsService.findAll(SEARCH_STR, PAGE_NO, 20, SORTING).size();
        assertThat(sizeAfter).isLessThan(sizeBefore);
    }

    @Test
    void checkDeleteShouldThrowResourceNotFoundException() throws IOException {
        assertThatThrownBy(() -> newsService.delete(50L))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
