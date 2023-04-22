package com.login.exceptions;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.NonUniqueResultException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleNotKnownTypeException(Exception ex){
        return new ResponseEntity<>(new ExceptionResponse(ex.getMessage(),false),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException ex){
        return new ResponseEntity<>(new ExceptionResponse(ex.getMessage(),false),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<ExceptionResponse> handleUserAlreadyExistException(UserExistException ex){
        return new ResponseEntity<>(new ExceptionResponse(ex.getMessage(),false),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<ExceptionResponse> handleUserDoesNotExistException(UserDoesNotExistException ex){
        return new ResponseEntity<>(new ExceptionResponse(ex.getMessage(), false),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(NonUniqueResultException.class)
    public ResponseEntity<ExceptionResponse> handleNonUniqueResultException(NonUniqueResultException ex){
        return new ResponseEntity<>(new ExceptionResponse("Contains Multiple Results",false),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    public ResponseEntity<ExceptionResponse> handleIncorrectResultSizeDataAccessException(IncorrectResultSizeDataAccessException ex){
        return new ResponseEntity<>(new ExceptionResponse("Contains Multiple Results",false),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleMethodArgsNotValidException(MethodArgumentNotValidException ex)
    {
        Map<String,String> resp=new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName = ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            resp.put(fieldName, message);
        });
        return new ResponseEntity<Map<String,String>>(resp, HttpStatus.BAD_REQUEST);
    }
}
