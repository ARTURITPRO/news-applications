package ru.clevertec.newsservice.repository;

import ru.clevertec.newsservice.dao.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository for Comment.
 * CommentRepository provides methods for interacting with the database.
 * @author Artur Malashkov
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    /**
     * Accepts the news id and returns the list of comments that have been applied to the given news
     * @param newsId id of the news.
     * @param paging The Pageable object to use for pagination.
     * @return the list of comments that have been applied to the given news.
     */
    Page<Comment> findAllByNewsId(Long newsId, Pageable paging);
}
