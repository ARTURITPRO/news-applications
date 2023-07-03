package ru.clevertec.newsservice.client.dto;

import lombok.RequiredArgsConstructor;

/**
 * <p> Enum, representing different roles</p>
 *
 * @author Artur Malashkov
 * @see Role
 * @see User
 * @since 17
 */
@RequiredArgsConstructor
public enum RoleEnum {

    SUBSCRIBER("SUBSCRIBER"),
    JOURNALIST("JOURNALIST"),
    ADMIN("ADMIN");
    private final String value;

    public String getAuthority() {
        return value;
    }

}
