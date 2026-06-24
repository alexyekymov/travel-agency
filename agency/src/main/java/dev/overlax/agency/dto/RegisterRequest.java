package dev.overlax.agency.dto;

import dev.overlax.agency.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(

        @NotBlank(message = "{register.firstName.notBlank}")
        String firstName,

        @NotBlank(message = "{register.lastName.notBlank}")
        String lastName,

        @NotBlank(message = "{register.email.notBlank}")
        @Email(message = "{register.email.invalid}")
        String email,

        @NotBlank(message = "{register.password.notBlank}")
        @ValidPassword(message = "{register.password.invalid}")
        String password,

        @Pattern(regexp = "^$|^\\+?[0-9 ()\\-]{7,20}$", message = "{register.phoneNumber.invalid}")
        String phoneNumber
) {
}
