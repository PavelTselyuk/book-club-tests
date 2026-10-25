package tests;

import models.login.LoginBodyModel;
import models.logout.FailureLogoutResponseBodyModel;
import models.logout.LogoutBodyModel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

public class LogoutTests extends TestBase {

    @Test
    public void successfulLogoutTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);
        String refreshToken = api.auth.loginAndGetRefreshToken(loginData);

        LogoutBodyModel logoutData = new LogoutBodyModel(refreshToken);
        api.auth.logout(logoutData);
    }

    @Test
    public void wrongTokenLogoutTest() {
        LogoutBodyModel logoutData = new LogoutBodyModel(WRONG_TOKEN);

        FailureLogoutResponseBodyModel logoutResponse = api.auth.failedLogout(logoutData);

        String actualDetailError = logoutResponse.detail();
        String actualCodeError = logoutResponse.code();
        assertThat(actualDetailError).isEqualTo(FAILED_LOGOUT_DETAIL_ERROR);
        assertThat(actualCodeError).isEqualTo(CODE_ERROR);

    }

    // todo add more negative tests
}

