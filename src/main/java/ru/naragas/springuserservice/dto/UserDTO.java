package ru.naragas.springuserservice.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Naragas
 * @version 1.0
 * @created 8/17/2025
 */


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    @Schema(description = "User ID", example = "1")
    private int id;

    @Schema(description = "User name", example = "Ivan")
    private String name;

    @Schema(description = "User email", example = "ivan@example.com")
    private String email;

    @Schema(description = "User Age", example = "34")
    private int age;

    private LocalDateTime createdAt;
}
