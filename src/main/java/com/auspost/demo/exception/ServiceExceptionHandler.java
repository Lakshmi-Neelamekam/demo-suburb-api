package com.auspost.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.auspost.demo.exception.ServiceError.SERVER_ERROR;

@ControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> serviceExceptionHandler(ServiceException ex) {
        ErrorResponse errorResponse =
                new ErrorResponse(ex.getErrorCode(), ex.getErrorDescription());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> catchAllExceptionHandler(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(SERVER_ERROR.getErrorCode(), SERVER_ERROR.getErrorDescription());
        return new ResponseEntity<>(errorResponse, SERVER_ERROR.getStatus());
    }

}
