package api;

import io.qameta.allure.Step;
import models.clubs.ClubModel;
import models.clubs.ClubsListResponseModel;
import models.clubs.CreateClubBodyModel;
import net.datafaker.Faker;

import static io.restassured.RestAssured.given;
import static specs.clubs.ClubsSpec.clubsRequestSpec;
import static specs.clubs.ClubsSpec.successfulClubsListResponseSpec;
import static specs.clubs.ClubsSpec.successfulCreateClubResponseSpec;
import static specs.clubs.ClubsSpec.successfulDeleteClubResponseSpec;
import static specs.clubs.ClubsSpec.unauthorizedCreateClubResponseSpec;
import static specs.clubs.MembersSpec.cannotLeaveOwnedClubResponseSpec;
import static specs.clubs.MembersSpec.successfulJoinClubResponseSpec;
import static specs.clubs.MembersSpec.successfulLeaveClubResponseSpec;

public class ClubsApiClient {

    @Step("[API] Получение списка клубов GET /clubs/")
    public ClubsListResponseModel getClubs() {
        return given(clubsRequestSpec)
                .when()
                .get("/clubs/")
                .then()
                .spec(successfulClubsListResponseSpec)
                .extract()
                .as(ClubsListResponseModel.class);
    }

    @Step("[API] Получение своих клубов GET /clubs/?membership=owner")
    public ClubsListResponseModel getOwnedClubs(String accessToken, int page, int pageSize) {
        return given(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .queryParam("page", page)
                .queryParam("page_size", pageSize)
                .queryParam("membership", "owner")
                .when()
                .get("/clubs/")
                .then()
                .spec(successfulClubsListResponseSpec)
                .extract()
                .as(ClubsListResponseModel.class);
    }

    @Step("[API] Поиск id своего клуба по названию «{bookTitle}»")
    public int findOwnedClubIdByTitle(String accessToken, String bookTitle) {
        return getOwnedClubs(accessToken, 1, 10).results().stream()
                .filter(club -> bookTitle.equals(club.bookTitle()))
                .map(ClubModel::id)
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "Клуб с названием «" + bookTitle + "» не найден среди клубов владельца"));
    }

    @Step("[API] Создание клуба POST /clubs/")
    public ClubModel createClub(String accessToken, CreateClubBodyModel body) {
        return given(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(body)
                .when()
                .post("/clubs/")
                .then()
                .spec(successfulCreateClubResponseSpec)
                .extract()
                .as(ClubModel.class);
    }

    @Step("[API] Создание рандомного клуба POST /clubs/")
    public ClubModel createRandomClub(String accessToken) {
        Faker faker = new Faker();
        String bookTitle = faker.book().title() + " " + faker.internet().uuid();
        String bookAuthors = faker.book().author();
        Integer publicationYear = 2009;
        String description = faker.lorem().sentence();
        String telegramChatLink = "https://t.me/qa_guru";

        CreateClubBodyModel createClubBody = new CreateClubBodyModel(
                bookTitle,
                bookAuthors,
                publicationYear,
                description,
                telegramChatLink
        );

        return createClub(accessToken, createClubBody);
    }

    @Step("[API] Удаление клуба DELETE /clubs/{clubId}/")
    public void deleteClub(String accessToken, int clubId) {
        given(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .delete("/clubs/{clubId}/", clubId)
                .then()
                .spec(successfulDeleteClubResponseSpec);
    }

    @Step("[API] Вступление в клуб POST /clubs/{clubId}/members/me/")
    public void joinClub(String accessToken, int clubId) {
        given(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .post("/clubs/{clubId}/members/me/", clubId)
                .then()
                .spec(successfulJoinClubResponseSpec);
    }

    @Step("[API] Выход из клуба DELETE /clubs/{clubId}/members/me/")
    public void leaveClub(String accessToken, int clubId) {
        given(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .delete("/clubs/{clubId}/members/me/", clubId)
                .then()
                .spec(successfulLeaveClubResponseSpec);
    }

    @Step("[API] Попытка покинуть свой клуб DELETE /clubs/{clubId}/members/me/")
    public void leaveOwnedClub(String accessToken, int clubId) {
        given(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .delete("/clubs/{clubId}/members/me/", clubId)
                .then()
                .spec(cannotLeaveOwnedClubResponseSpec);
    }

    @Step("[API] Попытка создать клуб без авторизации POST /clubs/")
    public void createClubWithoutAuth(CreateClubBodyModel body) {
        given(clubsRequestSpec)
                .body(body)
                .when()
                .post("/clubs/")
                .then()
                .spec(unauthorizedCreateClubResponseSpec);
    }
}
