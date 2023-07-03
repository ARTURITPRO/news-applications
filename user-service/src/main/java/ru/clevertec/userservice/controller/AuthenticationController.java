package ru.clevertec.userservice.controller;

import lombok.RequiredArgsConstructor;
import ru.clevertec.userservice.dao.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.userservice.dto.JwtRequest;
import ru.clevertec.userservice.dto.JwtResponse;
import ru.clevertec.userservice.service.AuthenticationService;
import ru.clevertec.userservice.dto.JwtRefreshingRequest;


/**
 * <p> Controller for authentication. </p>
 *
 * @author Artur Malashkov
 * @see AuthenticationService
 * @see User
 * @see JwtRefreshingRequest
 * @see JwtRequest
 * @see JwtResponse
 * @since 17
 */
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * This endpoint was register user in system.
     *
     * @param jwtRequest parameter with user credentials
     * @return JwtResponse with access and refresh tokens
     */
    @PostMapping("register")
    public ResponseEntity<JwtResponse> registerUser(@RequestBody JwtRequest jwtRequest) {
        return ResponseEntity.ok(authenticationService.register(jwtRequest));
    }

    /**
     * This endpoint was returning a user by a token
     *
     * @param token token of authorized user
     * @return authorized user
     */
    @GetMapping("user/{token}")
    public ResponseEntity<User> getUserByToken(@PathVariable String token) {
        return ResponseEntity.ok(authenticationService.getUserByToken(token));
    }

    /**
     * This endpoint  generate new access token.
     *
     * @param request parameter with refresh token
     * @return JwtResponse with new access token
     */
    @PostMapping("token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody JwtRefreshingRequest request) {
        return ResponseEntity.ok(authenticationService.getAccessToken(request.getRefreshingToken()));
    }

    /**
     * This endpoint  generate new refresh token.
     *
     * @param request parameter with refresh token
     * @return JwtResponse with new refresh and access tokens
     */
    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody JwtRefreshingRequest request) {
        return ResponseEntity.ok(authenticationService.refresh(request.getRefreshingToken()));
    }

    /**
     * This endpoint was created to log the user in the system and issue him a token.
     *
     * @param jwtRequest parameter with user data
     * @return responseToken with refresh and access token
     */
    @PostMapping("login")
    public ResponseEntity<JwtResponse> loginUser(@RequestBody JwtRequest jwtRequest) {
        return ResponseEntity.ok(authenticationService.login(jwtRequest));
    }

}
