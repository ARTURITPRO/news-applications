package ru.clevertec.userservice.dao;

import jakarta.persistence.*;
import lombok.*;

/**
 * <p> Entity representing a user Role  in the database. </p>
 *
 * @author Artur Malashkov
 * @since 17
 */
@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private RoleEnum role;

}
