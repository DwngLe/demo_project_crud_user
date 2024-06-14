package com.example.demo_crud_user.service;

import com.example.demo_crud_user.dto.request.*;
import com.example.demo_crud_user.dto.response.UserResponse;
import com.example.demo_crud_user.enity.User;
import com.example.demo_crud_user.exception.AppException;
import com.example.demo_crud_user.exception.ErrorCode;
import com.example.demo_crud_user.mapper.UserMapper;
import com.example.demo_crud_user.repository.UserRepository;
import com.example.demo_crud_user.specification.MongoSpecification;
import com.example.demo_crud_user.specification.UserSpecifications;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    private UserMapper userMapper;

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public UserResponse addUser(UserCreationRequest userCreationRequest) {
        if(userRepository.existsByUsername(userCreationRequest.getUsername())){
            throw  new AppException(ErrorCode.USER_EXISTED);
        }
        if (!userCreationRequest.getPassword().equals(userCreationRequest.getRepeatPassword())) {
            throw new AppException(ErrorCode.PASSWORD_MISS_MATCH);
        }
        User user = userMapper.toUser(userCreationRequest);
        user.setId(UUID.randomUUID().toString());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userMapper.toUserResponse(user);
    }

    @Override
    public void deleteUser(String id) {
        if(!userRepository.existsById(id)){
            throw  new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserResponse updateUser(String id, UserUpdateRequest userUpdateRequest) {
        User user = unwrapUser(userRepository.findById(id));
       userMapper.updateUser(user, userUpdateRequest);
      return userMapper.toUserResponse( userRepository.save(user));

    }

    @Override
    public Page<UserResponse> findUsers(String keyword, int pageNo, int pageSize) {
        Page<User> pageUser = userRepository.findByNameContaining(keyword, PageRequest.of(pageNo, pageSize));
        if(pageUser.getContent().isEmpty()){
            throw  new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        return pageUser.map(userMapper::toUserResponse);
    }

    @Override
    public UserResponse getUserById(String id) {
        return userMapper.toUserResponse(unwrapUser(userRepository.findById(id)));
    }

    @Override
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String idUser = context.getAuthentication().getName();
        User user = userRepository.findById(idUser).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    static User unwrapUser(Optional<User> enity){
        if(enity.isPresent()) return enity.get();
        else throw  new RuntimeException();
    }
}
