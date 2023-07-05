package ru.clevertec.newsservice.integration.service;

import com.github.tomakehurst.wiremock.client.WireMock;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Profile;
import ru.clevertec.newsservice.dao.News;

import ru.clevertec.newsservice.dto.NewsDTO;
import ru.clevertec.newsservice.integration.PostgreSQLContainerIntegrationTest;

import ru.clevertec.newsservice.integration.util.WireMockExtension;
import ru.clevertec.newsservice.integration.util.WireMockUtil;
import ru.clevertec.newsservice.service.impl.NewsServiceImpl;

import java.io.IOException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ru.clevertec.newsservice.util.Constants.*;
import static ru.clevertec.newsservice.util.TestData.getNewsDto;

@Profile("test")
@Tag("Integration test")
@RequiredArgsConstructor
@ExtendWith(WireMockExtension.class)
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

    @Test
    void saveNews() throws IOException {

        stubFor(WireMock.get(urlPathMatching("/api/auth/user/" + TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(WireMockUtil.buildUserAdminResponse())));

        NewsDTO newsDto = getNewsDto();
        News createdNews = newsService.save(newsDto, TOKEN);
        assertThat(createdNews.getTitle()).isEqualTo("Title1");
        assertThat(createdNews.getText()).isEqualTo("Text1");
    }


    @Test
    void updateNews() throws IOException {

        stubFor(WireMock.get(urlPathMatching("/api/auth/user/" + TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(WireMockUtil.buildUserAdminResponse())));

        NewsDTO newsDto = getNewsDto();
        News result = newsService.update(ID, newsDto, TOKEN);
        assertThat(result.getTitle()).isEqualTo(newsDto.getTitle());
        assertThat(result.getText()).isEqualTo(newsDto.getText());
    }

    @Test
    void updateNonExistentNewsShouldThrowEntityNotFoundException() throws IOException {

        stubFor(WireMock.get(urlPathMatching("/api/auth/user/" + TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(WireMockUtil.buildUserAdminResponse())));

        assertThatThrownBy(() -> newsService.update(60L, getNewsDto(), TOKEN))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deleteByIdNews() throws IOException {

        stubFor(WireMock.get(urlPathMatching("/api/auth/user/" + TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(WireMockUtil.buildUserAdminResponse())));

        Integer sizeBefore = newsService.findAll(SEARCH_STR, PAGE_NO, 20, SORTING).size();
        newsService.delete(9L , TOKEN);
        Integer sizeAfter = newsService.findAll(SEARCH_STR, PAGE_NO, 20, SORTING).size();
        assertThat(sizeAfter).isLessThan(sizeBefore);
    }

    @Test
    void checkDeleteShouldThrowResourceNotFoundException() throws IOException {

        stubFor(WireMock.get(urlPathMatching("/api/auth/user/" + TOKEN))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(WireMockUtil.buildUserAdminResponse())));

        assertThatThrownBy(() -> newsService.delete(50L, TOKEN))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
