package ru.clevertec.newsservice.service;

import ru.clevertec.newsservice.dao.News;
import ru.clevertec.newsservice.dto.NewsDTO;

import java.util.List;

/**
 * A service that provides CRUD operations for working with News.
 * @author Artur Malashkov.
 */
public interface NewsService {

    /**
     * Returns a list of news with pagination and sorting.
     *
     * @param pageNo   indicates the page number that will later need to be returned.
     * @param pageSize this parameter indicates the maximum number of entities (news) that will be placed on one page.
     * @param sortBy   this parameter indicates the type of sorting of found entities (news) on the page.
     * @return a List of news.
     */
    List<News> findAll(String search, Integer pageNo, Integer pageSize, String sortBy);

    /**
     * Search for news by ID in the database.
     *
     * @param id parameter for the id of a find news.
     * @return id News.
     */
    News findById(Long id);

    /**
     * Сreating new news in the database.
     *
     * @param newsDTO this is news that has been submitted for storage in the database.
     * @return created News.
     */
    News save(NewsDTO newsDTO);

    /**
     * Used to update the entity (News) in the database. The entity ID is used to find the entity to update.
     *
     * @param newsDTO entity to save.
     * @return the updated entity (News).
     */
    News update(Long id, NewsDTO newsDTO);

    /**
     * This method allows you to delete an entity (News) in the database.
     *
     * @param id this is the parameter by which the entity is searched for further removal from the database.
     */
    void delete(Long id);
}
