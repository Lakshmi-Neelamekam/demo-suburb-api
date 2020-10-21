package com.auspost.demo.validator;

import com.auspost.demo.model.SuburbDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.auspost.demo.exception.ServiceError.REQUEST_INVALID;
import static org.springframework.util.StringUtils.isEmpty;

@Component
public class SuburbValidator {
    @Autowired
    private PostCodeValidator postCodeValidator;

    public void validate(SuburbDetails suburbDetails) {
        postCodeValidator.validate(suburbDetails.getPostCode());
        if (isEmpty(suburbDetails.getName()) ||
            isEmpty(suburbDetails.getState())) {
            throw REQUEST_INVALID;
        }
    }
}
