package org.example.msvcauth.repositories;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import org.example.msvcauth.domain.User;

public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom {

    //Optional<User> findByNombre(String email);
    User findByEmail(String email );

}
