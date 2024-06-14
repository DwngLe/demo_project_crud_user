package com.example.demo_crud_user.dto.request;

import com.example.demo_crud_user.enums.Operation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {
    String field;
    String value;
    String operation;
}
