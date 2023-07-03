package ru.clevertec.userservice.mapper;

import ru.clevertec.userservice.dao.User;
import org.springframework.stereotype.Component;
import ru.clevertec.userservice.dto.JwtRequest;

/**
 * <p> Mapper for converting JwtRequest to User. </p>
 *
 * @author Artur Malashkov
 * @see JwtRequest
 * @see User
 * @since 17
 */
@Component
public class UserMapper {

    /**
     * Method for custom mapping JwtRequest to user
     * @param jwtRequest - request
     * @return user
     */
    public User requestToUser(JwtRequest jwtRequest) {
        return User.builder().username(jwtRequest.getUserName())
                .password(jwtRequest.getUserPassword())
                .firstName(jwtRequest.getUserFirstName())
                .lastName(jwtRequest.getUserLastName())
                .roles(jwtRequest.getUserRoles())
                .build();
    }

}
