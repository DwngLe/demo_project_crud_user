package com.example.demo_crud_user.repository;

import com.example.demo_crud_user.dto.request.PageRequestDTO;
import com.example.demo_crud_user.enity.User;
import com.example.demo_crud_user.specification.MongoSpecification;
import com.example.demo_crud_user.specification.MongoSpecificationBuilder;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@AllArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom{
    private MongoTemplate mongoTemplate;
    @Override
    public Page<User> findBySpecifications(List<MongoSpecification> specs, Pageable pageable) {
        MongoSpecificationBuilder builder = new MongoSpecificationBuilder();
        for (MongoSpecification spec : specs) {
            builder.with(spec);
        }
        Criteria criteria = builder.build();
        Query query = new Query(criteria);
        query.with(pageable);


        List<User> users = mongoTemplate.find(query, User.class);
        long count = mongoTemplate.count(query.skip(-1).limit(-1), User.class);
        return PageableExecutionUtils.getPage(users, pageable, () -> count);
    }
}
