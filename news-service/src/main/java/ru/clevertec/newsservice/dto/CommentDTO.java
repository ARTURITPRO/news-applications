package ru.clevertec.newsservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <d>DTO Entity CommentDTO</d>
 *
 *  @author Artur Malashkov
 *  @since 17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Long id;

    @Schema(description = "Comment content", example = "What a touching story!")
    @NotBlank(message = "no text")
    private String text;

    @Schema(description = "The name of the user who left the comment ", example = "Potter")
    @NotBlank(message = "no username ")
    private String userName;

}
