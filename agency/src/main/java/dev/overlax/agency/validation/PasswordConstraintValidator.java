package dev.overlax.agency.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    private static final Pattern HAS_LOWER = Pattern.compile("[a-z]");
    private static final Pattern HAS_UPPER = Pattern.compile("[A-Z]");
    private static final Pattern HAS_DIGIT = Pattern.compile("\\d");
    private static final Pattern HAS_SPECIAL = Pattern.compile("[@$!%*?&_]");
    private static final Pattern VALID_CHARS = Pattern.compile("^[A-Za-z\\d@$!%*?&_]{8,}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return VALID_CHARS.matcher(value).find()
                && HAS_LOWER.matcher(value).find()
                && HAS_UPPER.matcher(value).find()
                && HAS_DIGIT.matcher(value).find()
                && HAS_SPECIAL.matcher(value).find();
    }
}
