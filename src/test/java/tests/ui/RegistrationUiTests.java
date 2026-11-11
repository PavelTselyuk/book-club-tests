package tests.ui;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBase;

@Feature("Регистрация")
@Tag("ui")
public class RegistrationUiTests extends TestBase {

    String username;
    String password;

    @BeforeEach
    void prepareTestData() {
        username = faker.name().firstName() + "_" + faker.internet().uuid();
        password = "12345";
    }

    @Test
    @Description("UI-регистрация нового пользователя с уникальным username. Ожидается успешная регистрация.")
    @DisplayName("[UI] Успешная регистрация")
    @Severity(SeverityLevel.CRITICAL)
    public void successfulRegistrationTest() {
        signupPage.openPage();

        signupPage.register(username, password);

        signupPage.assertSuccessfulRegistration();
    }
}
