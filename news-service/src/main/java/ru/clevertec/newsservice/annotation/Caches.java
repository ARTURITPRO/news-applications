package ru.clevertec.newsservice.annotation;

import ru.clevertec.newsservice.aspect.CommentAspect;
import ru.clevertec.newsservice.aspect.NewsAspect;
import ru.clevertec.newsservice.cache.Cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>This annotation is put over the methods that the cache
 * should be used when working with.</p>
 *
 * @author Artur Malashkov
 * @see CommentAspect
 * @see NewsAspect
 * @see Cache
 * @since 17
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Caches {
}
