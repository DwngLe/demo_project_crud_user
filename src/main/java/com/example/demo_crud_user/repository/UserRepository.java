package com.example.demo_crud_user.repository;

import com.example.demo_crud_user.enity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom {
    Boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

    Page<User> findByNameContaining(String keyword, Pageable pageable);

}
