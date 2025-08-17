package ru.naragas.springuserservice.dto;


import lombok.*;

/**
 * @author Naragas
 * @version 1.0
 * @created 8/17/2025
 */

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserDTO {
    private String name;
    private String email;
    private int age;
}
