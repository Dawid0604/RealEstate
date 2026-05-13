package pl.dawid0604.realestate.api.auth.request;

public record RegisterRequest(
        String username,
        String password,
        String firstName,
        String lastName,
        String type,
        String notificationEmail,
        String notificationPhoneNumber
) {}
