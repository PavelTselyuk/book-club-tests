package tests;

import models.clubs.ClubModel;
import models.clubs.ClubsListResponseModel;
import models.clubs.CreateClubBodyModel;
import models.login.LoginBodyModel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.LOGIN_PASSWORD;
import static tests.TestData.LOGIN_USERNAME;

public class ClubsTests extends TestBase {

    private static final String TELEGRAM_LINK_REGEXP = "^https://t\\.me/[A-Za-z0-9_]{5,32}$";

    @Test
    public void getClubsReturns200AndValidStructure() {
        ClubsListResponseModel response = api.clubs.getClubs();

        assertThat(response).isNotNull();
        assertThat(response.count()).isGreaterThanOrEqualTo(0);
        assertThat(response.results()).isNotNull();
    }

    @Test
    public void getClubsEachClubHasRequiredFields() {
        ClubsListResponseModel response = api.clubs.getClubs();

        for (ClubModel club : response.results()) {
            assertThat(club.id()).isNotNull().isPositive();
            assertThat(club.bookTitle()).isNotNull();
            assertThat(club.bookAuthors()).isNotNull();
            assertThat(club.publicationYear()).isNotNull();
            assertThat(club.description()).isNotNull();
            assertThat(club.telegramChatLink()).isNotNull();
            assertThat(club.owner()).isNotNull().isPositive();
            assertThat(club.members()).isNotNull();
            assertThat(club.reviews()).isNotNull();
            assertThat(club.created()).isNotNull();
        }
    }

    @Test
    public void getClubsPaginationFieldsPresent() {
        ClubsListResponseModel response = api.clubs.getClubs();

        assertThat(response.count()).isNotNull();
        // next и previous могут быть null при одной странице
        assertThat(response.results()).isNotNull();
    }

    @Test
    public void createClubReturnsSentFields() {
        CreateClubBodyModel body = newClubBody();
        String accessToken = api.auth.login(new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD)).access();

        ClubModel created = api.clubs.createClub(accessToken, body);

        assertThat(created.bookTitle()).isEqualTo(body.bookTitle());
        assertThat(created.bookAuthors()).isEqualTo(body.bookAuthors());
        assertThat(created.publicationYear()).isEqualTo(body.publicationYear());
        assertThat(created.description()).isEqualTo(body.description());
        assertThat(created.telegramChatLink()).isEqualTo(body.telegramChatLink());
        assertThat(created.telegramChatLink()).matches(TELEGRAM_LINK_REGEXP);
    }

    @Test
    public void createClubResponseHasServerFields() {
        CreateClubBodyModel body = newClubBody();
        String accessToken = api.auth.login(new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD)).access();

        ClubModel created = api.clubs.createClub(accessToken, body);

        assertThat(created.id()).isNotNull().isPositive();
        assertThat(created.owner()).isNotNull().isPositive();
        assertThat(created.members()).contains(created.owner());
        assertThat(created.reviews()).isNotNull().isEmpty();
        assertThat(created.created()).isNotNull().isNotBlank();
        assertThat(created.modified()).isNull();
    }

    private CreateClubBodyModel newClubBody() {
        String suffix = String.valueOf(System.nanoTime());
        return new CreateClubBodyModel(
                "Book " + suffix,
                "Author " + suffix,
                2024,
                "Awesome description",
                "https://t.me/bc" + suffix.substring(suffix.length() - 8)
        );
    }
}
