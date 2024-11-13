package org.example.msvcauth.repositories;

import org.example.msvcauth.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends MongoRepository<User, String> {
    @Query("{ 'roles': 'ROLE_CLIENTE' }")
    List<User> getAllClientes(String roleName);

    @Query("{ '_id': ?0, 'roles': 'ROLE_CLIENTE'}")
    Optional<User> findCliente(String id);
}
