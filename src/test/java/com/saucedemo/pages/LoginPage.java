package com.saucedemo.pages;

import com.microsoft.playwright.Page;

public class LoginPage extends BasePage {
  public LoginPage(Page page) {
    super(page);
  }

  public void open(String baseUrl) {
    navigateTo(baseUrl);
  }

  public void login(String username, String password) {
    testId("username").fill(username);
    testId("password").fill(password);
    testId("login-button").click();
  }

  public String getErrorMessage() {
    return testId("error").innerText();
  }
}
