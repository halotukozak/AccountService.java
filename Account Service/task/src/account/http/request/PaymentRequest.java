package account.http.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

public record PaymentRequest(
        @NotBlank(message = "Email cannot be blank") @Pattern(regexp = "^\\w+@acme\\.com$", message = "Email should follow the pattern.") String employee,
        @NotBlank(message = "Period cannot be blank") @Pattern(regexp = "^(0[1-9]|1[012])-[0-9]{4}$", message = "Period should follow the pattern.") String period,
        @NotNull(message = "Salary cannot be null") @Positive(message = "Salary cannot be negative") Long salary) {

}
