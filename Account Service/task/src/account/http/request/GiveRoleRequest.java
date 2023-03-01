package account.http.request;

import javax.validation.constraints.NotBlank;

public record GiveRoleRequest(@NotBlank(message = "User's email cannot be blank") String user,
                              @NotBlank(message = "User's role cannot be blank") String role,
                              @NotBlank(message = "Operation cannot be blank") String operation) {
}
