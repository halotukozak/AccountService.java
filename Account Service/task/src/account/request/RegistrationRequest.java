package account.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record RegistrationRequest(@NotBlank(message = "Name cannot be blank") String name,
                                  @NotBlank(message = "Lastname cannot be blank") String lastname,
                                  @NotBlank(message = "Email cannot be blank") @Pattern(regexp = "^\\w+@acme\\.com$", message = "Email should follow the pattern.") String email,
                                  @Size(min = 12, message = "The password length must be at least 12 chars!") @NotBlank(message = "Password cannot be blank") String password) {
}
