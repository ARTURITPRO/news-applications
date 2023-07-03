package ru.clevertec.newsservice.exception;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.clevertec.newsservice.controller.CommentController;
import ru.clevertec.newsservice.controller.NewsController;
import ru.clevertec.newsservice.exception.handler.RestExceptionHandler;

import java.util.Date;

/**
 * <d>TThis class is used to create body response Entity..</d>
 *
 * @Author Artur Malashkov
 * @see RestExceptionHandler
 * @since 17
 */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {
    Date timestamp;
    Integer status;
    String error;
    String message;
    String path;
}
