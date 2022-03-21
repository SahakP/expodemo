package com.expo.demo.validator;

import com.expo.demo.model.user.User;
import com.expo.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserPasswordValidator implements Validator {
    @Autowired
    private UserService userService;


    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        User user = (User) o;


        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "oldPassword", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "NotEmpty");

        User userDb = userService.findByUsername(user.getUsername());



        if (user.getId() == null && userDb != null) {
            errors.rejectValue("username", "Duplicate.userForm.username");
        }



        if (user.getNotValidatePassword() == null) {
            if (user.getNewPassword().length() < 4 || user.getNewPassword().length() > 32) {
                errors.rejectValue("newPassword", "Size.userForm.password");
            }

            if (!user.getNewPassword().equals(user.getPasswordConfirm())) {
                errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
            }
            if (user.getNewPassword().equals(user.getOldPassword())) {
                errors.rejectValue("newPassword", "Diff.userForm.passwordSame");
            }

        }
    }
}
