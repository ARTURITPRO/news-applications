package ru.clevertec.userservice.dto;

import lombok.Getter;
import lombok.Setter;
import ru.clevertec.userservice.dao.Role;

import java.util.Set;

/**
 * <p> DTO representing the user in java web token request. </p>
 *
 * @author Artur Malashkov
 * @since 17
 */
@Setter
@Getter
public class JwtRequest {

    private String userName;
    private String userPassword;
    private Set<Role> userRoles;
    private String userLastName;
    private String userFirstName;

}
