package ru.clevertec.userservice.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/**
 * <p> Enum Role user. </p>
 *
 * @author Artur Malashkov
 * @since 17
 */
@RequiredArgsConstructor
public enum RoleEnum implements GrantedAuthority {

    ADMIN("ADMIN"),
    JOURNALIST("JOURNALIST"),
    SUBSCRIBER("SUBSCRIBER");
    private final String authority;

    @Override
    public String getAuthority() {
        return authority;
    }

}
