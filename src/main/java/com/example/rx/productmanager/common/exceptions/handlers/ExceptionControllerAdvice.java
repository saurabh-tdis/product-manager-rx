package com.example.rx.productmanager.common.exceptions.handlers;

import com.example.rx.productmanager.common.exceptions.EntityDuplicateException;
import com.example.rx.productmanager.domain.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

//@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(EntityDuplicateException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleDuplicateException(EntityDuplicateException ex){
        return Mono.just(new ResponseEntity(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponse>> processValidationError(WebExchangeBindException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        String message = fieldErrors
                .stream()
                .map(f-> f.getDefaultMessage())
                .collect(Collectors.joining());
        return Mono.just(new ResponseEntity(new ErrorResponse(message), HttpStatus.BAD_REQUEST));
    }
}
