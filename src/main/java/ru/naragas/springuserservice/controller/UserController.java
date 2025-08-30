package ru.naragas.springuserservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.naragas.springuserservice.assemblers.UserModelAssembler;
import ru.naragas.springuserservice.dto.CreateUserDTO;
import ru.naragas.springuserservice.dto.UpdateUserDTO;
import ru.naragas.springuserservice.dto.UserDTO;
import ru.naragas.springuserservice.service.UserService;


import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Naragas
 * @version 1.0
 * @created 8/17/2025
 */


@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Users management")
public class UserController {
    private final UserService userService;
    private final UserModelAssembler userModelAssembler;

    public UserController(UserService userService, UserModelAssembler userModelAssembler) {
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
    }

    @Operation(
            summary = "Get list of all users.",
            description = "Returns a full list of users in database."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List successfully received",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserDTO.class))
                    )
            )
    })
    @GetMapping()
    public CollectionModel<EntityModel<UserDTO>> getAllUsers() {
        return userModelAssembler.toCollectionModel(userService.getAllUsers());
    }

    @Operation(
            summary = "Get user by ID",
            description = "Returns one user by id from database."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully received",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = UserDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User with current id not found in database",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public EntityModel<UserDTO> getUserById(
            @Parameter(description = "Unique user identifier", example = "1")
            @PathVariable int id) {
        UserDTO userDTO = userService.getUserById(id);
        return userModelAssembler.toModel(userDTO);
    }


    @Operation(
            summary = "Update user by ID",
            description = "Updates user data by unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully updated in database",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Incorrect request data",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User with current id not found in database",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email address already exists in database",
                    content = @Content
            )
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<EntityModel<UserDTO>> updateUser(
            @Parameter(description = "Unique user identifier", example = "1")
            @PathVariable int id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Data for updating user by ID",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = UpdateUserDTO.class)
                    )
            )
            @Valid
            @RequestBody UpdateUserDTO updateUserDTO) {
        UserDTO updatedUser = userService.updateUser(id, updateUserDTO);
        EntityModel<UserDTO> userDTOEntityModel = userModelAssembler.toModel(updatedUser);
        return ResponseEntity.ok(userDTOEntityModel);
    }

    @Operation(
            summary = "Add user",
            description = "Add new user in database"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "New User successfully added in database",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Incorrect request data",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email address already exists in database",
                    content = @Content
            )
    })
    @PostMapping()
    public ResponseEntity<EntityModel<UserDTO>> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Data for creating a new user",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = CreateUserDTO.class)
                    )
            )
            @Valid
            @RequestBody CreateUserDTO createUserDTO) {
        UserDTO createdUser = userService.createUser(createUserDTO);
        EntityModel<UserDTO> userDTOEntityModel = userModelAssembler.toModel(createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTOEntityModel);
    }

    @Operation(
            summary = "Delete user by ID",
            description = "Deletes user by unique identifier from database"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "User successfully removed from database"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User with current id not found in database",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "Unique user identifier", example = "1")
            @PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
