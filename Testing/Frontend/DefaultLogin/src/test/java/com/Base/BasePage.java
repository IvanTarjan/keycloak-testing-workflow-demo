package com.Base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {
    public WebDriver driver;
    public WebDriverWait wait;
    private String chromeDriverPath;

    public BasePage(WebDriver driver, WebDriverWait wait, String chromeDriverPath) {
        this.driver = driver;
        this.wait = wait;
        this.chromeDriverPath = chromeDriverPath;
    }

    public void setup() {
        System.setProperty("webdriver.chrome.driver",chromeDriverPath);
        driver.manage().window().maximize();
    }

    public void url(String url) throws InterruptedException {
        driver.get(url);
        Thread.sleep(1000);
    }

    public void close() {
        driver.quit();
    }

    public WebElement findElement(By locator) {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        return driver.findElement(locator);
    }

    public void sendText(String imputText, By locator) {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        this.findElement(locator).clear();
        this.findElement(locator).sendKeys(imputText);
    }

    public void sendKey(CharSequence pressKeys, By locator) {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        this.findElement(locator).sendKeys(pressKeys);
    }

    public void clickear(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
        this.findElement(locator).click();
    }

    public String getText(By locator) {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        return this.findElement(locator).getText();
    }
}
