package ru.clevertec.newsservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <d>DTO Entity NewsDTO</d>
 *
 *  @author Artur Malashkov
 *  @since 17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO {


    private Long id;

    @Schema(description = "News_title", example = "Economy resumes growth")
    @NotBlank(message = "no title")
    private String title;

    @Schema(description = "News_text",
            example = "After several months of decline, the economy is finally showing signs of recovery")
    @NotBlank(message = "no text")
    private String text;

    @Schema(description = "User name",
            example = "The name of the user who created the news")
    @NotBlank(message = "no userName")
    private String userName;

    private LocalDateTime time;
}
