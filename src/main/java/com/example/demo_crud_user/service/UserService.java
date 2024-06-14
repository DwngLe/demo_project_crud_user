package com.example.demo_crud_user.service;

import com.example.demo_crud_user.dto.request.SpecificationRequest;
import com.example.demo_crud_user.dto.request.UserCreationRequest;
import com.example.demo_crud_user.dto.request.UserUpdateRequest;
import com.example.demo_crud_user.dto.response.UserResponse;
import com.example.demo_crud_user.enity.User;
import org.springframework.data.domain.Page;

public interface UserService {
    UserResponse addUser(UserCreationRequest userCreationRequest);
    void deleteUser(String id);
    UserResponse updateUser(String id, UserUpdateRequest userUpdateRequest);
    Page<UserResponse> findUsers(String keyword, int pageNo, int pageSize);
    UserResponse getUserById(String id);
    UserResponse getMyInfo();


}
