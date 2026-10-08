
package org.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class App {
    public static void main(String[] args) {

        WebDriver driver = new ChromeDriver();

        try {
            System.out.println("Chrome đã mở!");

            driver.get("https://vanphongdientu.utc.edu.vn/Login");

            System.out.println("Title: " + driver.getTitle());

            Thread.sleep(5000);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

        } finally {
            driver.quit();
            System.out.println("Chrome đã đóng!");
        }
    }
}
