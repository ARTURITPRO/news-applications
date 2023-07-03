package ru.clevertec.newsservice.cache;

import ru.clevertec.newsservice.aspect.CommentAspect;
import ru.clevertec.newsservice.aspect.NewsAspect ;
import ru.clevertec.newsservice.cache.impl.LFUCache;
import ru.clevertec.newsservice.cache.impl.LRUCache;
import ru.clevertec.newsservice.dao.Entity;

import java.util.Optional;

/**
 * <p>A common interface for implementing various caching algorithms.</p>
 * <p>Works with entities implementing the {@link Entity} interface</p>
 *
 * @author Artur Malashkov
 * @see CommentAspect
 * @see NewsAspect
 * @see LFUCache
 * @see LRUCache
 * @since 17
 */
public interface Cache {

    /**
     * Default cache size.
     */
    Long DEFAULT_CACHE_SIZE = 3L;

    /**
     * Sets the maximum number of entities in the cache.
     *
     * @param cacheSize cache size
     */
    void setCacheSize(Long cacheSize);

    /**
     * Saves the entity to the cache.
     *
     * @param entity entity
     * @return saved entity
     */
    Entity save(Entity entity);

    /**
     * Searches for an entity in the cache by ID and type. If the entity is found in the cache,
     * the returned container will contain it, but if the entity is not found, the container will be empty.
     *
     * @param id    ID of the entity in the repository
     * @param clazz type of entity
     * @return container for the entity
     */
    Optional<Entity> find(Long id, Class<?> clazz);

    /**
     * Updates the entity in the cache. The cache is searched by ID and type.
     *
     * @param entity entity to update
     * @return updated entity
     */
    Entity update(Entity entity);

    /**
     * Deletes an entity from the cache by ID and type.
     *
     * @param id    ID of the entity in the repository
     * @param clazz type of entity
     * @return true if the entity was found and deleted
     */
    boolean delete(Long id, Class<?> clazz);

    void clear();

    boolean isEmpty();
}
