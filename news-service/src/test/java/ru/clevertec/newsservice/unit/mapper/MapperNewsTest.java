package ru.clevertec.newsservice.unit.mapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.clevertec.newsservice.dao.News;
import ru.clevertec.newsservice.dto.NewsDTO;
import ru.clevertec.newsservice.mapper.NewsMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.clevertec.newsservice.util.TestData.*;


class MapperNewsTest {

    private static NewsMapper newsMapper;

    private News news = getNews();
    private NewsDTO newsDtoRequest = getNewsDto();

    @BeforeAll
    static void CreateCommentMapper() {
        newsMapper = new NewsMapper();
    }

    @Test
    void NewsDtoToNews() {
        News actual = newsMapper.newsDTOtoNews(newsDtoRequest);
        assertThat(actual.getTitle()).isEqualTo(newsDtoRequest.getTitle());
        assertThat(actual.getText()).isEqualTo(newsDtoRequest.getText());
    }

    @Test
    void NewsToNewsDto() {
        NewsDTO actual = newsMapper.newsToNewsDTO(news);
        assertThat(actual.getId()).isEqualTo(news.getId());
        assertThat(actual.getTitle()).isEqualTo(news.getTitle());
        assertThat(actual.getText()).isEqualTo(news.getText());
    }
}
