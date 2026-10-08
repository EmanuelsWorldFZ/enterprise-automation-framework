package com.saucedemo.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.TestData;
import com.saucedemo.utils.TestData.UserCredentials;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class LoginTests extends BaseTest {
  @ParameterizedTest(name = "{0}")
  @MethodSource("userAccounts")
  void accountsHaveTheirConfiguredLoginOutcome(UserCredentials user) {
    LoginPage loginPage = new LoginPage(page);
    loginPage.open(baseUrl());
    loginPage.login(user.username(), user.password());

    if (user.canLogin()) {
      InventoryPage inventoryPage = new InventoryPage(page);
      assertTrue(inventoryPage.isDisplayed());
      assertEquals(6, page.locator(".inventory_item").count());
    } else {
      assertTrue(loginPage.getErrorMessage().contains(user.expectedError()));
    }
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

  private static Stream<UserCredentials> userAccounts() {
    return TestData.users().stream();
  }
}
