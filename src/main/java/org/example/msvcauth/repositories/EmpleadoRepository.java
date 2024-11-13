package org.example.msvcauth.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.example.msvcauth.domain.User;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmpleadoRepository extends MongoRepository<User, String>{

        @Query("{ 'roles': ?0 }")
        List<User> findAllByRole(String roleName);

        @Query("{ '_id': ?0, 'roles': 'ROLE_EMPLEADO', 'detalleEmpleado': { $ne: null } }")
        Optional<User> findEmpleado(String id);

}
