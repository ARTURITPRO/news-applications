package ru.clevertec.newsservice.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>This class describes the role</p>
 *
 * @author Artur Malashkov
 * @see RoleEnum
 * @see User
 * @since 17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    private Long id;
    private RoleEnum role;

}
