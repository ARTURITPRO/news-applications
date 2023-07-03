package com.example.exception.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import com.example.exception.constants.Constants ;

/**
 * <d>This class is used for RestExceptionHandler</d>
 *
 *  @author Artur Malashkov
 *  @since 17
 */
@Service
@RequiredArgsConstructor
public class MessageUtil {

    private final MessageSource messageSource;

    public String getMessage(String code, Object... objects){
        return messageSource.getMessage(code, objects, Constants.LOCALE);
    }
}
