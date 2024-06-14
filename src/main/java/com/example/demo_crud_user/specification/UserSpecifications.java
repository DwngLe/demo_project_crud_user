package com.example.demo_crud_user.specification;

import com.example.demo_crud_user.enums.Operation;
import com.example.demo_crud_user.exception.AppException;
import com.example.demo_crud_user.exception.ErrorCode;
import org.springframework.data.mongodb.core.query.Criteria;

public class UserSpecifications {
    public static MongoSpecification hasKeyWithOperation(String key, String operation, Object value) {
        return () -> {
            switch (operation.toLowerCase()) {
                case "eq":
                    return Criteria.where(key).is(value);
                case "ne":
                    return Criteria.where(key).ne(value);
                case "gt":
                    return Criteria.where(key).gt(value);
                case "lt":
                    return Criteria.where(key).lt(value);
                case "gte":
                    return Criteria.where(key).gte(value);
                case "lte":
                    return Criteria.where(key).lte(value);
                case "in":
                    return Criteria.where(key).in((Object[]) value);
                case "like":
                    return Criteria.where(key).regex((String) value, "i");
                default:
                    throw new AppException(ErrorCode.OPERATION_NOT_SUPPORTED);
            }
        };
    }
}
