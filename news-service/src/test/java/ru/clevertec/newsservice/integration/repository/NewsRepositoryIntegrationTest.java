package ru.clevertec.newsservice.integration.repository;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.clevertec.newsservice.dao.News;
import ru.clevertec.newsservice.integration.PostgreSQLContainerIntegrationTest;
import ru.clevertec.newsservice.repository.NewsRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.clevertec.newsservice.util.Constants.*;
import static ru.clevertec.newsservice.util.TestData.getNews;

@Tag("Integration test")
class NewsRepositoryIntegrationTest extends PostgreSQLContainerIntegrationTest {

    @Autowired
    private NewsRepository newsRepository;

    @Test
    void findAllNewsWithPagingAndSorting() {
        Pageable paging = PageRequest.of(PAGE_NO, PAGE_SIZE, Sort.by(SORTING));
        Page<News> pagedResult = newsRepository.findAll(paging);
        assertThat(pagedResult.getContent().size()).isEqualTo(10);
    }

    @Test
    void findByIdNews() {
        Optional<News> newsData = newsRepository.findById(ID);
        assertThat(newsData.get().getId()).isEqualTo(ID);
        assertThat(newsData.get().getTitle()).isEqualTo("Economy resumes growth");
    }

    @Test
    void findByIdNonExistentNews() {
        Optional<News> newsData = newsRepository.findById(21L);
        assertThat(newsData).isEmpty();
    }

    @Test
    void saveNews() {
        News news = getNews();
        News createdNews = newsRepository.save(news);
        assertThat(createdNews.getTitle()).isEqualTo("Title1");
        assertThat(createdNews.getText()).isEqualTo("Text1");
    }

    @Test
    void updateNews() {
        News news = getNews();
        newsRepository.save(news);
        news.setTitle("Economy has been provider");
        news.setText("provider lang the most no");
        News updatedNews = newsRepository.save(news);
        assertThat(updatedNews.getTitle()).isEqualTo("Economy has been provider");
        assertThat(updatedNews.getText()).isEqualTo("provider lang the most no");
    }

    @Test
    void deleteNewsById() {
        Integer sizeBefore = newsRepository.findAll(PageRequest.of(PAGE_NO, PAGE_SIZE + 190, Sort.by(SORTING)))
                .getContent().size();
        newsRepository.deleteById(9L);
        Integer sizeAfter = newsRepository.findAll(PageRequest.of(PAGE_NO, PAGE_SIZE + 190, Sort.by(SORTING)))
                .getContent().size();
        assertThat(sizeAfter).isLessThan(sizeBefore);
    }
}
