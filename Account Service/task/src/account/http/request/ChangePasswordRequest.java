package account.http.request;

import account.security.validation.NonPwnedPassword;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotNull @NotBlank(message = "Password cannot be blank") @Size(min = 12, message = "Password must have at least 12 characters!") @NonPwnedPassword() String new_password) {
}

