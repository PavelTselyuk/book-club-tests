package tests;

import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import models.registration.EmptyPasswordResponseModel;
import models.registration.EmptyUsernameAndPasswordResponseModel;
import models.registration.EmptyUsernameResponseModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static specs.login.LoginSpec.loginRequestSpec;
import static tests.TestData.*;

public class LoginTests extends TestBase {

    @Test
    public void successfulLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        String actualAccess = loginResponse.access();
        String actualRefresh = loginResponse.refresh();
        assertThat(actualAccess).startsWith(LOGIN_TOKEN_PREFIX);
        assertThat(actualRefresh).startsWith(LOGIN_TOKEN_PREFIX);
        assertThat(actualAccess).isNotEqualTo(actualRefresh);
    }

    @Test
    public void wrongCredentialsLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_WRONG_PASSWORD);

        WrongCredentialsLoginResponseModel loginResponse = api.auth.loginWrongCredentials(loginData);

        String expectedDetailError = LOGIN_WRONG_CREDENTIALS_ERROR;
        String actualDetailError = loginResponse.detail();
        assertThat(actualDetailError).isEqualTo(expectedDetailError);
    }

    @Test
    public void emptyUsernameLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel("", LOGIN_PASSWORD);

        EmptyUsernameResponseModel loginResponse = api.auth.loginEmptyUsername(loginData);

        String actualError = loginResponse.username().get(0);
        assertThat(actualError).isEqualTo(AUTH_WITH_EMPTY_USERNAME_ERROR);
    }

    @Test
    public void emptyPasswordLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, "");

        EmptyPasswordResponseModel loginResponse = api.auth.loginEmptyPassword(loginData);

        String actualError = loginResponse.password().get(0);
        assertThat(actualError).isEqualTo(AUTH_WITH_EMPTY_PASSWORD_ERROR);
    }

    @Test
    public void emptyUsernameAndPasswordLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel("", "");

        EmptyUsernameAndPasswordResponseModel loginResponse = api.auth.loginEmptyUsernameAndPassword(loginData);

        String actualUsernameError = loginResponse.username().get(0);
        String actualPasswordError = loginResponse.password().get(0);
        assertThat(actualUsernameError).isEqualTo(AUTH_WITH_EMPTY_USERNAME_ERROR);
        assertThat(actualPasswordError).isEqualTo(AUTH_WITH_EMPTY_PASSWORD_ERROR);
    }

}
