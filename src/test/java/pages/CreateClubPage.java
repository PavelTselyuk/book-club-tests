package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class CreateClubPage extends BasePage {
    private final SelenideElement createClubLink = $("[data-testid=create-club-link]");
    private final SelenideElement bookTitleInput = $("#bookTitle");
    private final SelenideElement bookAuthorsInput = $("#bookAuthors");
    private final SelenideElement publicationYearInput = $("#publicationYear");
    private final SelenideElement descriptionInput = $("#description");
    private final SelenideElement telegramChatLinkInput = $("#telegramChatLink");
    private final SelenideElement clubsList = $(".clubs-list");
    private final ElementsCollection filterOptions = $$(".filter-option");

    @Step("[UI] Открытие страницы создания клуба /clubs/create")
    public CreateClubPage openPage() {
        open("/clubs/create");
        return this;
    }

    @Step("[UI] Переход к созданию клуба по ссылке")
    public CreateClubPage openViaCreateClubLink() {
        createClubLink.click();
        return this;
    }

    @Step("[UI] Заполнение формы создания клуба")
    public CreateClubPage fillClubFormAndSubmit(
            String bookTitle,
            String bookAuthors,
            String publicationYear,
            String description,
            String telegramChatLink
    ) {
        bookTitleInput.setValue(bookTitle);
        bookAuthorsInput.setValue(bookAuthors);
        publicationYearInput.setValue(publicationYear);
        descriptionInput.setValue(description);
        telegramChatLinkInput.setValue(telegramChatLink).pressEnter();
        return this;
    }

    @Step("[UI] Фильтр «Мои клубы»")
    public CreateClubPage filterMyClubs() {
        filterOptions.findBy(text("Мои клубы")).click();
        return this;
    }

    @Step("[UI] Открытие клуба «{bookTitle}» из списка")
    public ClubPage openClubFromList(String bookTitle) {
        clubsList.$(byText(bookTitle))
                .parent().parent()
                .$(".open-btn").click();
        return new ClubPage();
    }

    @Step("[UI] Проверка, что клуб «{bookTitle}» есть в списке")
    public CreateClubPage assertClubVisibleInList(String bookTitle) {
        clubsList.$(byText(bookTitle)).shouldBe(visible);
        return this;
    }
}
