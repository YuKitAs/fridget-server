package edu.kit.pse.fridget.server.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ExceptionResponseBody> handleInvalidFormatException(InvalidFormatException ex) {
        return new ResponseEntity<>(new ExceptionResponseBody(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponseBody> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(new ExceptionResponseBody(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityUnprocessableException.class)
    public ResponseEntity<ExceptionResponseBody> handleEntityUnprocessableException(EntityUnprocessableException ex) {
        return new ResponseEntity<>(new ExceptionResponseBody(ex.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(EntityConflictException.class)
    public ResponseEntity<ExceptionResponseBody> handleEntityConflictException(EntityConflictException ex) {
        return new ResponseEntity<>(new ExceptionResponseBody(ex.getMessage()), HttpStatus.CONFLICT);
    }
}
