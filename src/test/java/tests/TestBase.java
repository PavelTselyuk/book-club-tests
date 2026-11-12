package tests;

import allure.Attach;
import api.ApiClient;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import pages.ClubPage;
import pages.CreateClubPage;
import pages.LoginPage;
import pages.ProfilePage;
import pages.SignupPage;

import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.webdriver;

public class TestBase {
    protected Faker faker = new Faker();
    protected static final ApiClient api = new ApiClient();

    protected static final String LOGIN_USERNAME = System.getProperty("username");
    protected static final String LOGIN_PASSWORD = System.getProperty("password");

    protected ClubPage clubPage = new ClubPage();
    protected CreateClubPage createClubPage = new CreateClubPage();
    protected LoginPage loginPage = new LoginPage();
    protected ProfilePage profilePage = new ProfilePage();
    protected SignupPage signupPage = new SignupPage();

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://book-club.qa.guru";
        RestAssured.basePath = "/api/v1";

        Configuration.baseUrl = "https://book-club.qa.guru";
        Configuration.browserSize = "1920x1080";

        String loginSelenoid = System.getProperty("loginSelenoid");
        String passwordSelenoid = System.getProperty("passwordSelenoid");
        String selenoidAddress = System.getProperty("selenoidAddress");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(List.of("--disable-dev-shm-usage", "--no-sandbox"));
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", true
        ));
        Configuration.browserCapabilities = capabilities;
        Configuration.remote = String.format("https://%s:%s@%s",loginSelenoid, passwordSelenoid, selenoidAddress);
    }

    @BeforeEach
    void addListener() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @AfterEach
    void addAttachments() {
        if(webdriver().driver().hasWebDriverStarted()) {
            Attach.screenshotAs("Last screenshot");
            Attach.pageSource();
            Attach.browserConsoleLogs();
//          Attach.addVideo();
            closeWebDriver();
        }
    }
}
