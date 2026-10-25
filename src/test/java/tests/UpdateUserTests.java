package tests;

import models.login.LoginBodyModel;
import models.logout.FailureLogoutResponseBodyModel;
import models.logout.LogoutBodyModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.logout.LogoutSpec.failureLogoutResponseSpec;
import static specs.logout.LogoutSpec.logoutRequestSpec;
import static tests.TestData.*;

public class UpdateUserTests extends TestBase{

    @Test
    public void successfulUpdateTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);
        api.auth.login(loginData);

       // given(logoutRequestSpec)
       //         .body(logoutBody)
       //         .when()
       //         .post("/auth/logout/")
       //         .then()
       //         .spec(failureLogoutResponseSpec)
       //         .extract()
       //         .as(FailureLogoutResponseBodyModel.class);
//
       // LogoutBodyModel logoutData = new LogoutBodyModel(refreshToken);
       // api.auth.logout(logoutData);
    }

    @Test
    public void wrongTokenLogoutTest() {
        LogoutBodyModel logoutData = new LogoutBodyModel(WRONG_TOKEN);

        FailureLogoutResponseBodyModel logoutResponse = api.auth.failedLogout(logoutData);

        String actualDetailError = logoutResponse.detail();
        String actualCodeError = logoutResponse.code();
        assertThat(actualDetailError).isEqualTo(FAILED_LOGOUT_DETAIL_ERROR);
        assertThat(actualCodeError).isEqualTo(FAILED_LOGOUT_CODE_ERROR);

    }
}
