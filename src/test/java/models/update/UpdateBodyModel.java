package models.update;

public record UpdateBodyModel(
        String access,
        String username,
        String firstName,
        String lastName,
        String email
) {
}
