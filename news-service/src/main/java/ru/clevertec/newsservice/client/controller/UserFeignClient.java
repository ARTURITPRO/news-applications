package ru.clevertec.newsservice.client.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.clevertec.newsservice.client.dto.Role;
import ru.clevertec.newsservice.client.dto.User;

/**
 * <p>FeignClient that Sends requests to user-service</p>
 *
 * @author Artur Malashkov
 * @see Role
 * @see User
 * @since 17
 */
@FeignClient(name = "user-service", url = "${feign.user-client.uri}")
public interface UserFeignClient {

    /**
     * This method is created to pass the token to the service, which, after processing on its service,
     * returns the user in accordance with the token that we passed.
     *
     * @param token this is the token of the user who is already logged in.
     * @return the response body that will contain the user.
     */
    @GetMapping("/api/auth/user/{token}")
    ResponseEntity<User> getUserByToken(@PathVariable String token);

}
