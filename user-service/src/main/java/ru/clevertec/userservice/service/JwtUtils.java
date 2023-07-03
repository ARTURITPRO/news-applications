package ru.clevertec.userservice.service;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.clevertec.userservice.dto.JwtAuthentication;
import ru.clevertec.userservice.dao.RoleEnum;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p> Class generating JwtAuthentication. </p>
 *
 * @author Artur Malashkov
 * @see RoleEnum
 * @see JwtAuthentication
 * @since 17
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    private static Set<RoleEnum> getRoles(Claims claims) {
        final List<LinkedHashMap<String, String>> rolesList = claims.get("roles", List.class);
        return rolesList.stream()
                .map(map -> map.get("role"))
                .map(RoleEnum::valueOf)
                .collect(Collectors.toSet());
    }

    public static JwtAuthentication generate(Claims claims) {
        JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setUserRoles(getRoles(claims));
        jwtInfoToken.setFirstName(claims.get("firstName", String.class));
        jwtInfoToken.setUserName(claims.getSubject());
        return jwtInfoToken;
    }

}
