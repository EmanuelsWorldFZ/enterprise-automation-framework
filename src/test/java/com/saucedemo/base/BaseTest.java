package com.saucedemo.base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.ConfigManager;
import com.saucedemo.utils.ScreenshotOnFailure;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;

public abstract class BaseTest {
  protected Playwright playwright;
  protected Browser browser;
  protected Page page;

  @RegisterExtension
  private final ScreenshotOnFailure screenshotOnFailure =
      new ScreenshotOnFailure(() -> page, Path.of("test-results", "screenshots"));

  @BeforeEach
  void setUpBrowser() {
    playwright = Playwright.create();
    playwright.selectors().setTestIdAttribute("data-test");
    String browserName = ConfigManager.get("browser");
    BrowserType browserType = switch (browserName.toLowerCase()) {
      case "chromium" -> playwright.chromium();
      case "firefox" -> playwright.firefox();
      case "webkit" -> playwright.webkit();
      default -> throw new IllegalArgumentException(
          "Unsupported browser '" + browserName + "'. Use chromium, firefox, or webkit.");
    };
    browser = browserType.launch(
        new BrowserType.LaunchOptions().setHeadless(ConfigManager.getBoolean("headless")));
    page = browser.newPage();
    page.setDefaultTimeout(ConfigManager.getInt("timeoutMs"));
  }

  @AfterEach
  void tearDownBrowser() {
    if (browser != null) {
      browser.close();
    }
    if (playwright != null) {
      playwright.close();
    }
  }

  protected String baseUrl() {
    return ConfigManager.get("baseUrl");
  }

  protected void loginAsStandardUser() {
    LoginPage loginPage = new LoginPage(page);
    loginPage.open(baseUrl());
    loginPage.login(ConfigManager.standardUsername(), ConfigManager.standardPassword());
  }
}
