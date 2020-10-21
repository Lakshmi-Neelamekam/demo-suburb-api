package com.auspost.demo.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public interface ServiceError {
 ServiceException REQUEST_INVALID = new ServiceException(BAD_REQUEST, "AUS001", "Request is invalid.");
 ServiceException SERVER_ERROR = new ServiceException(INTERNAL_SERVER_ERROR, "AUS002", "Exception occurred while processing your request.");
}
