package account.request;

import javax.validation.constraints.NotBlank;

public record ChangePasswordRequest(@NotBlank String new_password) {
}

