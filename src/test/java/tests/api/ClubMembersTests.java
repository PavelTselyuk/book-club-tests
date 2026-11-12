package tests.api;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.login.LoginBodyModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static tests.TestData.OTHER_CLUB_ID;
import static tests.TestData.USER888_OWNED_CLUB_ID;

@Feature("Участники клуба")
public class ClubMembersTests extends TestBase {

    private static String accessToken;

    @BeforeAll
    static void loginAsUser888() {
        accessToken = api.auth.login(new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD)).access();
    }

    @Test
    @Description("Владелец не может покинуть свой клуб: DELETE /clubs/{id}/members/me/ для клуба 8517 возвращает ошибку.")
    @DisplayName("[API] DELETE /clubs/{id}/members/me/ — нельзя покинуть свой клуб")
    @Severity(SeverityLevel.CRITICAL)
    public void cannotLeaveOwnedClub() {
        api.clubs.leaveOwnedClub(accessToken, USER888_OWNED_CLUB_ID);
    }

    @Test
    @Description("Вступление в чужой клуб и выход из него: POST и DELETE /clubs/{id}/members/me/. " +
            "После теста пользователь снова не состоит в клубе.")
    @DisplayName("[API] POST+DELETE /clubs/{id}/members/me/ — вступление и выход из чужого клуба")
    @Severity(SeverityLevel.CRITICAL)
    public void joinAndLeaveOtherClub() {
        // на случай «грязного» состояния после прошлого падения — сначала выходим, если уже участник
        try {
            api.clubs.leaveClub(accessToken, OTHER_CLUB_ID);
        } catch (AssertionError ignored) {
            // не состояли в клубе — ок
        }

        api.clubs.joinClub(accessToken, OTHER_CLUB_ID);
        api.clubs.leaveClub(accessToken, OTHER_CLUB_ID);
    }
}
