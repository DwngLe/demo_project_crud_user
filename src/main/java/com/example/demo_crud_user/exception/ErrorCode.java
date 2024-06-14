package com.example.demo_crud_user.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

    //for creation sth
    INVALID_FIELD(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(1002, "Username must be at least {min} characters, cannot contain special characters and cannot contain capital words", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1003, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_DOB(1004, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    INVALID_NAME(1005, "Invalid name", HttpStatus.BAD_REQUEST),
    PASSWORD_MISS_MATCH(1006, "Password and repeat password miss match", HttpStatus.BAD_REQUEST),
    REPEAT_PASSWORD_CANNOT_BE_EMPTY(1007, "Repeat password cannot be empty", HttpStatus.BAD_REQUEST),


    //for exist or not exist
    USER_EXISTED(2000, "User existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(2001, "User not existed", HttpStatus.NOT_FOUND),
    OPERATION_NOT_SUPPORTED(2002, "Operation not supported", HttpStatus.BAD_REQUEST),

    //for authen and author
    UNAUTHENTICATED(3000, "Cannot Authenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(3001, "You do not have permission", HttpStatus.FORBIDDEN)
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}