package com.auspost.demo.validator;

import com.auspost.demo.exception.ServiceException;
import com.auspost.demo.model.SuburbDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.auspost.demo.exception.ServiceError.REQUEST_INVALID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SuburbValidatorTest {

    @Mock
    PostCodeValidator postCodeValidator;
    @InjectMocks
    private SuburbValidator validator;
    private SuburbDetails suburbDetails;
    private static Integer POST_CODE = 1234;

    @Test
    public void shouldValidateSuccessfully() {
        suburbDetails = new SuburbDetails("test", POST_CODE, "VIC", "");
        assertDoesNotThrow(() -> validator.validate(suburbDetails));
        verify(postCodeValidator, times(1)).validate(POST_CODE);
    }

    @Test
    public void shouldThrowExceptionForEmptyName() {
        suburbDetails = new SuburbDetails("", POST_CODE, "VIC", "");
        ServiceException serviceException = assertThrows(ServiceException.class, () -> validator.validate(suburbDetails));
        assertEquals(REQUEST_INVALID, serviceException);
        verify(postCodeValidator, times(1)).validate(POST_CODE);
    }

    @Test
    public void shouldThrowExceptionForEmptyState() {
        suburbDetails = new SuburbDetails("name", POST_CODE, "", "");
        ServiceException serviceException = assertThrows(ServiceException.class, () -> validator.validate(suburbDetails));
        assertEquals(REQUEST_INVALID, serviceException);
        verify(postCodeValidator, times(1)).validate(POST_CODE);
    }
}
