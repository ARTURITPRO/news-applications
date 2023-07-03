package ru.clevertec.newsservice.service.impl;

import ru.clevertec.newsservice.annotation.Caches;
import ru.clevertec.newsservice.dao.News;
import ru.clevertec.newsservice.dto.NewsDTO;
import ru.clevertec.newsservice.mapper.NewsMapper;
import ru.clevertec.newsservice.repository.NewsRepository;
import ru.clevertec.newsservice.service.NewsService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.clevertec.newsservice.constants.Constants.EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT;

/**
 * This service was created to work with the news database.
 *
 * @author Artur Malashkov
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    /**
     * {@inheritDoc}
     */
    public List<News> findAll(String keyword, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<News> pagedResult;
        if (Objects.nonNull(keyword)) {
            pagedResult = newsRepository.findAll(new Specification<News>() {
                @Override
                public Predicate toPredicate(Root<News> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.or(
                            criteriaBuilder.like(root.get("title"), keyword),
                            criteriaBuilder.like(root.get("text"), keyword)
                    );
                }
            }, paging);
        } else {
            pagedResult = newsRepository.findAll(paging);
        }
        return pagedResult.getContent().stream().collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Caches
    @Override
    public News findById(Long id) {
        final News news = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT,  id)));
        log.info("found giftCertificate - {}", news);
        return news;
    }

    /**
     * {@inheritDoc}
     */
    @Caches
    @Transactional
    @Override
    public News save(NewsDTO newsDto) {
        News news = newsMapper.newsDTOtoNews(newsDto);
        news.setTime(LocalDateTime.now());
        return newsRepository.save(news);
    }

    /**
     * {@inheritDoc}
     */
    @Caches
    @Transactional
    @Override
    public News update(Long id, NewsDTO newsDTO) {
        News newNews = newsRepository.findById(id)
                .map(news -> updateNewsFromNewsDTO(news, newsDTO))
                .map(newsRepository::saveAndFlush)
                .orElseThrow(() -> new EntityNotFoundException(String
                        .format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, id)));
        log.info("successful update of the news in the database - {}", newNews);
        return newNews;
    }

    /**
     * {@inheritDoc}
     */
    @Caches
    @Transactional
    @Override
    public void delete(Long id) {
       newsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String
                        .format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, id)));

        newsRepository.deleteById(id);
    }

    private News updateNewsFromNewsDTO(News news, NewsDTO dto) {
        if (Objects.nonNull(dto.getTitle())) {
            news.setTitle(dto.getTitle());
        }
        if (Objects.nonNull(dto.getText())) {
            news.setText(dto.getText());
        }
        if (Objects.nonNull(dto.getUserName())) {
            news.setUserName(dto.getUserName());
        }
        news.setTime(LocalDateTime.now());
        return news;
    }
}
