package com.auspost.demo.validator;

import com.auspost.demo.exception.ServiceError;
import com.auspost.demo.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PostCodeValidatorTest {
    private PostCodeValidator validator ;

    @BeforeEach
    public void init() {
        validator = new PostCodeValidator();
    }

    @Test
    public void shouldInvokeSuccessfullyForValidPostCode() {
        assertDoesNotThrow(() -> validator.validate(1234));
    }

    @Test
    public void shouldThrowExceptionForInvalidPostCode() {
        ServiceException serviceException = assertThrows(ServiceException.class, () -> validator.validate(34567));
        assertEquals(ServiceError.REQUEST_INVALID, serviceException);
    }
}
