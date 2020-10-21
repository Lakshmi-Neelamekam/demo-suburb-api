package com.auspost.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ServiceException extends RuntimeException {
    private HttpStatus status;
    private String errorCode;
    private String errorDescription;
}
