package com.login.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.NonUniqueResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
//    @ExceptionHandler(AuthenticationException.class)
//    public void handleAuthenticationException(AuthenticationException authException, HttpServletResponse response, HttpServletRequest request) throws IOException {
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//
//        final Map<String, Object> body = new LinkedHashMap<>();
//        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
//        body.put("error", "Unauthorized");
//        body.put("message", authException.getMessage());
//        body.put("path", request.getServletPath());
//
//        final ObjectMapper mapper = new ObjectMapper();
//        mapper.writeValue(response.getOutputStream(), body);
//        log.info("Sended error");
//    }
}
