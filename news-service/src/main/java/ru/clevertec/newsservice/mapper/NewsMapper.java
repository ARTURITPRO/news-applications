package ru.clevertec.newsservice.mapper;

import ru.clevertec.newsservice.dao.News;
import ru.clevertec.newsservice.dto.NewsDTO;
import org.springframework.stereotype.Component;

import java.security.Timestamp;
import java.time.Instant;
import java.time.ZoneId;

/**
 * <d>This class is used for mapping NewsDTO and News.</d>
 *
 *  @author Artur Malashkov
 *  @since 17
 */
@Component
public class NewsMapper {

    public NewsDTO newsToNewsDTO (News news){
        return NewsDTO.builder()
                .id(news.getId())
                .title(news.getTitle())
                .text(news.getText())
                .time(news.getTime())
                .userName(news.getUserName())
                .build();
    }

    public News newsDTOtoNews(NewsDTO newsDTO){

        return News.builder()
                .title(newsDTO.getTitle())
                .text(newsDTO.getText())
                .userName(newsDTO.getUserName())
                .build();

    }
}
