package com.example.demo_crud_user.mapper;

import com.example.demo_crud_user.dto.request.UserCreationRequest;
import com.example.demo_crud_user.dto.request.UserUpdateRequest;
import com.example.demo_crud_user.dto.response.UserResponse;
import com.example.demo_crud_user.enity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest userCreationRequest);
    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);
    @Mapping(source = "id", target = "id")
    UserResponse toUserResponse(User user);
}
