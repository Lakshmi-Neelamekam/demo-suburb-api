package com.auspost.demo.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static com.auspost.demo.exception.ServiceError.REQUEST_INVALID;
import static com.auspost.demo.exception.ServiceError.SERVER_ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class ServiceExceptionHandlerTest {

    private ServiceExceptionHandler exceptionHandler;

    @BeforeEach
    public void init() {
        exceptionHandler = new ServiceExceptionHandler();
    }

    @Test
    public void shouldWorkForServiceException() {

        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.serviceExceptionHandler(REQUEST_INVALID);
        ErrorResponse errorResponse = new ErrorResponse(REQUEST_INVALID.getErrorCode(), REQUEST_INVALID.getErrorDescription());
        assertAll(() -> {
           assertNotNull(responseEntity);
           assertNotNull(responseEntity.getBody());
           assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
           assertEquals(errorResponse.getCode(), responseEntity.getBody().getCode());
           assertEquals(errorResponse.getMessage(), responseEntity.getBody().getMessage());
        });
    }

    @Test
    public void shouldWorkForAnyException() {

        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.catchAllExceptionHandler(new RuntimeException("some error"));
        ErrorResponse errorResponse = new ErrorResponse(SERVER_ERROR.getErrorCode(), SERVER_ERROR.getErrorDescription());
        assertAll(() -> {
            assertNotNull(responseEntity);
            assertNotNull(responseEntity.getBody());
            assertEquals(INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
            assertEquals(errorResponse.getCode(), responseEntity.getBody().getCode());
            assertEquals(errorResponse.getMessage(), responseEntity.getBody().getMessage());
        });
    }
}
