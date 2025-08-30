package ru.naragas.springuserservice.assemblers;


import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.naragas.springuserservice.controller.UserController;
import ru.naragas.springuserservice.dto.UserDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * @author Naragas
 * @version 1.0
 * @created 8/30/2025
 */
@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserDTO, EntityModel<UserDTO>> {

    @Override
    public EntityModel<UserDTO> toModel(UserDTO userDTO) {
        return EntityModel.of(
                userDTO,
                linkTo(methodOn(UserController.class).getUserById(userDTO.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("users"),
                linkTo(methodOn(UserController.class).updateUser(userDTO.getId(), null)).withRel("update"),
                linkTo(methodOn(UserController.class).deleteUser(userDTO.getId())).withRel("delete")
        );
    }

    @Override
    public CollectionModel<EntityModel<UserDTO>> toCollectionModel(Iterable<? extends UserDTO> users) {
        return RepresentationModelAssembler.super.toCollectionModel(users)
                .add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
    }
}
