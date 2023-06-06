package com.muhammadusman92.healthservice.exception;

import com.muhammadusman92.healthservice.payload.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Response> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .status(NOT_FOUND)
                .statusCode(NOT_FOUND.value())
                .message(ex.getMessage())
                .build(), NOT_FOUND);
    }
    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<Response> unauthorizedExceptionHandler(UnAuthorizedException ex){
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message(ex.getMessage())
                .status(FORBIDDEN)
                .statusCode(FORBIDDEN.value())
                .build(), FORBIDDEN);
    }
    @ExceptionHandler(AlreadyExistExeption.class)
    public ResponseEntity<Response> alreadyExistHandler(AlreadyExistExeption ex){
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .status(BAD_REQUEST)
                .statusCode(BAD_REQUEST.value())
                .message(ex.getMessage())
                .build(), BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response>  methodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String,String> map=new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(objectError -> {
            String fieldName = ((FieldError) objectError).getField();
            String fieldError = objectError.getDefaultMessage();
            map.put(fieldName,fieldError);
        });
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .status(BAD_REQUEST)
                .statusCode(BAD_REQUEST.value())
                .data(map)
                .build(),BAD_REQUEST);
    }

}
