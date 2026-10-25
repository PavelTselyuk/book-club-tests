package models.update;

public record SuccessfulUpdateResponseBodyModel(
        Long id,
        String username,
        String firstName,
        String lastName,
        String email,
        String remoteAddr
) {}
