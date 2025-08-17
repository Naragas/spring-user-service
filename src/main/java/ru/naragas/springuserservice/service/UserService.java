package ru.naragas.springuserservice.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import ru.naragas.springuserservice.dto.CreateUserDTO;
import ru.naragas.springuserservice.dto.UpdateUserDTO;
import ru.naragas.springuserservice.dto.UserDTO;
import ru.naragas.springuserservice.entity.User;
import ru.naragas.springuserservice.exception.UserNotFoundException;
import ru.naragas.springuserservice.mapper.UserMapper;
import ru.naragas.springuserservice.repository.UserRepository;

import java.util.List;

/**
 * @author Naragas
 * @version 1.0
 * @created 8/17/2025
 */
@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::entityToDTO)
                .toList();
    }

    public UserDTO getUserById(int id) {
        var user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(id));
        return userMapper.entityToDTO(user);
    }

    public UserDTO createUser(CreateUserDTO createUserDTO) {
        User user = userMapper.createDTOToEntity(createUserDTO);
        userRepository.save(user);
        return userMapper.entityToDTO(user);
    }

    public UserDTO updateUser(int id, UpdateUserDTO updateUserDTO) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setName(updateUserDTO.getName());
        user.setEmail(updateUserDTO.getEmail());
        user.setAge(updateUserDTO.getAge());
        userRepository.save(user);

        return userMapper.entityToDTO(user);
    }

    public void deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }
}
