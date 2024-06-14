package com.example.demo_crud_user.controller;

import com.example.demo_crud_user.dto.request.SpecificationRequest;
import com.example.demo_crud_user.dto.request.UserCreationRequest;
import com.example.demo_crud_user.dto.request.UserUpdateRequest;
import com.example.demo_crud_user.dto.response.UserResponse;
import com.example.demo_crud_user.enity.User;
import com.example.demo_crud_user.exception.APIResponse;
import com.example.demo_crud_user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @SecurityRequirement(name = "bearAuth")
    @Operation(
            summary = "Tìm kiếm người dùng dựa trên tên người dùng (name)",
            responses = {
                    @ApiResponse(
                            description = "Thành công",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Không tìm thấy kết quả",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Không có quyền truy cập hoặc Token không hợp lệ",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Xác thực không thành công",
                            responseCode = "401"
                    )
            }

    )
    @GetMapping("")
    public APIResponse<Page<UserResponse>> getUsers(@RequestParam String keyword,
                                                    @RequestParam int pageSize,
                                                    @RequestParam int pageNo){
        return APIResponse.<Page<UserResponse>>builder()
                .result(userService.findUsers(keyword, pageNo, pageSize))
                .build();
    }

    @SecurityRequirement(name = "bearAuth")
    @Operation(
            summary = "Lấy ra thông tin cá nhân của người dùng",
            responses = {
                    @ApiResponse(
                            description = "Thành công",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Không tìm thấy kết quả",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Không có quyền truy cập hoặc Token không hợp lệ",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Xác thực không thành công",
                            responseCode = "401"
                    )
            }

    )
    @GetMapping("/{id}")
    public APIResponse<UserResponse> findById(@PathVariable String id) {
        return APIResponse.<UserResponse>builder()
                .result(userService.getUserById(id))
                .build();
    }
    @SecurityRequirement(name = "bearAuth")
    @Operation(
            summary = "Cập nhật thông tin cá nhân của người dùng",
            responses = {
                    @ApiResponse(
                            description = "Thành công",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Không tìm thấy kết quả",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Không có quyền truy cập hoặc Token không hợp lệ",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Xác thực không thành công",
                            responseCode = "401"
                    )
            }

    )
    @PostMapping("/{id}")
    public APIResponse<UserResponse> updateUser(@PathVariable String id
            ,@RequestBody UserUpdateRequest userUpdateRequest) {
        return APIResponse.<UserResponse>builder()
                .result(userService.updateUser(id, userUpdateRequest))
                .build();
    }
    @SecurityRequirement(name = "bearAuth")
    @Operation(
            summary = "Xoá người dùng",
            responses = {
                    @ApiResponse(
                            description = "Thành công",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Không tìm thấy kết quả",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "Không có quyền truy cập hoặc Token không hợp lệ",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Xác thực không thành công",
                            responseCode = "401"
                    )
            }

    )
    @DeleteMapping("/{id}")
    public APIResponse<Void> deleteUser(@PathVariable String id){
        userService.deleteUser(id);
        return APIResponse.<Void>builder()
                .build();
    }






}
