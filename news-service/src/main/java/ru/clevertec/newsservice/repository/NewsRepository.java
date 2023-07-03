package ru.clevertec.newsservice.repository;

import ru.clevertec.newsservice.dao.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository for News.
 *
 * @author Artur Malashkov
 * @since 17
 */
@Repository
public interface NewsRepository extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {
}