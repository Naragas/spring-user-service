package ru.naragas.springuserservice.mapper;


import org.springframework.stereotype.Component;
import ru.naragas.springuserservice.dto.CreateUserDTO;
import ru.naragas.springuserservice.dto.UserDTO;
import ru.naragas.springuserservice.entity.User;

/**
 * @author Naragas
 * @version 1.0
 * @created 8/17/2025
 */
@Component
public class UserMapper {
    public UserDTO entityToDTO(User user) {
        if(user == null) {
         return null;
        }
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getAge(), user.getCreatedAt());
    }

    public User createDTOToEntity(CreateUserDTO createUserDTO) {
        if(createUserDTO == null) {
            return null;
        }
        return new User(createUserDTO.getName(), createUserDTO.getEmail(), createUserDTO.getAge());
    }

    public User dtoToEntity(UserDTO userDTO) {
        if(userDTO == null) {
            return null;
        }
        var user = new User();

        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setAge(userDTO.getAge());
        user.setCreatedAt(userDTO.getCreatedAt());

        return user;
    }


}
