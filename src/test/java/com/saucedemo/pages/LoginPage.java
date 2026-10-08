package com.saucedemo.pages;

import com.microsoft.playwright.Page;

public class LoginPage {
  private final Page page;

  public LoginPage(Page page) {
    this.page = page;
  }

  public void open(String baseUrl) {
    page.navigate(baseUrl);
  }

  public void login(String username, String password) {
    page.getByTestId("username").fill(username);
    page.getByTestId("password").fill(password);
    page.getByTestId("login-button").click();
  }

  public String getErrorMessage() {
    return page.getByTestId("error").innerText();
  }
}
