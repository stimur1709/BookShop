package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.errors.AdminException;
import com.example.mybookshopapp.errors.RestDefaultException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandlerController {

    @ExceptionHandler
    public ResponseEntity<String> handlerDeleteException(RestDefaultException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handlerDeleteException(DataAccessException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getLocalizedMessage());
    }

    @ExceptionHandler(AdminException.class)
    public ResponseEntity<String> handlerAdminUserException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещен");
    }

}
