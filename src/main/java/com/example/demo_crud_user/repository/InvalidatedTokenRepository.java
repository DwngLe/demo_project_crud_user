package com.example.demo_crud_user.repository;

import com.example.demo_crud_user.enity.InvalidatedToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvalidatedTokenRepository extends MongoRepository<InvalidatedToken, String> {
}
