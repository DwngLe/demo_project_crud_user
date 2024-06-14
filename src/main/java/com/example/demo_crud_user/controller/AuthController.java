package com.example.demo_crud_user.controller;

import com.example.demo_crud_user.dto.request.AuthenticationRequest;
import com.example.demo_crud_user.dto.request.RefeshRequest;
import com.example.demo_crud_user.dto.request.UserCreationRequest;
import com.example.demo_crud_user.dto.response.AuthenticationResponse;
import com.example.demo_crud_user.dto.response.UserResponse;
import com.example.demo_crud_user.enity.User;
import com.example.demo_crud_user.exception.APIResponse;
import com.example.demo_crud_user.service.AuthenticationService;
import com.example.demo_crud_user.service.UserService;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private UserService userService;
    private AuthenticationService authenticationService;
    @Operation(
            description = "Người dùng gửi lên các thông tin cần thiết để đăng ký tài khoản, hệ thống sẽ trả về các thông tin cơ bản",
            summary = "Đăng ký tài khoản",
            responses = {
                    @ApiResponse(
                            description = "Thành công",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Thông tin không hợp lệ",
                            responseCode = "405"
                    )
            }
    )
    @PostMapping("/register")
    private APIResponse<UserResponse> register(@Valid @RequestBody UserCreationRequest userCreationRequest){
        return APIResponse.<UserResponse>builder()
                .result(userService.addUser(userCreationRequest))
                .build();
    }

    @Operation(
            description = "Người dùng gửi lên các thông tin cần thiết để đăng nhập tài khoản, hệ thống sẽ trả về token",
            summary = "Đăng nhập tài khoản",
            responses = {
                    @ApiResponse(
                            description = "Thành công",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Thông tin không hợp lệ",
                            responseCode = "405"
                    ),
                    @ApiResponse(
                            description = "Xác thực không thành công",
                            responseCode = "401"
                    )
            }
    )
    @PostMapping("/login")
    private APIResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return APIResponse.<AuthenticationResponse>builder()
                .result(authenticationService.authenticate(request))
                .build();
    }
    @Operation(
            summary = "Đăng xuất tài khoản",
            responses = {
                    @ApiResponse(
                            description = "Thành công",
                            responseCode = "200"
                    )
            }
    )
    @SecurityRequirement(name = "bearAuth")
    @PostMapping("/logout")
    private APIResponse<Void> logout() throws ParseException, JOSEException {
        authenticationService.logout();
        return APIResponse.<Void>builder().build();
    }
    @Operation(
            description = "Người dùng gửi lên token đã hết hạn, hệ thống sẽ trả về token mới (token cũ phải còn trong thời hạn refresh)",
            summary = "Refresh token",
            responses = {
                    @ApiResponse(
                            description = "Thành công",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Xác thực không thành công",
                            responseCode = "401"
                    )
            }
    )
    @SecurityRequirement(name = "bearAuth")
    @PostMapping("/refresh")
    private APIResponse<AuthenticationResponse> refreshToken(@RequestBody RefeshRequest request) throws ParseException, JOSEException {
        return APIResponse.<AuthenticationResponse>builder()
                .result(authenticationService.refeshToken(request))
                .build();
    }


}
