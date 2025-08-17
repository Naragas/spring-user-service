package ru.naragas.springuserservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.naragas.springuserservice.entity.User;


/**
 * @author Naragas
 * @version 1.0
 * @created 8/17/2025
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
