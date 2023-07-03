package ru.clevertec.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p> DTO  representing tokens in respons. </p>
 *
 * @author Artur Malashkov
 * @since 17
 */
@Getter
@AllArgsConstructor
public class JwtResponse {

    private String accessToken;
    private String refreshToken;
    private final String type = "Bearer";

}
