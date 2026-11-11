package tests.ui;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.login.SuccessfulLoginResponseModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBase;

@Feature("Клубы")
@Tag("ui")
public class CreateClubUiTests extends TestBase {

    private String accessToken;
    private int clubId;

    @AfterEach
    void cleanupClub() {
        if (accessToken != null && clubId > 0) {
            api.clubs.deleteClub(accessToken, clubId);
        }
    }

    @Test
    @Description("Логин через API, создание клуба через UI, фильтр «Мои клубы». " +
            "Ожидается клуб с указанным названием; id для удаления берётся из GET /clubs/?membership=owner.")
    @DisplayName("[UI] Успешное создание клуба")
    @Severity(SeverityLevel.CRITICAL)
    public void successfulCreateClubTest() {
        String bookTitle = "UI Club " + faker.book().title() + " " + faker.internet().uuid();
        String bookAuthors = faker.book().author();
        String publicationYear = "2020";
        String description = faker.lorem().sentence();
        String telegramChatLink = "https://t.me/qa_guru_" + faker.internet().uuid();

        SuccessfulLoginResponseModel loginResponse = createClubPage.openBlankPageWithNewUser();
        accessToken = loginResponse.access();
        createClubPage.openPage();

        createClubPage.fillClubFormAndSubmit(bookTitle, bookAuthors, publicationYear, description, telegramChatLink);
        createClubPage.filterMyClubs();

        createClubPage.assertClubVisibleInList(bookTitle);

        clubId = api.clubs.findOwnedClubIdByTitle(accessToken, bookTitle);
    }
}