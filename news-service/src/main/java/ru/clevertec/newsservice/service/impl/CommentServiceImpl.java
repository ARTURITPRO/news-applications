package ru.clevertec.newsservice.service.impl;

import ru.clevertec.newsservice.annotation.Caches;
import ru.clevertec.newsservice.dao.Comment;
import ru.clevertec.newsservice.dao.News;
import ru.clevertec.newsservice.dto.CommentDTO;
import ru.clevertec.newsservice.mapper.CommentMapper;
import ru.clevertec.newsservice.repository.CommentRepository;
import ru.clevertec.newsservice.repository.NewsRepository;
import ru.clevertec.newsservice.service.CommentService;
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
 * @author Artur Malashkov
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;
    private  final CommentMapper commentMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Comment> findAll(String keyword, Integer pageNo, Integer pageSize, String sortBy ) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        log.info("keyword : {}", keyword);

        Page<Comment> pagedResult;
        if (Objects.nonNull(keyword)) {
            pagedResult = commentRepository.findAll(new Specification<Comment>() {
                @Override
                public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    return criteriaBuilder.or(
                            criteriaBuilder.like(root.get("text"), keyword)
                    );
                }
            }, paging);
        }
        else{
            pagedResult = commentRepository.findAll(paging);
        }
        return pagedResult.getContent().stream().collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Caches
    @Override
    public Comment findById(Long id) {
        final Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, "comment", id)));
        log.info("found giftCertificate - {}", comment);
        return comment;
    }


    /**
     * {@inheritDoc}
     */
    @Caches
    @Transactional
    @Override
    public Comment save(Long idNews, CommentDTO commentDTO) {
        log.info("newsId = :{}", idNews);
        log.info("commentDto = :{}", commentDTO);
        News news = newsRepository.findById(idNews)
                .orElseThrow(() -> new EntityNotFoundException(String
                        .format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, idNews)));

        Comment comment = commentMapper.commentDTOToComment(commentDTO);
        comment.setNews(news);
        comment.setTime(LocalDateTime.now());
        log.info("save comment = :{}", comment);
        return commentRepository.save(comment);
    }

    /**
     * {@inheritDoc}
     */
    @Caches
    @Transactional
    @Override
    public Comment update(Long id, CommentDTO commentDTO) {
         return commentRepository.findById(id)
                .map(comment -> updateCommentFromCommentDTO(comment, commentDTO))
                .map(commentRepository::saveAndFlush)
                .orElseThrow(() -> new EntityNotFoundException(String
                        .format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, id)));
    }

    /**
     * {@inheritDoc}
     */
    @Caches
    @Transactional
    @Override
    public void delete(Long id) {
        commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String
                .format(EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT, id)));

        commentRepository.deleteById(id);
    }

    private Comment updateCommentFromCommentDTO(Comment comment, CommentDTO commentDTO) {
        if (Objects.nonNull(commentDTO.getText())) {
            comment.setText(commentDTO.getText());
        }
        if (Objects.nonNull(commentDTO.getUserName())) {
            comment.setUserName(commentDTO.getUserName());
        }
        comment.setTime(LocalDateTime.now());
        return comment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Comment> findAllByNewsId(Long newsId, Integer pageNo, Integer pageSize, String sortBy) {
        if (!newsRepository.existsById(newsId)) {
            throw new EntityNotFoundException("Not found News with id = " + newsId);
        }
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Comment> pagedResult = commentRepository.findAllByNewsId(newsId, paging);

        return pagedResult.getContent().stream().collect(Collectors.toList());
    }
}
