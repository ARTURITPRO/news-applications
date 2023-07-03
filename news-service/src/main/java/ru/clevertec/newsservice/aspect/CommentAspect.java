package ru.clevertec.newsservice.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.clevertec.newsservice.annotation.Caches;
import ru.clevertec.newsservice.cache.Cache;
import ru.clevertec.newsservice.dao.Entity;

import java.util.Optional;

/**
 * <p>An aspect class that adds functionality for caching entities when receiving,
 * deleting, creating and updating in the dao.Caching to methods defined in a slice
 * of points is applied if they have a @Caches annotation hanging over them.</p>
 *
 * @author Artur Malashkov
 * @see Caches
 * @see Cache
 * @since 17
 */
@Slf4j
@Aspect
@Component
@Profile("custom-cache")
@RequiredArgsConstructor
public class CommentAspect {

    private final Cache cache;

    @Pointcut("@annotation(ru.clevertec.newsservice.annotation.Caches)")
    public void joinToCache() {
    }

    /**
     * Advice at the method level. This advice intercepts control to interact with the cache.
     *
     * @param joinPoint represents the execution of the method.
     * @return the result of executing the method to which the advice was applied.
     * The result of the method execution does not change.
     */
    @SuppressWarnings("unchecked")
    @Around("joinToCache() && execution( public * ru.clevertec.newsservice.service.impl.CommentServiceImpl.findById(..))")
    public Object searchInTheCacheAndRepo(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("findInTheCacheAndDatabase");
        Class<?> clazz = joinPoint.getSignature().getDeclaringType();
        Object[] arguments = joinPoint.getArgs();

        Optional<Entity> maybeEntity = cache.find((Long) arguments[0], clazz);
        if (maybeEntity.isPresent()) {
            return maybeEntity;
        } else {
            Entity entity = (Entity) joinPoint.proceed(arguments);
            Optional.of(entity).ifPresent(cache::update);
            return entity;
        }
    }

    /**
     * Advice at the method level. This advice intercepts control to interact with the cache.
     *
     * @param joinPoint represents the execution of the method.
     * @return the result of executing the method to which the advice was applied.
     * The result of the method execution does not change.
     */
    @Around("joinToCache() && execution(public * ru.clevertec.newsservice.service.impl.CommentServiceImpl.save(..))")
    public Object putToCacheAndDatabase(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("saveInTheCacheAndDatabase");
        Object[] arguments = joinPoint.getArgs();

        Entity entity = (Entity) joinPoint.proceed(arguments);

        cache.save(entity);
        return entity;
    }

    /**
     * Advice at the method level. This advice intercepts control to interact with the cache.
     *
     * @param joinPoint represents the execution of the method.
     * @return the result of executing the method to which the advice was applied.
     * The result of the method execution does not change.
     */
    @SuppressWarnings("unchecked")
    @Around("joinToCache() && execution(public * ru.clevertec.newsservice.service.impl.CommentServiceImpl.update(..))")
    public Object refreshedInRepoAndCache(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("updateInTheCacheAndDatabase");
        Object[] arguments = joinPoint.getArgs();

        Entity entity = (Entity) joinPoint.proceed(arguments);
        Optional.of(entity).ifPresent(cache::update);
        return entity;
    }

    /**
     * Advice at the method level. This advice intercepts control to interact with the cache.
     *
     * @param joinPoint represents the execution of the method.
     */
    @Around("joinToCache() && execution(public * ru.clevertec.newsservice.service.impl.CommentServiceImpl.delete(..))")
    public void disposalInCacheAndRepo(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("deleteInTheCacheAndDatabase");
        Class<?> clazz = joinPoint.getSignature().getDeclaringType();
        Object[] arguments = joinPoint.getArgs();

        joinPoint.proceed(arguments);

        cache.delete((Long) arguments[0], clazz);

    }

}
