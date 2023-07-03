package ru.clevertec.newsservice.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * <p> DTO Entity UserDTO </p>
 *
 * @author Artur Malashkov
 * @see RoleEnum
 * @see Role
 * @since 17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Set<Role> roles;

}
