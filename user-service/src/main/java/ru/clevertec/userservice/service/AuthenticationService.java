package ru.clevertec.userservice.service;

import com.example.exception.exceptions.AuthException;
import com.example.exception.exceptions.ResourceNotFoundException;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.clevertec.userservice.mapper.UserMapper;
import ru.clevertec.userservice.dto.JwtRequest;
import ru.clevertec.userservice.dto.JwtResponse;
import ru.clevertec.userservice.dao.User;

import java.util.HashMap;
import java.util.Map;


/**
 * <p> Authenticated service for checking the password and getting new access and refresh tokens to replace
 * the rotten ones. </p>
 *
 * @author Artur Malashkov
 * @see UserMapper
 * @see JwtRequest
 * @see JwtResponse
 * @see User
 * @since 17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserMapper userMapper;
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final Map<String, String> refreshStorage = new HashMap<>();

    public User getUserByToken(String token) {
        Claims claims = jwtProvider.getAccessClaims(token);
        String login = claims.getSubject();
        User user = userService.findByUsername(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found in database"));
        return user;
    }

    public JwtResponse login(@NonNull JwtRequest jwtRequest) {
        log.info("!!!!!!!!!!!!!!!!  UserName : {}", jwtRequest.getUserName());
        User user = userService.findByUsername(jwtRequest.getUserName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found in database"));

        if (user.getPassword().equals(jwtRequest.getUserPassword())) {
            String accessToken = jwtProvider.generateAccessToken(user);
            String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getUsername(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Erroneous password");
        }
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            String login = claims.getSubject();
            String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                User user = userService.findByUsername(login)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found in database"));
                String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            String login = claims.getSubject();
            String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                User user = userService.findByUsername(login)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found in database"));
                String accessToken = jwtProvider.generateAccessToken(user);
                String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getUsername(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Invalid JWT token");
    }

    public JwtResponse register(@NonNull JwtRequest authRequest) {
        User user = userService.save(userMapper.requestToUser(authRequest));
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);
        refreshStorage.put(user.getUsername(), refreshToken);
        return new JwtResponse(accessToken, refreshToken);
    }

}
