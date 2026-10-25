package models.registration;

import java.util.List;

public record InvalidUsernameResponseModel(List<String> username) implements ResponseModel {}
