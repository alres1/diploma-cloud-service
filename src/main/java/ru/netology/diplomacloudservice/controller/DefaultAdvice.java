package ru.netology.diplomacloudservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.netology.diplomacloudservice.dto.ErrorResponse;
import ru.netology.diplomacloudservice.exceptions.FileStNotFoundException;
import ru.netology.diplomacloudservice.exceptions.InputDataException;
import ru.netology.diplomacloudservice.exceptions.UnauthorizedException;

@ControllerAdvice
public class DefaultAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleException(UnauthorizedException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(),401);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InputDataException.class)
    public ResponseEntity<ErrorResponse> handleException(InputDataException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(),400);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileStNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(FileStNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(),500);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}