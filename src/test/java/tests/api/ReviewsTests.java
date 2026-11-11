package tests.api;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.clubs.ClubReviewModel;
import models.login.LoginBodyModel;
import models.reviews.ReviewBodyModel;
import models.reviews.ReviewsListResponseModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.LOGIN_PASSWORD;
import static tests.TestData.LOGIN_USERNAME;

@Feature("Отзывы")
public class ReviewsTests extends TestBase {

    private static String accessToken;
    private int clubId;

    @BeforeAll
    static void loginAsUser888() {
        accessToken = api.auth.login(new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD)).access();
    }

    @BeforeEach
    void createClub() {
        clubId = api.clubs.createRandomClub(accessToken).id();
    }

    @AfterEach
    void deleteClub() {
        api.clubs.deleteClub(accessToken, clubId);
    }

    @Test
    @Description("Публичный GET /clubs/reviews/: объект ответа не null, count ≥ 0, results не null.")
    @DisplayName("[API] GET /clubs/reviews/ — список отзывов")
    @Severity(SeverityLevel.CRITICAL)
    public void getReviewsReturnsValidStructure() {
        ReviewsListResponseModel response = api.reviews.getReviews();

        assertThat(response).isNotNull();
        assertThat(response.count()).isGreaterThanOrEqualTo(0);
        assertThat(response.results()).isNotNull();
    }

    @Test
    @Description("POST /clubs/reviews/: ответ 201, поля совпадают с телом запроса.")
    @DisplayName("[API] POST /clubs/reviews/ — создание отзыва")
    @Severity(SeverityLevel.CRITICAL)
    public void createReviewReturnsSentFields() {
        ReviewBodyModel body = api.reviews.randomReviewBody(clubId);

        ClubReviewModel created = api.reviews.createReview(accessToken, body);

        assertThat(created.id()).isNotNull().isPositive();
        assertThat(created.club()).isEqualTo(clubId);
        assertThat(created.review()).isEqualTo(body.review());
        assertThat(created.assessment()).isEqualTo(body.assessment());
        assertThat(created.readPages()).isEqualTo(body.readPages());
        assertThat(created.user()).isNotNull();
        assertThat(created.user().username()).isEqualTo(LOGIN_USERNAME);
        assertThat(created.created()).isNotBlank();
    }

    @Test
    @Description("Повторный POST отзыва на тот же клуб возвращает 500.")
    @DisplayName("[API] POST /clubs/reviews/ — нельзя создать второй отзыв на клуб")
    @Severity(SeverityLevel.NORMAL)
    public void createDuplicateReviewReturns500() {
        ReviewBodyModel body = api.reviews.randomReviewBody(clubId);

        api.reviews.createReview(accessToken, body);
        api.reviews.createDuplicateReview(accessToken, body);
    }

    @Test
    @Description("GET /clubs/reviews/{id}/ возвращает ранее созданный отзыв.")
    @DisplayName("[API] GET /clubs/reviews/{id}/ — получение отзыва по id")
    @Severity(SeverityLevel.CRITICAL)
    public void getReviewByIdReturnsCreatedReview() {
        ClubReviewModel created = api.reviews.createRandomReview(accessToken, clubId);

        ClubReviewModel fetched = api.reviews.getReview(created.id());

        assertThat(fetched.id()).isEqualTo(created.id());
        assertThat(fetched.club()).isEqualTo(clubId);
        assertThat(fetched.review()).isEqualTo(created.review());
        assertThat(fetched.assessment()).isEqualTo(created.assessment());
        assertThat(fetched.readPages()).isEqualTo(created.readPages());
        assertThat(fetched.user().username()).isEqualTo(LOGIN_USERNAME);
    }

    @Test
    @Description("PUT /clubs/reviews/{id}/ полностью обновляет отзыв.")
    @DisplayName("[API] PUT /clubs/reviews/{id}/ — полное обновление")
    @Severity(SeverityLevel.CRITICAL)
    public void putReviewUpdatesAllFields() {
        ClubReviewModel created = api.reviews.createRandomReview(accessToken, clubId);
        ReviewBodyModel updateBody = api.reviews.randomReviewBody(clubId);

        ClubReviewModel updated = api.reviews.updateReview(accessToken, created.id(), updateBody);

        assertThat(updated.id()).isEqualTo(created.id());
        assertThat(updated.club()).isEqualTo(clubId);
        assertThat(updated.review()).isEqualTo(updateBody.review());
        assertThat(updated.assessment()).isEqualTo(updateBody.assessment());
        assertThat(updated.readPages()).isEqualTo(updateBody.readPages());
        assertThat(updated.modified()).isNotBlank();
    }

    @Test
    @Description("PATCH /clubs/reviews/{id}/ обновляет только переданные поля.")
    @DisplayName("[API] PATCH /clubs/reviews/{id}/ — частичное обновление")
    @Severity(SeverityLevel.CRITICAL)
    public void patchReviewUpdatesOnlySentFields() {
        ClubReviewModel created = api.reviews.createRandomReview(accessToken, clubId);
        String newReviewText = "Patched " + System.nanoTime();
        Integer newAssessment = 3;
        ReviewBodyModel patchBody = new ReviewBodyModel(clubId, newReviewText, newAssessment, null);

        ClubReviewModel updated = api.reviews.patchReview(accessToken, created.id(), patchBody);

        assertThat(updated.id()).isEqualTo(created.id());
        assertThat(updated.club()).isEqualTo(clubId);
        assertThat(updated.review()).isEqualTo(newReviewText);
        assertThat(updated.assessment()).isEqualTo(newAssessment);
        assertThat(updated.readPages()).isEqualTo(created.readPages());
        assertThat(updated.modified()).isNotBlank();
    }

    @Test
    @Description("DELETE /clubs/reviews/{id}/ удаляет отзыв, ответ 204.")
    @DisplayName("[API] DELETE /clubs/reviews/{id}/ — удаление отзыва")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteReviewReturns204() {
        ClubReviewModel created = api.reviews.createRandomReview(accessToken, clubId);

        api.reviews.deleteReview(accessToken, created.id());
    }
}
