package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.confirm;
import static com.codeborne.selenide.Selenide.open;

public class ClubPage extends BasePage {

    private final SelenideElement leaveButton = $(".leave-btn");
    private final SelenideElement error = $(".error");

    private final SelenideElement addReviewButton = $(".add-review-btn");
    private final SelenideElement reviewInput = $("#review");
    private final SelenideElement assessmentInput = $("#assessment");
    private final SelenideElement readPagesInput = $("#readPages");
    private final SelenideElement submitReviewButton = $(".save-btn");
    private final ElementsCollection reviewContents = $$(".review-content");

    @Step("[UI] Открытие страницы клуба по id: {clubId}")
    public ClubPage openPageById(String clubId) {
        open("/clubs/" + clubId);
        return this;
    }

    @Step("[UI] Нажать «Покинуть клуб» и подтвердить диалог")
    public ClubPage pressLeaveClubButton() {
        leaveButton.click();
        confirm();
        return this;
    }

    @Step("[UI] Владелец не может покинуть клуб: подтверждение и сообщение об ошибке")
    public ClubPage assertOwnerCannotLeaveClub() {
        error.shouldHave(text("Не удалось покинуть клуб"));
        return this;
    }

    @Step("[UI] Нажать «Добавить отзыв»")
    public ClubPage pressAddReviewButton() {
        addReviewButton.click();
        return this;
    }

    @Step("[UI] Заполнение формы отзыва")
    public ClubPage fillReviewForm(String review, String assessment, String readPages) {
        pressAddReviewButton();
        reviewInput.setValue(review);
        assessmentInput.setValue(assessment);
        readPagesInput.setValue(readPages);
        return this;
    }

    @Step("[UI] Отправка отзыва")
    public ClubPage submitReview() {
        submitReviewButton.click();
        return this;
    }

    @Step("[UI] Проверка, что отзыв «{review}» отображается")
    public ClubPage assertReviewVisible(String review) {
        reviewContents.findBy(text(review)).shouldBe(visible);
        return this;
    }
}
