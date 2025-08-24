package ru.naragas.springuserservice.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Naragas
 * @version 1.0
 * @created 8/18/2025
 */

public enum UserEventType {
    @JsonProperty("CreateUser")
    CREATE_USER,
    @JsonProperty("DeleteUser")
    DELETE_USER
}
