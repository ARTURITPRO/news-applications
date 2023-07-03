package ru.clevertec.userservice.dao;

/**
 * <p> Base Entity. </p>
 *
 * @author Artur Malashkov
 * @since 17
 */
public interface BaseEntity<K extends Number> {

    K getId();

}
