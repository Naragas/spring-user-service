package ru.naragas.springuserservice.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * @author Naragas
 * @version 1.0
 * @created 8/17/2025
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserDTO {
    @NotBlank
    @Schema(description = "User name", example = "Ivan")
    private String name;

    @Email
    @NotBlank
    @Schema(description = "User email", example = "ivan@example.com")
    private String email;

    @Min(1)
    @Schema(description = "User Age", example = "34")
    private int age;
}
