package tests;

import models.login.LoginBodyModel;
import models.update.FailureUpdateResponseBodyModel;
import models.update.SuccessfulUpdateResponseBodyModel;
import models.update.UpdateBodyModel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

public class UpdateUserTests extends TestBase {

    @Test
    public void successfulUpdateTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);
        String accessToken = api.auth.loginAndGetAccessToken(loginData);

        UpdateBodyModel updateBody = new UpdateBodyModel(
                accessToken,
                LOGIN_USERNAME,
                UPDATED_FIRSTNAME,
                UPDATED_LASTNAME,
                UPDATED_EMAIL
        );

        SuccessfulUpdateResponseBodyModel updateResponse  = api.update.successfullUpdate(updateBody);
//
        String actualFirstName = updateResponse.firstName();
        String actualUsername = updateResponse.username();
        assertThat(actualFirstName).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(actualUsername).isEqualTo(LOGIN_USERNAME);
    }

    @Test
    public void wrongTokenUpdateTest() {
        UpdateBodyModel updateBody = new UpdateBodyModel(
                WRONG_TOKEN,
                LOGIN_USERNAME,
                UPDATED_FIRSTNAME,
                UPDATED_LASTNAME,
                UPDATED_EMAIL
        );

        FailureUpdateResponseBodyModel updateResponse  = api.update.failureUpdate(updateBody);
//
        String actualDetail = updateResponse.detail();
        String actualCode = updateResponse.code();
        assertThat(actualDetail).isEqualTo(FAILED_UPDATE_DETAIL);
        assertThat(actualCode).isEqualTo(CODE_ERROR);
    }
}
