package models.users;

public record UserProfileResponseModel(
        Integer id,
        String username,
        String firstName,
        String lastName,
        String email,
        String remoteAddr
) {}
