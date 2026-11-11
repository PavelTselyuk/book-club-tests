package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage extends BasePage {
    private final SelenideElement loginContainer = $("[data-testid=login-container]");
    private final SelenideElement usernameInput = $("[data-testid=username-input]");
    private final SelenideElement passwordInput = $("[data-testid=password-input]");
    private final SelenideElement signInButton = $("[data-testid=submit-button]");
    private final SelenideElement errorMessage = $("[data-testid=error-message]");

    @Step("[UI] Открытие страницы входа /signin")
    public LoginPage openPage() {
        open("/signin");
        return this;
    }

    @Step("[UI] Ввод логина: {username}")
    public LoginPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Step("[UI] Ввод пароля")
    public LoginPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("[UI] Нажатие кнопки входа")
    public LoginPage pressSignInButton() {
        signInButton.click();
        return this;
    }

    @Step("[UI] Логин пользователем {username}")
    public LoginPage login(String username, String password) {
        setUsername(username);
        setPassword(password);
        pressSignInButton();
        return this;
    }

    @Step("[UI] Проверка, что открыта страница входа")
    public LoginPage loginPageShouldBeOpened() {
        loginContainer.shouldBe(visible);
        return this;
    }

    @Step("[UI] Проверка успешного входа")
    public LoginPage assertSuccessfulLogin() {
        signInButton.should(disappear);
        return this;
    }

    @Step("[UI] Проверка сообщения об ошибке входа")
    public LoginPage assertLoginErrorVisible() {
        errorMessage.shouldBe(visible);
        return this;
    }
}
