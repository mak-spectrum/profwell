package org.profwell.security.service;

import org.profwell.conf.di.ServiceHolder;
import org.profwell.generic.model.ModelConstants;
import org.profwell.generic.validation.ValidationContext;
import org.profwell.generic.validation.utils.ValidationUtility;
import org.profwell.security.domain.UserForm;
import org.profwell.security.model.User;

import play.data.validation.Constraints.EmailValidator;

public class UserValidator {

    private EmailValidator emailValidator = new EmailValidator();

    public boolean validateCurrentUserDeletion(String currentUsername,
            UserForm userForm) {

        boolean result = true;
        UserService userService = ServiceHolder.getService(UserService.class);

        User user = userService.get(userForm.getId());
        if (user != null && currentUsername.equals(user.getUsername())) {
            userForm.getVc().add("message", "Cannot delete myself.");
            result = false;
        }

        return result;
    }

    public boolean validate(UserForm userForm) {
        boolean result = true;

        ValidationContext context = userForm.getVc();

        result &= validateUserInfo(userForm, context);

        result &= validatePersonInfo(userForm, context);

        return result;
    }

    private boolean validateUserInfo(UserForm userForm,
            ValidationContext context) {

        boolean result = true;

        if (userForm.isNew()) {
            result &= ValidationUtility.validateRequiredText(
                    "username", userForm.getUsername(), context);

            if (result && !validateCorrectUsername(userForm)) {
                context.add("username", "Username can contain alphabetical characters, numbers"
                        + " and symbols: '.', '_', '-', '@', '#', '*' only.");
                result = false;
            } else if (ServiceHolder.getService(UserService.class)
                    .chechUserExists(userForm.getUsername())) {
                context.add("username",
                        "User with the specified username already exists, input another value, please.");
                result = false;
            }
        }

        result &= ValidationUtility.validateRequiredText(
                "email", userForm.getEmail(), context);
        if (result && !emailValidator.isValid(userForm.getEmail())) {
            context.add("email", "Input correct e-mail, please.");
            result = false;
        }

        if (!userForm.isNoRole()) {
            result &= ValidationUtility.validateDropDown(
                    "roleValue", userForm.getRoleValue(), context);
        }

        if (userForm.isNew()) {
            PasswordValidator passwordValidator = ServiceHolder
                    .getService(PasswordValidator.class);

            result &= passwordValidator.validatePassword(
                    userForm.getPasswordForm(), userForm.getVc());
        }

        return result;
    }

    private boolean validateCorrectUsername(UserForm userForm) {
        for (int i = 0; i < userForm.getUsername().length(); i++) {
            char c = userForm.getUsername().charAt(i);
            if (!validateCorrectUsernameChar(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean validateCorrectUsernameChar(char c) {
        return c >= 'A' && c <= 'Z'
                || c >= 'a' && c <= 'z'
                || c >= '0' && c <= '9'
                || c == '.'
                || c == '_'
                || c == '-'
                || c == '@'
                || c == '#'
                || c == '*';
    }

    private boolean validatePersonInfo(UserForm userForm,
            ValidationContext context) {

        boolean result = true;

        result &= ValidationUtility
                .validateMaxLength("firstName", userForm.getUsername(),
                        ModelConstants.STANDARD_TEXT_LIMIT, context);

        result &= ValidationUtility
                .validateMaxLength("lastName", userForm.getUsername(),
                        ModelConstants.STANDARD_TEXT_LIMIT, context);

        return result;
    }

}
