package com.Page;

import com.Base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Welcome extends BasePage {

    private final By welcomeHeader = By.cssSelector("body > div > div > div > div.welcome-header > h1");

    public Welcome(WebDriver driver, WebDriverWait wait, String chromeDriverPath) {
        super(driver, wait, chromeDriverPath);
    }

    public String getWelcomeHeader() {
        return this.getText(welcomeHeader);
    }

}
