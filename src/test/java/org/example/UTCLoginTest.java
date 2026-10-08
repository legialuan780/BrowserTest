
package org.example;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class UTCLoginTest {

    private WebDriver driver;
    private LoginPage page;

    private final String USER =
            System.getenv("UTC_TEST_USERNAME");
    private final String PASS =
            System.getenv("UTC_TEST_PASSWORD");

    // Chỉ dùng trên môi trường staging được cấp phép
    private final boolean SECURITY_TEST_ENABLED =
            Boolean.parseBoolean(
                    System.getenv("UTC_SECURITY_TEST_ENABLED"));

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        page = new LoginPage(driver);
        page.open();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void requireCredentials() {
        assumeTrue(USER != null && !USER.isBlank()
                        && PASS != null && !PASS.isBlank(),
                "Chua cau hinh tai khoan test");
    }

    private void requireSecurityEnvironment() {
        assumeTrue(SECURITY_TEST_ENABLED,
                "Chi chay security test tren staging duoc phep");
    }

    private void requireAuthenticatedIndicator() {
        assumeTrue(
                System.getenv("UTC_AUTHENTICATED_SELECTOR") != null,
                "Can CSS selector xac nhan dang nhap thanh cong");
    }

    private void assertAuthenticated() {
        String selector =
                System.getenv("UTC_AUTHENTICATED_SELECTOR");

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions
                        .visibilityOfElementLocated(
                                By.cssSelector(selector)));
    }

    private void requireErrorSelector() {
        assumeTrue(
                System.getenv("UTC_LOGIN_ERROR_SELECTOR") != null,
                "Can CSS selector thong bao loi dang nhap");
    }

    private void assertLoginRejected() {
        String selector =
                System.getenv("UTC_LOGIN_ERROR_SELECTOR");

        WebElement error =
                new WebDriverWait(driver, Duration.ofSeconds(10))
                        .until(ExpectedConditions
                                .visibilityOfElementLocated(
                                        By.cssSelector(selector)));

        assertFalse(error.getText().isBlank(),
                "Thong bao loi khong co noi dung");
    }

    // TC01: Dang nhap hop le
    @Test
    void TC01_ValidLogin() {
        requireCredentials();
        requireAuthenticatedIndicator();
        page.login(USER, PASS);
        assertAuthenticated();
    }
    // TC02: Sai mat khau
    @Test
    void TC02_WrongPassword() {
        requireCredentials();
        requireErrorSelector();
        page.login(USER, "InvalidPassword_ForTest");
        assertLoginRejected();
    }
    // TC03: Sai ten dang nhap
    @Test
    void TC03_WrongUsername() {
        requireErrorSelector();
        page.login("invalid_test_user", "InvalidPassword_ForTest");
        assertLoginRejected();
    }
    // TC04: Bo trong ca hai truong
    @Test
    void TC04_EmptyFields() {
        page.typeUsername("");
        page.typePassword("");

        assertEquals("", page.usernameValue());
        assertEquals("", page.passwordValue());

        // Chua kiem tra thong bao validation cua server
    }
    // TC05: Bo trong username
    @Test
    void TC05_EmptyUsername() {
        page.typeUsername("");
        page.typePassword("Test123");

        assertTrue(page.usernameValue().isEmpty());
    }
    // TC06: Bo trong password
    @Test
    void TC06_EmptyPassword() {
        page.typeUsername("testuser");
        page.typePassword("");

        assertTrue(page.passwordValue().isEmpty());
    }
    // TC07: Mat khau duoc che
    @Test
    void TC07_PasswordMasked() {
        page.typePassword("123456");

        assertEquals("password",
                page.password()
                        .getDomAttribute("type"));
    }
    // TC08: Checkbox ghi nho dang nhap
    @Test
    void TC08_RememberMe() {
        page.setRemember(true);
        assertTrue(page.isRememberChecked());

        page.setRemember(false);
        assertFalse(page.isRememberChecked());
    }
    // TC09: Quen mat khau
    @Test
    void TC09_ForgotPassword() {
        page.clickForgot();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains(
                        "/Login/GetPass"));

        assertTrue(driver.getCurrentUrl()
                .contains("/Login/GetPass"));
    }
    // TC10: Dang nhap email UTC
    @Test
    void TC10_EmailLogin() {
        page.clickEmail();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains(
                        "accounts.google.com"));

        assertTrue(driver.getCurrentUrl()
                .startsWith("https://accounts.google.com/"));
    }
    // TC11: Username chi co khoang trang
    @Test
    void TC11_WhitespaceUsername() {
        page.typeUsername("   ");

        assertEquals("   ", page.usernameValue());
    }
    // TC12: Ky tu dac biet giong SQL
    @Test
    void TC12_SpecialSQLInput() {
        requireSecurityEnvironment();
        requireErrorSelector();

        page.login("' OR '1'='1", "Test123");
        assertLoginRejected();
    }
    // TC13: Nhan Enter
    @Test
    void TC13_PressEnter() {
        requireErrorSelector();

        page.typeUsername("invalid_test_user");
        page.typePassword("InvalidPassword_ForTest");
        page.password().sendKeys(Keys.ENTER);

        assertLoginRejected();
    }
    // TC14: Mat khau co khoang trang
    @Test
    void TC14_PasswordWithSpaces() {
        page.typePassword("abc 123");

        assertEquals("abc 123",
                page.passwordValue());
    }
}
