package com.example.demo_crud_user.dto.request;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefeshRequest {
    private String token;
}
