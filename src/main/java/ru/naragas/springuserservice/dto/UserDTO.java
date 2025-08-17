package ru.naragas.springuserservice.dto;


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
    private int id;
    private String name;
    private String email;
    private int age;
    private LocalDateTime createdAt;
}
