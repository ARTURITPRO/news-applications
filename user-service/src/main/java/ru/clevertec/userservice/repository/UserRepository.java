package ru.clevertec.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.userservice.dao.User;

import java.util.Optional;

/**
 * <p> User repository. </p>
 *
 * @author Artur Malashkov
 * @since 17
 */

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Returns the User by username.
     *
     * @param username its name user.
     * @return Optional User
     */
    Optional<User> findByUsername(String username);

}
