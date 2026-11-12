package tests.ui;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static tests.TestData.LOGIN_WRONG_PASSWORD;

@Feature("Авторизация")
@Tag("ui")
public class LoginUiTests extends TestBase {

    @Test
    @Description("UI-логин пользователем user888 из констант TestData. Ожидается успешный вход.")
    @DisplayName("[UI] Успешный логин user888")
    @Severity(SeverityLevel.CRITICAL)
    public void successfulLoginAsUser888Test() {
        loginPage.openPage();

        loginPage.login(LOGIN_USERNAME, LOGIN_PASSWORD);

        loginPage.assertSuccessfulLogin();
    }

    @Test
    @Description("UI-логин с неверным паролем: после нажатия кнопки входа появляется сообщение об ошибке.")
    @DisplayName("[UI] Ошибка при логине с неверным паролем")
    @Severity(SeverityLevel.NORMAL)
    public void loginWithWrongPasswordShowsErrorTest() {
        loginPage.openPage();

        loginPage.login(LOGIN_USERNAME, LOGIN_WRONG_PASSWORD);

        loginPage.assertLoginErrorVisible();
    }
}
