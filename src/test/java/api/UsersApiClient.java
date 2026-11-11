package api;

import io.qameta.allure.Step;
import models.registration.ExistingUserResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import models.users.UpdateUserBodyModel;
import models.users.UserProfileResponseModel;

import static io.restassured.RestAssured.given;
import static specs.registration.RegistrationSpec.existingUserRegistrationResponseSpec;
import static specs.registration.RegistrationSpec.registrationRequestSpec;
import static specs.registration.RegistrationSpec.successfulRegistrationResponseSpec;
import static specs.users.UsersSpec.successfulDeleteUserResponseSpec;
import static specs.users.UsersSpec.successfulUpdateUserResponseSpec;
import static specs.users.UsersSpec.usersRequestSpec;

public class UsersApiClient {

    @Step("[API] Регистрация пользователя POST /users/register/")
    public SuccessfulRegistrationResponseModel register(RegistrationBodyModel body) {
        return given(registrationRequestSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec)
                .extract()
                .as(SuccessfulRegistrationResponseModel.class);
    }

    @Step("[API] Попытка повторной регистрации существующего пользователя POST /users/register/")
    public ExistingUserResponseModel registerExistingUser(RegistrationBodyModel body) {
        return given(registrationRequestSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .spec(existingUserRegistrationResponseSpec)
                .extract()
                .as(ExistingUserResponseModel.class);
    }

    @Step("Полное обновление текущего пользователя PUT /users/me/")
    public UserProfileResponseModel updateUser(String accessToken, UpdateUserBodyModel body) {
        return given(usersRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(body)
                .when()
                .put("/users/me/")
                .then()
                .spec(successfulUpdateUserResponseSpec)
                .extract()
                .as(UserProfileResponseModel.class);
    }

    @Step("Частичное обновление текущего пользователя PATCH /users/me/")
    public UserProfileResponseModel patchUser(String accessToken, UpdateUserBodyModel body) {
        return given(usersRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(body)
                .when()
                .patch("/users/me/")
                .then()
                .spec(successfulUpdateUserResponseSpec)
                .extract()
                .as(UserProfileResponseModel.class);
    }

    @Step("Удаление текущего пользователя DELETE /users/me/")
    public void deleteCurrentUser(String accessToken) {
        given(usersRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .delete("/users/me/")
                .then()
                .spec(successfulDeleteUserResponseSpec);
    }
}
