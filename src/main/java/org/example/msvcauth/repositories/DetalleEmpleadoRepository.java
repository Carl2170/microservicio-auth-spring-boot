package org.example.msvcauth.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.example.msvcauth.domain.DetalleEmpleado;


public interface DetalleEmpleadoRepository extends MongoRepository<DetalleEmpleado, String> {

}
