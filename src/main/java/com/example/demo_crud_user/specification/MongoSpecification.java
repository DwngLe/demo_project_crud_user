package com.example.demo_crud_user.specification;

import org.springframework.data.mongodb.core.query.Criteria;

public interface MongoSpecification {
    Criteria toCriteria();
}
