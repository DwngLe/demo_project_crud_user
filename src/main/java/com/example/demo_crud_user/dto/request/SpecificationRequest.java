package com.example.demo_crud_user.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@Setter
public class SpecificationRequest {
    private List<SearchRequest> searchRequest;
    private PageRequestDTO pageRequestDTO;
    private Sort.Direction sort = Sort.Direction.ASC;
    private String sortByColumn = "_id";
}
