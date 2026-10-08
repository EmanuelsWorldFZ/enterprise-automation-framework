package com.saucedemo.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutPage;
import com.saucedemo.pages.InventoryPage;
import org.junit.jupiter.api.Test;

class CheckoutTests extends BaseTest {
  @Test
  void customerCanCompleteCheckout() {
    loginAsStandardUser();
    InventoryPage inventoryPage = new InventoryPage(page);
    inventoryPage.addToCart("Sauce Labs Backpack");
    inventoryPage.openCart();

    CartPage cartPage = new CartPage(page);
    cartPage.checkout();
    CheckoutPage checkoutPage = new CheckoutPage(page);
    checkoutPage.enterDetails("Ada", "Lovelace", "N1 1AA");
    checkoutPage.continueToOverview();
    page.getByText("Checkout: Overview").waitFor();
    assertEquals("Checkout: Overview", page.locator(".title").innerText());
    assertEquals(1, page.locator(".cart_item").count());

    checkoutPage.finishOrder();

    assertTrue(checkoutPage.getConfirmationMessage().contains("Thank you for your order!"));
  }

  @Test
  void checkoutRequiresPostalCode() {
    loginAsStandardUser();
    InventoryPage inventoryPage = new InventoryPage(page);
    inventoryPage.addToCart("Sauce Labs Backpack");
    inventoryPage.openCart();
    new CartPage(page).checkout();

    CheckoutPage checkoutPage = new CheckoutPage(page);
    checkoutPage.enterDetails("Ada", "Lovelace", "");
    checkoutPage.continueToOverview();

    assertTrue(checkoutPage.getErrorMessage().contains("Postal Code is required"));
  }
}
