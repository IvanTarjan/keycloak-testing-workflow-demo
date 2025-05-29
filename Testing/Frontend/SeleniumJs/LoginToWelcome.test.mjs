import { Builder, By, Key, until } from 'selenium-webdriver';
import { expect } from 'chai';
import * as fs from 'fs';
import chrome from 'selenium-webdriver/chrome.js';

describe('Login to Welcome Test', function () {
  this.timeout(60000);

  let driver;
  const screenshotsDir = './screenshots';

  const serverUrl = process.env.SERVER_URL;
  const realm = process.env.REALM;
  const clientId = process.env.CLIENT_ID;
  const username = process.env.TEST_USERNAME;
  const password = process.env.TEST_PASSWORD;
  const browser = process.env.BROWSER || 'chrome';

  if (!serverUrl || !realm || !clientId || !username || !password) {
    throw new Error('Missing required environment variables');
  }

  if (!fs.existsSync(screenshotsDir)) {
    fs.mkdirSync(screenshotsDir);
  }

  before(async function () {
    const options = new chrome.Options();
    options.addArguments(
      '--remote-allow-origins=*',
      '--window-size=1920,1080',
      '--ignore-certificate-errors',
      '--no-sandbox',
      '--disable-dev-shm-usage'
    );

    driver = await new Builder()
      .usingServer('http://localhost:4444/wd/hub')
      .forBrowser('chrome')
      .setChromeOptions(options)
      .build();
      console.log("Driver set")
  });

  after(async function () {
    if (driver) {
      await driver.quit();
    }
  });

  afterEach(async function () {
    if (driver && this.currentTest?.state === 'failed') {
      const encodedString = await driver.takeScreenshot();
      const filename = `${screenshotsDir}/${this.currentTest.title.replace(/[^a-z0-9]/gi, '_').toLowerCase()}.png`;
      fs.writeFileSync(filename, encodedString, 'base64');
    }
  });

  it('should log in and reach the welcome page', async function () {
    const loginUrl = `${serverUrl}/realms/${realm}/protocol/openid-connect/auth?response_type=code&client_id=${clientId}`;
    await driver.get(loginUrl);
    console.log("Login URL set")
    await driver.sleep(10000); // Optional wait for page load
    console.log("Page loaded")
    const usernameField = By.id('username');
    const passwordField = By.id('password');
    const loginButton = By.id('kc-login');

    await driver.wait(until.elementLocated(usernameField), 5000);
    await driver.findElement(usernameField).sendKeys(username);
    await driver.findElement(passwordField).sendKeys(password);
    await driver.findElement(loginButton).click();

    await driver.sleep(10000); // Wait for redirect

    const welcomeHeader = By.css('body > div > div > div > div.welcome-header > h1');
    await driver.wait(until.elementLocated(welcomeHeader), 5000);
    const headerText = await driver.findElement(welcomeHeader).getText();

    console.log(`Expected: Welcome to Keycloak\nGiven: ${headerText}`);
    expect(headerText).to.equal('Welcome to Keycloak');
  });
});
