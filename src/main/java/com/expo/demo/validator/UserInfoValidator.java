package com.expo.demo.validator;

import com.expo.demo.model.user.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserInfoValidator implements Validator {


    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
         ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userInfo.firstName", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userInfo.lastName", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userInfo.email", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userInfo.phone", "NotEmpty");
    }
}
