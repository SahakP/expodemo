package com.expo.demo.validator;

import com.expo.demo.model.user.User;
import com.expo.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userInfo.firstName", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userInfo.lastName", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "selectedRoleName", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
        if (user.getNotValidatePassword() == null) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        }


        User userDb = userService.findByUsername(user.getUsername());

        if (user.getId() == null && userDb != null) {
            errors.rejectValue("username", "Duplicate.userForm.username");
        }

        if (user.getNotValidatePassword() == null) {
            if (user.getPassword().length() < 4 || user.getPassword().length() > 32) {
                errors.rejectValue("password", "Size.userForm.password");
            }

            if (!user.getPasswordConfirm().equals(user.getPassword())) {
                errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
            }
        }
    }
}
