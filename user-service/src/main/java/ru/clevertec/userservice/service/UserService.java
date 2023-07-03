package ru.clevertec.userservice.service;

import lombok.RequiredArgsConstructor;
import ru.clevertec.userservice.dao.User;
import org.springframework.stereotype.Service;
import ru.clevertec.userservice.repository.UserRepository;

import java.util.Optional;

/**
 * <p> Service implementation for managing users. </p>
 *
 * @author Artur Malashkov
 * @see User
 * @see UserRepository
 * @since 17
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
