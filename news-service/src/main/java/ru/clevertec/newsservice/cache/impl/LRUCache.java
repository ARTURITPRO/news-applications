package ru.clevertec.newsservice.cache.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.clevertec.newsservice.annotation.Caches;
import ru.clevertec.newsservice.cache.Cache;
import ru.clevertec.newsservice.dao.Entity;

import java.time.Instant;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static lombok.AccessLevel.PRIVATE;

/**
 * <p>LRU Cache.</p>
 *
 * @author Artur Malashkov
 * @see Caches
 * @see Cache
 * @since 17
 */
@Slf4j
@Component
@Profile("LRU")
@NoArgsConstructor(access = PRIVATE)
public class LRUCache implements Cache {

    /**
     * Cache Object.
     */
    private static final Cache INSTANCE = new LRUCache();

    /**
     * A thread-safe map for storing cache items.
     */
    private final Map<String, Element> mapCacheElements = new ConcurrentHashMap<>();

    /**
     * Cache size, default value is set to default.
     */
    @Setter
    private Long cacheSize = DEFAULT_CACHE_SIZE;

    /**
     * Saves the entity to the cache.
     *
     * @param entity entity
     * @return saved entity
     */
    @Override
    public Entity save(Entity entity) {
        Optional<Element> maybeElement = findCacheElement(entity.getId(), entity.getClass());
        if (!maybeElement.isPresent()) {
            addCacheElement(entity);
        }
        return entity;
    }

    /**
     * Searches for an entity in the cache by ID and type. If the entity is found in the cache,
     * the returned container will contain it, but if the entity is not found, the container will be empty.
     *
     * @param id    ID of the entity in the repository
     * @param clazz type of entity
     * @return container for the entity
     */
    @Override
    public Optional<Entity> find(Long id, Class<?> clazz) {
        return findCacheElement(id, clazz)
                .map(Element::getEntity);
    }

    /**
     * Updates the entity in the cache. The cache is searched by ID and type.
     *
     * @param entity entity to update
     * @return updated entity
     */
    @Override
    public Entity update(Entity entity) {
        Optional<Element> maybeElement = findCacheElement(entity.getId(), entity.getClass());
        if (maybeElement.isPresent()) {
            maybeElement.get().setEntity(entity);
        } else {
            addCacheElement(entity);
        }
        return entity;
    }

    /**
     * Deletes an entity from the cache by ID and type.
     *
     * @param id    ID of the entity in the repository
     * @param clazz type of entity
     * @return true if the entity was found and deleted
     */
    @Override
    public boolean delete(Long id, Class<?> clazz) {
        Optional<Element> maybeElement = findCacheElement(id, clazz);
        if (maybeElement.isPresent()) {
            mapCacheElements.remove(generatedKeyForCache(maybeElement.get().getEntity()));
            log.info("The entity has been removed from the cache: {} [{}]",
                    maybeElement.get().getEntity(),
                    maybeElement.get().getUpdatedAt()
            );
            return true;
        } else {
            return false;
        }
    }

    /**
     * Clears the cache of all elements.
     */
    @Override
    public void clear() {
        mapCacheElements.clear();
    }

    /**
     * Returns true if this cache contains no elements.
     *
     * @return true if this cache contains no elements
     */
    @Override
    public boolean isEmpty() {
        return mapCacheElements.isEmpty();
    }

    /**
     * Adds a new element to the cache.
     *
     * @param entity the entity to add to the element
     */
    private void addCacheElement(Entity entity) {
        while (mapCacheElements.size() >= cacheSize) {
            removeOldCacheElement();
        }
        mapCacheElements.put(generatedKeyForCache(entity), new Element(entity, Instant.now()));
        log.info("Entity is saved in the cache: {}", entity);
    }

    /**
     * Generates a key by entity class and id, for storing and searching in the cache.
     *
     * @param entity entity
     * @return key for storing and searching in the cache
     */
    private String generatedKeyForCache(Entity entity) {
        return entity.getClass().getName() + "@" + entity.getId();
    }

    /**
     * Generates a key by entity class and id, for storing and searching in the cache.
     *
     * @param id    ID of the entity in the repository
     * @param clazz type of entity
     * @return key for storing and searching in the cache
     */
    private String generatedKeyForCache(Long id, Class<?> clazz) {
        return clazz.getName() + "@" + id;
    }

    /**
     * Searches for an item in the cache by ID and the type of entity in it.
     * If such an entity is found, then the returned container will contain an element with it,
     * if there is no element with such an entity, then the container will be empty.
     *
     * @param id    entity ID
     * @param clazz type of entity
     * @return container for the element
     */
    private Optional<Element> findCacheElement(Long id, Class<?> clazz) {
        Optional<Element> maybeElement = Optional.ofNullable(mapCacheElements.getOrDefault(generatedKeyForCache(id, clazz), null));
        if (maybeElement.isPresent()) {
            maybeElement.get().setUpdatedAt(Instant.now());
            log.info("Entity found in cache: {} [{}]",
                    maybeElement.get().getEntity(),
                    maybeElement.get().getUpdatedAt()
            );
        }
        return maybeElement;
    }

    /**
     * Deletes the oldest item from the cache.
     */
    private void removeOldCacheElement() {
        Optional<Element> maybeElement = mapCacheElements.values().stream()
                .min(Comparator.comparing(Element::getUpdatedAt));
        if (maybeElement.isPresent()) {
            mapCacheElements.remove(generatedKeyForCache(maybeElement.get().getEntity()));
            log.info("The entity has been removed from the cache: {} [{}]",
                    maybeElement.get().getEntity(),
                    maybeElement.get().getUpdatedAt()
            );
        }
    }

    /**
     * Static method for getting the cache object.
     *
     * @return cache object
     */
    public static Cache getInstance() {
        return INSTANCE;
    }

    /**
     * The cache element contains the entity and the time of the last access to it.
     */
    @AllArgsConstructor
    @Data
    private static class Element {

        Entity entity;
        Instant updatedAt;
    }

}
