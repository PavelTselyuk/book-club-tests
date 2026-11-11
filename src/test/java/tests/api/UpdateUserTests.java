package tests.api;

import models.login.LoginBodyModel;
import models.registration.RegistrationBodyModel;
import models.users.UpdateUserBodyModel;
import models.users.UserProfileResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.REGISTRATION_IP_REGEXP;

public class UpdateUserTests extends TestBase {

    private String username;
    private String password;

    @BeforeEach
    public void prepareTestData() {
        username = "user_" + System.currentTimeMillis();
        password = "pass_" + System.currentTimeMillis();
    }

    @Test
    public void putUserUpdatesAllFields() {
        String accessToken = registerAndGetAccessToken();
        UpdateUserBodyModel body = fullProfile("put");

        UserProfileResponseModel response = api.users.updateUser(accessToken, body);

        assertProfileMatches(response, body);
    }

    @Test
    public void patchUserUpdatesAllFields() {
        String accessToken = registerAndGetAccessToken();
        UpdateUserBodyModel body = fullProfile("patch");

        UserProfileResponseModel response = api.users.patchUser(accessToken, body);

        assertProfileMatches(response, body);
    }

    @Test
    public void patchUserUpdatesOnlyFirstName() {
        String accessToken = registerAndGetAccessToken();
        String firstName = "First" + System.nanoTime();
        UpdateUserBodyModel body = new UpdateUserBodyModel(null, firstName, null, null);

        UserProfileResponseModel response = api.users.patchUser(accessToken, body);

        assertThat(response.firstName()).isEqualTo(firstName);
        assertThat(response.username()).isEqualTo(username);
        assertThat(response.lastName()).isEqualTo("");
        assertThat(response.email()).isEqualTo("");
        assertThat(response.id()).isGreaterThan(0);
        assertThat(response.remoteAddr()).matches(REGISTRATION_IP_REGEXP);
    }

    @Test
    public void patchUserUpdatesLastNameAndEmail() {
        String accessToken = registerAndGetAccessToken();
        String suffix = String.valueOf(System.nanoTime());
        String lastName = "Last" + suffix;
        String email = "mail" + suffix + "@example.com";
        UpdateUserBodyModel body = new UpdateUserBodyModel(null, null, lastName, email);

        UserProfileResponseModel response = api.users.patchUser(accessToken, body);

        assertThat(response.lastName()).isEqualTo(lastName);
        assertThat(response.email()).isEqualTo(email);
        assertThat(response.username()).isEqualTo(username);
        assertThat(response.firstName()).isEqualTo("");
        assertThat(response.id()).isGreaterThan(0);
        assertThat(response.remoteAddr()).matches(REGISTRATION_IP_REGEXP);
    }

    @Test
    public void deleteCurrentUserReturns204() {
        String accessToken = registerAndGetAccessToken();

        api.users.deleteCurrentUser(accessToken);
    }

    private String registerAndGetAccessToken() {
        api.users.register(new RegistrationBodyModel(username, password));
        return api.auth.login(new LoginBodyModel(username, password)).access();
    }

    private UpdateUserBodyModel fullProfile(String prefix) {
        String suffix = String.valueOf(System.nanoTime());
        return new UpdateUserBodyModel(
                prefix + "_" + suffix,
                "First" + suffix,
                "Last" + suffix,
                prefix + suffix + "@example.com"
        );
    }

    private void assertProfileMatches(UserProfileResponseModel response, UpdateUserBodyModel body) {
        assertThat(response.id()).isGreaterThan(0);
        assertThat(response.username()).isEqualTo(body.username());
        assertThat(response.firstName()).isEqualTo(body.firstName());
        assertThat(response.lastName()).isEqualTo(body.lastName());
        assertThat(response.email()).isEqualTo(body.email());
        assertThat(response.remoteAddr()).matches(REGISTRATION_IP_REGEXP);
    }
}
