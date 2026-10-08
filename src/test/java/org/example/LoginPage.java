
package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public static final String URL =
            "https://vanphongdientu.utc.edu.vn/Login";

    private final By username = By.name("username");
    private final By password = By.name("userpwd");
    private final By submit = By.cssSelector("input.submit_login");
    private final By remember = By.id("persistent");
    private final By rememberLabel =
            By.cssSelector("label.check[for='persistent']");
    private final By forgot =
            By.cssSelector("a[href='/Login/GetPass']");
    private final By email =
            By.linkText("Đăng nhập bằng e-mail UTC");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get(URL);
        wait.until(ExpectedConditions
                .visibilityOfElementLocated(username));
    }

    public WebElement username() {
        return driver.findElement(username);
    }

    public WebElement password() {
        return driver.findElement(password);
    }

    public void typeUsername(String value) {
        username().clear();
        username().sendKeys(value);
    }

    public void typePassword(String value) {
        password().clear();
        password().sendKeys(value);
    }

    public void clickLogin() {
        List<WebElement> buttons = driver.findElements(submit);
        for (WebElement button : buttons) {
            if (button.isDisplayed() && button.isEnabled()) {
                button.click();
                return;
            }
        }
        throw new NoSuchElementException(
                "Khong tim thay nut Dang nhap hien thi");
    }

    public void login(String user, String pass) {
        typeUsername(user);
        typePassword(pass);
        clickLogin();
    }

    public boolean loginFormVisible() {
        return driver.findElements(username).stream()
                .anyMatch(WebElement::isDisplayed);
    }

    public boolean loginPagePresent() {
        return !driver.findElements(username).isEmpty()
                && !driver.findElements(password).isEmpty();
    }

    public void setRemember(boolean value) {
        if (isRememberChecked() != value) {
            driver.findElement(rememberLabel).click();
        }
    }

    public boolean isRememberChecked() {
        return driver.findElement(remember).isSelected();
    }

    public void clickForgot() {
        driver.findElement(forgot).click();
    }

    public void clickEmail() {
        driver.findElement(email).click();
    }

    public String usernameValue() {
        return username().getDomProperty("value");
    }

    public String passwordValue() {
        return password().getDomProperty("value");
    }
}
