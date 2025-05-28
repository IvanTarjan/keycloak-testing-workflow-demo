package com.Tests;

import com.Page.Login;
import com.Page.Welcome;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginToWelcomeTest {

    public WebDriver driver;
    public WebDriverWait wait;

    public String serverUrl;

    public String clientId;

    public String realm;

    public String chromeDriverPath;

    public String username;

    public String password;

    @BeforeAll
    public void setUp(){
        this.serverUrl = System.getenv("SERVER_URL");
        this.clientId = System.getenv("CLIENT_ID");
        this.realm = System.getenv("REALM");
        this.username = System.getenv("TEST_USERNAME");
        this.password = System.getenv("TEST_PASSWORD");
        this.chromeDriverPath = System.getenv("CHROMEDRIVER_PATH");

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=1920,1080","--ignore-certificate-errors","--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofMillis(2000));
    }

    @Test
    public void LoginToWelcome() throws InterruptedException {
        Login loginPage = new Login(driver , wait, chromeDriverPath);
        loginPage.setup();
        loginPage.url(serverUrl+"/realms/"+realm+"/protocol/openid-connect/auth?response_type=code&client_id="+clientId);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        loginPage.InsertUsername(username);
        loginPage.InsertPassword(password);
        loginPage.ClickLogin();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Welcome welcomePage = new Welcome(driver, wait, chromeDriverPath);
        System.out.println("Expected: Welcome to Keycloak\nGiven: " + welcomePage.getWelcomeHeader());
        Assertions.assertEquals("Welcome to Keycloak" , welcomePage.getWelcomeHeader());

    }

    @AfterAll
    public void close(){
        driver.quit();
    }

}