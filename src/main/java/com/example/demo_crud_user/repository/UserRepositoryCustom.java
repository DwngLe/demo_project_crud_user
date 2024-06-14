package com.example.demo_crud_user.repository;

import com.example.demo_crud_user.dto.request.PageRequestDTO;
import com.example.demo_crud_user.enity.User;
import com.example.demo_crud_user.specification.MongoSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface UserRepositoryCustom {
   Page<User> findBySpecifications(List<MongoSpecification> specs, Pageable pageable);
}
