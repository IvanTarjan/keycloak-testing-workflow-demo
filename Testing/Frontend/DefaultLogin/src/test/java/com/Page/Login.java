package com.Page;

import com.Base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Login extends BasePage {

    private final By usernameField = By.id("username");

    private final By passwordField = By.id("password");

    private final By loginButton = By.id("kc-login");

    public Login(WebDriver driver, WebDriverWait wait, String chromeDriverPath) {
        super(driver, wait, chromeDriverPath);
    }

    public void InsertUsername(String username) {
        this.sendKey(username, usernameField);
    }

    public void InsertPassword(String password){
        this.sendKey(password, passwordField);
    }

    public void ClickLogin(){
        this.clickear(loginButton);
    }
}
