package ru.clevertec.userservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import ru.clevertec.userservice.dao.RoleEnum;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * <p> DTO authentication class in SecurityContext. </p>
 *
 * @author Artur Malashkov
 * @since 17
 */
@Getter
@Setter
public class JwtAuthentication implements Authentication {

    private String userName;
    private String firstName;
    private boolean isAuthenticated;
    private Set<RoleEnum> userRoles;

    @Override
    public String getName() {
        return firstName;
    }

    @Override
    public Object getPrincipal() {
        return userName;
    }

    @Override
    public Object getDetails() {
        return Optional.empty();
    }

    @Override
    public Object getCredentials() {
        return Optional.empty();
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRoles;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

}
