package ru.clevertec.exception.constants;

import java.util.Locale;

/**
 * <d> This class simply provides constants for given RestExceptionHandler and Services.</d>
 *
 *  @author Artur Malashkov
 *  @since 17
 */
public class Constants {
    public static final String EXCEPTION_MESSAGE_ENTITY_NOT_FOUND_FORMAT = "Entity with id: %s is not exist";

    public static final String EXCEPTION_MESSAGE_SOMETHING_WENT_WRONG = "SOMETHING WENT WRONG";
    public static final Locale LOCALE = new Locale("ru");
    public static final int ERROR_CODE_ENTITY_NOT_FOUND_EXCEPTION = 404;
    public static final int ERROR_CODE_ILLEGAL_ARGUMENT_EXCEPTION = 400;
    public static final int ERROR_CODE_METHOD_ARGUMENT_NOT_VALID_EXCEPTION = 403;
    public static final int ERROR_CODE_DATA_INTEGRITY_VIOLATION_EXCEPTION = 404;
}
