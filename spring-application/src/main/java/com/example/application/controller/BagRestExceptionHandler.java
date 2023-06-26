package com.example.application.controller;


import com.example.application.service.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
public class BagRestExceptionHandler {


    @ExceptionHandler(EntityNotFoundException.class)
    Mono<ProblemDetail> entityNotFoundException(EntityNotFoundException entf) {
        log.debug("hadling exception :: {}", entf.getMessage());
        return Mono.just(
                ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, entf.getLocalizedMessage())
        );
    }
}
