package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class SignupPage extends BasePage {
    private final SelenideElement usernameInput = $("[data-testid=username-input]");
    private final SelenideElement passwordInput = $("[data-testid=password-input]");
    private final SelenideElement confirmPasswordInput = $("[data-testid=confirm-password-input]");
    private final SelenideElement signUpButton = $("[data-testid=signup-button]");

    @Step("[UI] Открытие страницы регистрации /signup")
    public SignupPage openPage() {
        open("/signup");
        return this;
    }

    @Step("[UI] Регистрация пользователя {username}")
    public SignupPage register(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        confirmPasswordInput.setValue(password).pressEnter();
        return this;
    }

    @Step("[UI] Проверка успешной регистрации")
    public SignupPage assertSuccessfulRegistration() {
        signUpButton.should(disappear);
        return this;
    }
}
