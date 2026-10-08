
package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public static final String URL =
            "https://vanphongdientu.utc.edu.vn/Login";

    private final By username = By.name("username");
    private final By password = By.name("userpwd");
    private final By loginButton =
            By.cssSelector("input.submit_login");
    private final By rememberMe = By.id("persistent");
    private final By rememberLabel =
            By.cssSelector("label.check[for='persistent']");
    private final By forgotPassword =
            By.cssSelector("a[href='/Login/GetPass']");
    private final By emailLogin =
            By.linkText("Đăng nhập bằng e-mail UTC");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get(URL);
        wait.until(ExpectedConditions
                .visibilityOfElementLocated(username));
    }

    public void enterUsername(String value) {
        WebElement input = driver.findElement(username);
        input.clear();
        input.sendKeys(value);
    }

    public void enterPassword(String value) {
        WebElement input = driver.findElement(password);
        input.clear();
        input.sendKeys(value);
    }

    public void clickLogin() {
        for (WebElement button : driver.findElements(loginButton)) {
            if (button.isDisplayed() && button.isEnabled()) {
                button.click();
                return;
            }
        }
        throw new NoSuchElementException(
                "Khong tim thay nut Dang nhap");
    }

    public void login(String user, String pass) {
        enterUsername(user);
        enterPassword(pass);
        clickLogin();
    }

    public WebElement usernameField() {
        return driver.findElement(username);
    }

    public WebElement passwordField() {
        return driver.findElement(password);
    }

    public boolean isLoginFormVisible() {
        return driver.findElements(username)
                .stream().anyMatch(WebElement::isDisplayed);
    }

    public void setRememberMe(boolean checked) {
        WebElement checkbox = driver.findElement(rememberMe);
        if (checkbox.isSelected() != checked) {
            driver.findElement(rememberLabel).click();
        }
    }

    public boolean isRememberMeChecked() {
        return driver.findElement(rememberMe).isSelected();
    }

    public void clickForgotPassword() {
        driver.findElement(forgotPassword).click();
    }

    public void clickEmailLogin() {
        driver.findElement(emailLogin).click();
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }
}
