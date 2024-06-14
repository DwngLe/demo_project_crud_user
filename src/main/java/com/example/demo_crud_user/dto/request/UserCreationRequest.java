package com.example.demo_crud_user.dto.request;

import com.example.demo_crud_user.validation.NameConstraint;
import com.example.demo_crud_user.validation.UsernameConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationRequest {
    @Size(min = 5, message = "INVALID_USERNAME")
    String username;
    @Size(min = 3, message = "INVALID_PASSWORD")
    String password;
    String repeatPassword;
    @NameConstraint(message = "INVALID_NAME")
    String name;
    int age;
    String address;
}
