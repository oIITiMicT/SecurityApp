package com.example.InPostPW.validation.impl;

import java.util.regex.Pattern;
import com.example.InPostPW.exception.IncorrectPasswordException;
import com.example.InPostPW.validation.PasswordValidation;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidationImpl implements PasswordValidation {
    private static final String ALLOWED_LENGTH_PATTERN = ".{8,20}";
    private static final String CAPITAL_LETTERS_PATTERN = ".*[A-Z].*";
    private static final String LETTERS_PATTERN = ".*[a-z].*";
    private static final String NUMBERS_PATTERN = ".*[\\d].*";
    private static final String SPECIAL_CHARACTERS_PATTERN = ".*[\\x21-\\x2F\\x3A-\\x40\\x5B-\\x60\\x7B-\\x7E].*";
    private static final String FORBIDDEN_CHARACTERS_PATTERN = ".*[^\\x21-\\x2F\\x3A-\\x40\\x5B-\\x60\\x7B-\\x7E\\dA-Za-z].*";
    private static final String LENGTH_ERROR = "the password must contain from 8 to 20 characters";
    private static final String NO_LOWERCASE = "the password must contain at least one lowercase letter";
    private static final String NO_UPPERCASE = "the password must contain at least one uppercase letter";
    private static final String NO_NUMBER = "the password must contain at least one number";
    private static final String NO_SPECIAL_CHARACTER = "the password must contain at least one special character";
    private static final String CHARACTER_ERROR = "the password contains an invalid character (must be ASCII 33-126)";
    private static final String NULL_ERROR = "the password cannot be NULL";

    @Override
    public void checkPasswordValidity(String password) {
        if (password == null) {
            throw new IncorrectPasswordException(NULL_ERROR);
        }
        if (Pattern.matches(FORBIDDEN_CHARACTERS_PATTERN, password)) {
            throw new IncorrectPasswordException(CHARACTER_ERROR);
        }
        if (!Pattern.matches(ALLOWED_LENGTH_PATTERN, password)) {
            throw new IncorrectPasswordException(LENGTH_ERROR);
        }
        if (!Pattern.matches(CAPITAL_LETTERS_PATTERN, password)) {
            throw new IncorrectPasswordException(NO_UPPERCASE);
        }
        if (!Pattern.matches(LETTERS_PATTERN, password)) {
            throw new IncorrectPasswordException(NO_LOWERCASE);
        }
        if (!Pattern.matches(NUMBERS_PATTERN, password)) {
            throw new IncorrectPasswordException(NO_NUMBER);
        }
        if (!Pattern.matches(SPECIAL_CHARACTERS_PATTERN, password)) {
            throw new IncorrectPasswordException(NO_SPECIAL_CHARACTER);
        }
    }
}
