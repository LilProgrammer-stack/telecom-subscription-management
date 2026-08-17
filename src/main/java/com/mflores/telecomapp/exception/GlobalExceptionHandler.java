package com.mflores.telecomapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

//We must use ResponseEntity since we are handling a controller, it returns http status
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> resourceNotFoundException(ResourceNotFoundException e) {

        ApiError apiError = new ApiError("Resource Not Found",
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> noValidException(MethodArgumentNotValidException e){

        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError: e.getBindingResult().getFieldErrors()){
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ApiError apiError = new ApiError("Validation Failed",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                errors);

        return new ResponseEntity<>(apiError,HttpStatus.BAD_REQUEST);
    }
}
