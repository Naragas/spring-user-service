package ru.naragas.springuserservice.dto;


import lombok.*;

/**
 * @author Naragas
 * @version 1.0
 * @created 8/18/2025
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@ToString
public class UserEventDTO {
    private String eventType;
    private String email;
}
