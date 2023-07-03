package ru.clevertec.userservice.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * <p> DTO entity representing token in request. </p>
 *
 * @author Artur Malashkov
 * @since 17
 */
@Getter
@Setter
public class JwtRefreshingRequest {

    public String refreshingToken;

}
