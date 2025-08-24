package ru.naragas.springuserservice.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import ru.naragas.springuserservice.dto.UserEventType;
import ru.naragas.springuserservice.dto.CreateUserDTO;
import ru.naragas.springuserservice.dto.UpdateUserDTO;
import ru.naragas.springuserservice.dto.UserDTO;
import ru.naragas.springuserservice.dto.UserEventDTO;
import ru.naragas.springuserservice.entity.User;
import ru.naragas.springuserservice.exception.EmailAlreadyExistsException;
import ru.naragas.springuserservice.exception.UserNotFoundException;
import ru.naragas.springuserservice.kafka.UserEventProducer;
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
    private final UserEventProducer userEventProducer;
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
        validateEmailUniqueness(createUserDTO.getEmail());

        User user = userMapper.createDTOToEntity(createUserDTO);
        userRepository.save(user);
        userEventProducer.sendUserEvent(new UserEventDTO(UserEventType.CREATE_USER, user.getEmail()));
        return userMapper.entityToDTO(user);
    }

    public UserDTO updateUser(int id, UpdateUserDTO updateUserDTO) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        validateEmailUniqueness(updateUserDTO.getEmail(), user);

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
        String userEmail = userRepository.findById(id).get().getEmail();
        userRepository.deleteById(id);
        userEventProducer.sendUserEvent(new UserEventDTO(UserEventType.DELETE_USER, userEmail));
    }

    /**
     * Проверяет, что email уникален при создании нового пользователя.
     * Выбрасывает EmailAlreadyExistsException, если такой email уже есть в базе.
     */
    private void validateEmailUniqueness(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
    }

    /**
     * Проверяет, что email уникален при обновлении пользователя.
     * Выбрасывает EmailAlreadyExistsException, если другой пользователь уже имеет такой email.
     *
     * @param email новый email для проверки
     * @param updatedUser текущий пользователь, которого обновляем
     */
    private void validateEmailUniqueness(String email, User updatedUser) {
        if (!(updatedUser.getEmail().equals(email)) && userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
    }
}
