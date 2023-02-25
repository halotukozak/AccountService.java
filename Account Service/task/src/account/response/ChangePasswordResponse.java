package account.response;

public record ChangePasswordResponse(String email) {

    public String getStatus() {
        return "The password has been updated successfully";
    }
}
