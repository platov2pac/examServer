package com.exams.service.error.processing;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ErrorProcessingHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    private ResponseEntity<AwesomeException> handleBadRequestException(BadRequestException be) {
        return new ResponseEntity<>(
                new AwesomeException(be.message),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    private ResponseEntity<AwesomeException> handleNotFoundException(NotFoundException nfe) {
        return new ResponseEntity<>(
                new AwesomeException(nfe.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @Data
    @AllArgsConstructor
    private static class AwesomeException {
        private String message;
    }
}
