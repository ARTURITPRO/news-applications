package ru.clevertec.newsservice.repository;

import ru.clevertec.newsservice.dao.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository for News.
 * NewsRepository provides methods for interacting with the database.
 * @author Artur Malashkov
 */
@Repository
public interface NewsRepository extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {
}