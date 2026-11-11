package tests.ui;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.clubs.ClubModel;
import models.login.SuccessfulLoginResponseModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBase;

@Feature("Отзывы")
@Tag("ui")
public class CreateReviewUiTests extends TestBase {

    private String accessToken;
    private int clubId;

    @AfterEach
    void cleanupClub() {
        if (accessToken != null && clubId > 0) {
            api.clubs.deleteClub(accessToken, clubId);
        }
    }

    @Test
    @Description("Логин через API, создание клуба через API, открытие страницы клуба и оставление отзыва через UI. " +
            "TODO: селекторы формы отзыва в ClubPage.")
    @DisplayName("[UI] Успешное создание отзыва")
    @Severity(SeverityLevel.CRITICAL)
    public void successfulCreateReviewTest() {
        String reviewText = "UI review " + faker.internet().uuid();
        String assessment = "5";
        String readPages = "100";

        SuccessfulLoginResponseModel loginResponse = clubPage.openBlankPageWithNewUser();
        accessToken = loginResponse.access();
        ClubModel club = api.clubs.createRandomClub(accessToken);
        clubId = club.id();
        clubPage.openPageById(String.valueOf(clubId));

        clubPage.fillReviewForm(reviewText, assessment, readPages);
        clubPage.submitReview();

        clubPage.assertReviewVisible(reviewText);
    }
}
