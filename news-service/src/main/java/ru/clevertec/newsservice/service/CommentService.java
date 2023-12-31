package ru.clevertec.newsservice.service;

import ru.clevertec.newsservice.dao.Comment;
import ru.clevertec.newsservice.dto.CommentDTO;

import java.util.List;

/**
 * <p> A service that provides CRUD operations for working with Comment. </p>
 *
 * @author Artur Malashkov
 * @since 17
 */
public interface CommentService {

    /**
     * Returns a list of Comment with pagination and sorting.
     *
     * @param pageNo   indicates the page number that will later need to be returned.
     * @param pageSize this parameter indicates the maximum number of entities (Comment) that will be placed on one page.
     * @param sortBy   this parameter indicates the type of sorting of found entities (Comment) on the page.
     * @return a List of Comment.
     */
    List<Comment> findAll(String search, Integer pageNo, Integer pageSize, String sortBy);

    /**
     * Search for Comment by ID in the database.
     *
     * @param id parameter for the id of a find Comment.
     * @return id Comment.
     */
    Comment findById(Long id);

    /**
     * Сreating new Comment in the database.
     *
     * @param idNews     this is id News
     * @param commentDTO this is news that has been submitted for storage in the database.
     * @param token      this is a unique string that is used to authenticate and authorize the user when making
     *                   requests to protected API resources
     * @return created Comment.
     */
    Comment save(Long idNews, CommentDTO commentDTO, String token);

    /**
     * Used to update the entity (Comment) in the database. The entity ID is used to find the entity to update.
     *
     * @param commentDTO entity to save.
     * @param token      this is a unique string that is used to authenticate and authorize the user when making
     *                   requests to protected API resources
     * @return the updated entity (News).
     */
    Comment update(Long id, CommentDTO commentDTO, String token);

    /**
     * This method allows you to delete an entity (Comment) in the database.
     *
     * @param id    this is the parameter by which the entity is searched for further removal from the database.
     * @param token this is a unique string that is used to authenticate and authorize the user when making
     *              requests to protected API resources
     */
    void delete(Long id, String token);

    /**
     * Returns all comments by specifying news id.
     *
     * @param newsId   parameter for the id of a certain news
     * @param pageNo   parameter for a specific page
     * @param pageSize parameter for page size
     * @param sortBy   parameter for sorting the result
     * @return A JSON representation of the comments of the given news
     */
    List<Comment> findAllByNewsId(Long newsId, Integer pageNo, Integer pageSize, String sortBy);

}
