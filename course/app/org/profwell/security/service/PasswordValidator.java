package org.profwell.security.service;

import org.profwell.conf.di.ServiceHolder;
import org.profwell.generic.validation.ValidationContext;
import org.profwell.generic.validation.utils.ValidationUtility;
import org.profwell.security.domain.PasswordForm;
import org.profwell.security.web.SessionUtility;

public class PasswordValidator {

    private static String passwordMessage =
            "Password should have at least 8 characters in length,"
                    + " and contain at least one lower case letter,"
                    + " at least one number or upper case letter,"
                    + " and at least one special symbol.";

    private static char[] specialSymbols = new char[]{
            '~', '`', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')',
            '_', '-', '=', '+', '{', '}', '[', ']', '\\', '/', '|', ':',
            ';', '\'', '"', ',', '<', '.', '>', '?'};

    private static String specialSymbolsMessage;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(passwordMessage);
        sb.append(" Special symbols: [");
        boolean firstPassed = false;
        for (char c : specialSymbols) {
            if (firstPassed) {
                sb.append(", ");
            } else {
                firstPassed = true;
            }
            sb.append("'");
            sb.append(c);
            sb.append("'");
        }
        sb.append("].");
        specialSymbolsMessage = sb.toString();
    }

    public boolean validatePassword(PasswordForm form,
            ValidationContext context) {

        boolean result = true;

        result &= ValidationUtility.validateRequiredText(
                "password", form.getPassword(), context);

        if (result) {
            result &= form.getPassword().length() >= 8;
            result &= checkContainsLowerLetter(form.getPassword());
            result &= checkContainsUpperLetterOrDigit(form.getPassword());
            if (result) {
                if (!checkContainsSpecialSymbol(form.getPassword())) {
                    context.add("password", specialSymbolsMessage);
                    result = false;
                }
            } else {
                context.add("password", passwordMessage);
            }
        }

        if (result) {
            if (!form.getPassword().equals(form.getConfirmPassword())) {
                context.add("confirmPassword",
                        "Password and Confirm password fields should be equal.");
                result = false;
            }
        }

        return result;
    }

    public boolean validateUserPasswordOnChange(PasswordForm form,
            ValidationContext context) {

        boolean result = true;
        if (form.getId() == SessionUtility.getCurrentUserId()) {
            result &= ValidationUtility.validateRequiredText(
                    "currentPassword", form.getPassword(), context);

            if (ServiceHolder.getService(UserService.class)
                    .authenticate(SessionUtility.getCurrentUsername(),
                            form.getCurrentPassword()) == null) {
                context.add("currentPassword", "Current password is incorrect.");
                result = false;
            }
        }

        if (result) {
            result &= this.validatePassword(form, context);
        }

        return result;
    }

    private boolean checkContainsLowerLetter(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLowerCase(password.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean checkContainsUpperLetterOrDigit(String password) {
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (Character.isUpperCase(ch) || Character.isDigit(ch)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkContainsSpecialSymbol(String password) {
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (isSpecialSymbol(ch)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSpecialSymbol(char ch) {
        for (int i = 0; i < specialSymbols.length; i++) {
            if (ch == specialSymbols[i]) {
                return true;
            }
        }
        return false;
    }
}
