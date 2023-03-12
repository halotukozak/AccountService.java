package account.http.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public record ChangeLockRequest(@NotNull @NotBlank(message = "User cannot be blank") String user,
                                @NotNull @NotBlank(message = "Operation cannot be blank") String operation) {
}
