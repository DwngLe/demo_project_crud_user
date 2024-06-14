package com.example.demo_crud_user.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Objects;

@Getter
@Setter
public class PageRequestDTO {
    private Integer pageNo = 0;
    private Integer pageSize = 10;

}
