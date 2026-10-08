package com.saucedemo.base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.ScreenshotOnFailure;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;

public abstract class BaseTest {
  private static final String DEFAULT_BASE_URL = "https://www.saucedemo.com";

  protected Playwright playwright;
  protected Browser browser;
  protected Page page;

  @RegisterExtension
  private final ScreenshotOnFailure screenshotOnFailure =
      new ScreenshotOnFailure(() -> page, Path.of("target", "test-artifacts", "screenshots"));

  @BeforeEach
  void setUpBrowser() {
    playwright = Playwright.create();
    playwright.selectors().setTestIdAttribute("data-test");
    String browserName = System.getProperty(
        "browser", System.getenv().getOrDefault("PLAYWRIGHT_BROWSER", "chromium"));
    BrowserType browserType = switch (browserName.toLowerCase()) {
      case "chromium" -> playwright.chromium();
      case "firefox" -> playwright.firefox();
      case "webkit" -> playwright.webkit();
      default -> throw new IllegalArgumentException(
          "Unsupported browser '" + browserName + "'. Use chromium, firefox, or webkit.");
    };
    browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(true));
    page = browser.newPage();
    page.setDefaultTimeout(10_000);
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
    return System.getProperty(
        "baseUrl", System.getenv().getOrDefault("SAUCEDEMO_BASE_URL", DEFAULT_BASE_URL));
  }

  protected void loginAsStandardUser() {
    LoginPage loginPage = new LoginPage(page);
    loginPage.open(baseUrl());
    loginPage.login("standard_user", "secret_sauce");
  }
}
