package com.example.demo_crud_user.specification;

import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.List;

public class MongoSpecificationBuilder {
    private final List<MongoSpecification> specifications = new ArrayList<>();

    public MongoSpecificationBuilder with(MongoSpecification specification) {
        specifications.add(specification);
        return this;
    }

    public Criteria build() {
        if (specifications.isEmpty()) {
            return new Criteria();
        }
        List<Criteria> criteriaList = new ArrayList<>();
        for (MongoSpecification specification : specifications) {
            criteriaList.add(specification.toCriteria());
        }
        return new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
    }
}
