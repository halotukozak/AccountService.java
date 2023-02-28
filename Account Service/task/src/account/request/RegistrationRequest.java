package account.request;

import account.validation.NonPwnedPassword;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record RegistrationRequest(@NotBlank(message = "Name cannot be blank") String name,
                                  @NotBlank(message = "Lastname cannot be blank") String lastname,
                                  @NotBlank(message = "Email cannot be blank") @Pattern(regexp = "^\\w+@acme\\.com$", message = "Email should follow the pattern.") String email,
                                  @NotNull @NotBlank(message = "Password cannot be blank") @Size(min = 12, message = "Password must have at least 12 characters!") @NonPwnedPassword String password) {
}
