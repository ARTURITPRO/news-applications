package com.example.exception.aspect.handler;

import com.example.exception.aspect.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.exception.constants.Constants ;
import com.example.exception.util.MessageUtil ;

import java.util.Date;
import java.util.Objects;

/**
 * <d>This class provides guidelines for handling exceptions thrown by application controllers and contains unique responses to errors.</d>
 *
 * @Author Artur Malashko
 * @since 17
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler {

    private final MessageUtil messageUtil;

    /**
     * Handles EntityNotFoundException.
     *
     * @param request contains the parameters of the request that the user sent to the controller.
     * @param e       this is the exception that is passed to be handled
     * @return the handlerEntityNotFoundException response
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handlerEntityNotFoundException(HttpServletRequest request, EntityNotFoundException e) {
        log.error(e.getMessage(), e);
        return createResponseEntity(request, Constants.ERROR_CODE_ENTITY_NOT_FOUND_EXCEPTION, "handlerEntityNotFoundException", e.getMessage());
    }

    /**
     * Handles IllegalArgumentException.
     *
     * @param request contains the parameters of the request that the user sent to the controller.
     * @param e       this is the exception that is passed to be handled
     * @return the handlerIllegalArgumentException response
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handlerIllegalArgumentException(HttpServletRequest request, IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        return createResponseEntity(request, Constants.ERROR_CODE_ILLEGAL_ARGUMENT_EXCEPTION, "handlerIllegalArgumentException", e.getMessage());
    }

    /**
     * Handles MethodArgumentNotValidException.
     *
     * @param request contains the parameters of the request that the user sent to the controller.
     * @param e       this is the exception that is passed to be handled
     * @return the handlerMethodArgumentNotValidException response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handlerMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return createResponseEntity(request, Constants.ERROR_CODE_METHOD_ARGUMENT_NOT_VALID_EXCEPTION, "handlerMethodArgumentNotValidException", e.getMessage());
    }

    /**
     * Handles DataIntegrityViolationException.
     *
     * @param request contains the parameters of the request that the user sent to the controller.
     * @param e       this is the exception that is passed to be handled
     * @return the handlerDataIntegrityViolationException response
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handlerDataIntegrityViolationException(HttpServletRequest request, DataIntegrityViolationException e) {
        log.error(e.getMessage(), e);
        return createResponseEntity(request, Constants.ERROR_CODE_DATA_INTEGRITY_VIOLATION_EXCEPTION, "handlerDataIntegrityViolationException", e.getMessage());
    }

    /**
     * Handles Exception.
     *
     * @param request contains the parameters of the request that the user sent to the controller.
     * @param e       this is the exception that is passed to be handled
     * @return the handlerException response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerException(HttpServletRequest request, Exception e) {
        log.error(e.getMessage(), e);
        return createResponseEntity(request, HttpStatus.INTERNAL_SERVER_ERROR, "handlerException", e.getMessage());
    }

    /**
     * Create response Entity.
     *
     * @param request contains the parameters of the request that the user sent to the controller.
     * @param status  its status http.
     * @return response Entity.
     */
    private ResponseEntity<?> createResponseEntity(HttpServletRequest request, HttpStatus status, String code, Object... objects) {
        return ResponseEntity.status(status.value())
                .body(ErrorResponse.builder()
                        .path(request.getRequestURI())
                        .message(Objects.nonNull(code) ? messageUtil.getMessage(code, objects) : String.valueOf(objects[0]))
                        .error(status.getReasonPhrase())
                        .status(status.value())
                        .timestamp(new Date())
                        .build());
    }

    /**
     * Create response Entity.
     *
     * @param request contains the parameters of the request that the user sent to the controller.
     * @param status  its status http.
     * @return response Entity.
     */
    private ResponseEntity<?> createResponseEntity(HttpServletRequest request, int status, String code, Object... objects) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .path(request.getRequestURI())
                        .message(Objects.nonNull(code) ? messageUtil.getMessage(code, objects) : String.valueOf(objects[0]))
                        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .status(status)
                        .timestamp(new Date())
                        .build());
    }

}
