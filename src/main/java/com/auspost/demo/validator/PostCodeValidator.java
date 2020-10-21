package com.auspost.demo.validator;

import com.auspost.demo.exception.ServiceError;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PostCodeValidator {
    public void validate(Integer postCode) {
        String pattern = "^\\d{4}$";
        if (!Pattern.matches(pattern, postCode.toString())) {
            throw ServiceError.REQUEST_INVALID;
        }
    }
}
