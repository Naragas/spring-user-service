package ru.naragas.springuserservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.naragas.springuserservice.dto.CreateUserDTO;
import ru.naragas.springuserservice.dto.UpdateUserDTO;
import ru.naragas.springuserservice.dto.UserDTO;
import ru.naragas.springuserservice.exception.EmailAlreadyExistsException;
import ru.naragas.springuserservice.exception.UserNotFoundException;
import ru.naragas.springuserservice.service.UserService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * @author Naragas
 * @version 1.0
 * @created 8/17/2025
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired()
    private ObjectMapper objectMapper;


    private final int userIdDoesNotExist = Integer.MAX_VALUE;
    private final String notUniqueEmail = "thirdTestUser@gmail.com";

    private UserDTO firstTestUserDTO;
    private UserDTO secondTestUserDTO;
    private UserDTO thirdTestUserDTO;
    private UserDTO firstTestUserAfterUpdateDTO;

    private List<UserDTO> userDTOList;

    private CreateUserDTO createUserDTO;
    private UpdateUserDTO updateUserDTO;


    @BeforeEach
    void setUp() {
        final int firstTestUserId = 1;
        final String firstTestUserName = "firstTestUser";
        final String firstTestUserMail = "firstTestUser@gmail.com";
        final int firstTestUserAge = 55;
        final LocalDateTime firstTestUserCreatedAt = LocalDateTime.parse("2025-08-17T05:58:27.301825");

        final int secondTestUserId = 2;
        final String secondTestUserName = "secondTestUser";
        final String secondTestUserMail = "secondTestUser@gmail.com";
        final int secondTestUserAge = 44;
        final LocalDateTime secondTestUserCreatedAt = LocalDateTime.parse("2025-08-16T05:58:27.301825");

        final int thirdTestUserId = 3;
        final String thirdTestUserName = "thirdTestUser";
        final String thirdTestUserMail = "thirdTestUser@gmail.com";
        final int thirdTestUserAge = 23;
        final LocalDateTime thirdTestUserCreatedAt = LocalDateTime.parse("2025-08-14T05:58:27.301825");


        final String fourthTestUserName = "fourthTestUser";
        final String fourthTestUserMail = "fourthTestUser@gmail.com";
        final int fourthTestUserAge = 43;

        firstTestUserDTO = new UserDTO(firstTestUserId, firstTestUserName,
                firstTestUserMail, firstTestUserAge, firstTestUserCreatedAt);
        secondTestUserDTO = new UserDTO(secondTestUserId, secondTestUserName,
                secondTestUserMail, secondTestUserAge, secondTestUserCreatedAt);
        userDTOList = Arrays.asList(firstTestUserDTO, secondTestUserDTO);

        createUserDTO = new CreateUserDTO(thirdTestUserName, thirdTestUserMail, thirdTestUserAge);
        thirdTestUserDTO = new UserDTO(thirdTestUserId, thirdTestUserName,
                thirdTestUserMail, thirdTestUserAge, thirdTestUserCreatedAt);

        updateUserDTO = new UpdateUserDTO(fourthTestUserName, fourthTestUserMail, fourthTestUserAge);
        firstTestUserAfterUpdateDTO = new UserDTO(firstTestUserId, fourthTestUserName,
                fourthTestUserMail, fourthTestUserAge, firstTestUserCreatedAt);
    }

    @Test
    @DisplayName("GET /api/users/all - Should return all users in DB")
    void getAllUsersShouldReturnAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(userDTOList);

        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(firstTestUserDTO.getId()))
                .andExpect(jsonPath("$[1].id").value(secondTestUserDTO.getId()));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("GET /api/users/all - Should return empty list when no users exist")
    void getAllUsersShouldReturnEmptyListWhenNoUsersExist() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(0));

        verify(userService, times(1)).getAllUsers();
    }


    @Test
    @DisplayName("GET /api/users/{id} - Should return user by ID, when user exist")
    void getUserByIdShouldReturnUserWhenUserExist() throws Exception {
        when(userService.getUserById(firstTestUserDTO.getId())).thenReturn(firstTestUserDTO);

        mockMvc.perform(get("/api/users/{id}", firstTestUserDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(firstTestUserDTO.getId()))
                .andExpect(jsonPath("$.name").value(firstTestUserDTO.getName()))
                .andExpect(jsonPath("$.email").value(firstTestUserDTO.getEmail()))
                .andExpect(jsonPath("$.age").value(firstTestUserDTO.getAge()))
                .andExpect(jsonPath("$.createdAt").value(firstTestUserDTO.getCreatedAt().toString()));

        verify(userService, times(1)).getUserById(firstTestUserDTO.getId());
    }

    @Test
    @DisplayName("GET /api/users/{id} - Should return User Not Found, when user does not exist")
    void getUserByIdShouldReturnUserNotFoundWhenUserDoesNotExist() throws Exception {
        when(userService.getUserById(userIdDoesNotExist)).thenThrow(new UserNotFoundException(userIdDoesNotExist));

        mockMvc.perform(get("/api/users/{id}", userIdDoesNotExist))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(userIdDoesNotExist);
    }

    @Test
    @DisplayName("POST /api/users/new - Should return new User")
    void createUserShouldCreateNewUser() throws Exception {
        when(userService.createUser(any(CreateUserDTO.class))).thenReturn(thirdTestUserDTO);

        mockMvc.perform(post("/api/users/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(createUserDTO.getName()))
                .andExpect(jsonPath("$.email").value(createUserDTO.getEmail()))
                .andExpect(jsonPath("$.age").value(createUserDTO.getAge()));

        ArgumentCaptor<CreateUserDTO> captor = ArgumentCaptor.forClass(CreateUserDTO.class);
        verify(userService, times(1)).createUser(captor.capture());
        CreateUserDTO captured = captor.getValue();

        assertEquals(createUserDTO.getName(), captured.getName());
        assertEquals(createUserDTO.getEmail(), captured.getEmail());
        assertEquals(createUserDTO.getAge(), captured.getAge());
    }

    @Test
    @DisplayName("POST /api/users/new - Should return Email address already exists if the email address is not unique.")
    void createUserShouldCreateNewUserIfEmailAddressAlreadyExists() throws Exception {
        doThrow(new EmailAlreadyExistsException(notUniqueEmail))
                .when(userService).createUser(any(CreateUserDTO.class));

        mockMvc.perform(post("/api/users/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isConflict());

        verify(userService, times(1)).createUser(any(CreateUserDTO.class));
    }



    @Test
    @DisplayName("PUT /api/users/update/{id} - Should return user with updated field")
    void updateUserShouldUpdateFieldAndReturnUser() throws Exception {
        when(userService.updateUser(eq(firstTestUserDTO.getId()), any(UpdateUserDTO.class))).thenReturn(firstTestUserAfterUpdateDTO);

        mockMvc.perform(put("/api/users/update/{id}", firstTestUserDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(firstTestUserDTO.getId()))
                .andExpect(jsonPath("$.name").value(updateUserDTO.getName()))
                .andExpect(jsonPath("$.email").value(updateUserDTO.getEmail()))
                .andExpect(jsonPath("$.age").value(updateUserDTO.getAge()))
                .andExpect(jsonPath("$.createdAt").value(firstTestUserDTO.getCreatedAt().toString()));

        verify(userService, times(1)).updateUser(eq(firstTestUserDTO.getId()), any(UpdateUserDTO.class));
    }

    @Test
    @DisplayName("PUT /api/users/update/{id} - Should return User Not Found, when id does not exist in DB")
    void updateUserShouldReturnUserNotFoundWhenIdDoesNotExistInDB() throws Exception {
        doThrow(new UserNotFoundException(userIdDoesNotExist))
                .when(userService).updateUser(eq(userIdDoesNotExist), any(UpdateUserDTO.class));

        mockMvc.perform(put("/api/users/update/{id}", userIdDoesNotExist)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserDTO)))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).updateUser(eq(userIdDoesNotExist), any(UpdateUserDTO.class));
    }

    @Test
    @DisplayName("PUT /api/users/update/{id} - Should return Email address already exists " +
            "if the email address is not unique.")
    void updateUserShouldUpdateEmailAddressIfEmailAddressAlreadyExistsInDB() throws Exception {
        doThrow(new EmailAlreadyExistsException(notUniqueEmail))
                .when(userService).updateUser(eq(firstTestUserDTO.getId()), any(UpdateUserDTO.class));

        mockMvc.perform(put("/api/users/update/{id}", firstTestUserDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDTO)))
                .andExpect(status().isConflict());

        verify(userService, times(1)).updateUser(eq(firstTestUserDTO.getId()), any(UpdateUserDTO.class));
    }



    @Test
    @DisplayName("DELETE /api/users/delete/{id} - should delete user by id, when id exist in DB")
    void deleteUserShouldDeleteUserByIdWhenUserExistInDB() throws Exception {
        doNothing().when(userService).deleteUser(secondTestUserDTO.getId());

        mockMvc.perform(delete("/api/users/delete/{id}", secondTestUserDTO.getId()))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(secondTestUserDTO.getId());
    }

    @Test
    @DisplayName("DELETE /api/users/delete/{id} - should return User Not Found, when id does not exist in DB")
    void deleteUserShouldReturnUserNotFoundWhenUserDoesNotExistInDB() throws Exception {
        doThrow(new UserNotFoundException(userIdDoesNotExist)).when(userService).deleteUser(userIdDoesNotExist);

        mockMvc.perform(delete("/api/users/delete/{id}", userIdDoesNotExist))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).deleteUser(userIdDoesNotExist);
    }

}
