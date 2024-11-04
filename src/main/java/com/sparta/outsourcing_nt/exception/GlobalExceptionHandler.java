package com.sparta.outsourcing_nt.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApplicationException.class)
    protected ResponseEntity<ErrorResponseEntity> handleApplicationException(ApplicationException e) {
        return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseEntity> handleValidationExceptions(MethodArgumentNotValidException e) {
        return ErrorResponseEntity.toResponseEntity(
                new ApplicationException(ErrorCode.INVALID_FORMAT, e.getMessage()).getErrorCode()
        );
    }
}