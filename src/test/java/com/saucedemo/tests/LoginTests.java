package com.saucedemo.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Test;

class LoginTests extends BaseTest {
  @Test
  void standardUserCanLogInAndSeeProducts() {
    loginAsStandardUser();

    InventoryPage inventoryPage = new InventoryPage(page);
    assertTrue(inventoryPage.isDisplayed());
    assertEquals(6, page.locator(".inventory_item").count());
  }

  @ParameterizedTest
  @CsvSource({
      "invalid_user,secret_sauce,Username and password do not match any user in this service",
      "standard_user,wrong_password,Username and password do not match any user in this service",
      "standard_user,'',Password is required"
  })
  void invalidCredentialsShowAnError(String username, String password, String expectedMessage) {
    LoginPage loginPage = new LoginPage(page);
    loginPage.open(baseUrl());
    loginPage.login(username, password);

    assertTrue(loginPage.getErrorMessage().contains(expectedMessage));
  }

  @Test
  void standardUserCanLogOut() {
    loginAsStandardUser();
    new InventoryPage(page).logout();

    assertTrue(page.getByTestId("login-button").isVisible());
  }
}
