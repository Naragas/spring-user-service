package ru.naragas.springuserservice;


/**
 * @author Naragas
 * @version 1.0
 * @created 8/18/2025
 */

public enum UserEventType {
    CreateUser("CreateUser"),
    UpdateUser("UpdateUser"),
    DeleteUser("DeleteUser"),
    ;

    UserEventType(String createUSer) {
    }
}
