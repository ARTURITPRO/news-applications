package ru.clevertec.newsservice.service.impl;

import com.example.exception.exceptions.PermissionException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.newsservice.annotation.Caches;
import ru.clevertec.newsservice.client.dto.User;
import ru.clevertec.newsservice.dao.News;
import ru.clevertec.newsservice.dto.NewsDTO;
import ru.clevertec.newsservice.mapper.NewsMapper;
import ru.clevertec.newsservice.repository.NewsRepository;
import ru.clevertec.newsservice.service.NewsService;
import ru.clevertec.newsservice.util.UserUtility;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.clevertec.newsservice.constants.Constants.EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT;
import static ru.clevertec.newsservice.util.UserUtility.isUserAdmin;
import static ru.clevertec.newsservice.util.UserUtility.isUserJournalist;

/**
 * <p> This service was created to work with the news database </p>
 *
 * @author Artur Malashkov
 * @since 17
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final UserUtility userUtility;

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
    @Cacheable(value = "news", key = "#id")
    public News findById(Long id) {
        final News news = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, id)));
        log.info("found giftCertificate - {}", news);
        return news;
    }

    /**
     * {@inheritDoc}
     */
    @Caches
    @Override
    @Transactional
    @CachePut(value = "news", key = "#result.id")
    public News save(NewsDTO newsDto, String token) {
        User user = userUtility.getUserByToken(token);
        if (!(isUserAdmin(user) || isUserJournalist(user))) {
            throw new PermissionException("No permission to perform operation");
        }
        News news = newsMapper.newsDTOtoNews(newsDto);
        news.setTime(LocalDateTime.now());
        return newsRepository.save(news);
    }

    /**
     * {@inheritDoc}
     */
    @Caches
    @Override
    @Transactional
    @CachePut(value = "news", key = "#id")
    public News update(Long id, NewsDTO newsDTO, String token) {
        User user = userUtility.getUserByToken(token);
        if (!(isUserAdmin(user) || isUserJournalist(user))) {
            throw new PermissionException("No permission to perform operation");
        }

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
    @Override
    @Transactional
    @CacheEvict(value = "news", key = "#id")
    public void delete(Long id, String token) {
        User user = userUtility.getUserByToken(token);
        if (!(isUserAdmin(user) || isUserJournalist(user))) {
            throw new PermissionException("No permission to perform operation");
        }
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
