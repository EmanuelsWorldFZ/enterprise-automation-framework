package com.saucedemo.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.utils.TestData;
import com.saucedemo.utils.TestData.CheckoutDetails;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class CheckoutTests extends BaseTest {
  @ParameterizedTest(name = "checkout customer {0}")
  @MethodSource("checkoutRows")
  void customerCanCompleteCheckout(CheckoutDetails details) {
    loginAsStandardUser();
    InventoryPage inventoryPage = new InventoryPage(page);
    inventoryPage.addToCart("Sauce Labs Backpack");
    inventoryPage.openCart();

    CartPage cartPage = new CartPage(page);
    cartPage.checkout();
    CheckoutPage checkoutPage = new CheckoutPage(page);
    checkoutPage.enterDetails(details.firstname(), details.lastname(), details.postcode());
    checkoutPage.continueToOverview();
    page.getByText("Checkout: Overview").waitFor();
    assertEquals("Checkout: Overview", page.locator(".title").innerText());
    assertEquals(1, page.locator(".cart_item").count());

    checkoutPage.finishOrder();

    assertTrue(checkoutPage.getConfirmationMessage().contains("Thank you for your order!"));
  }

  @Test
  void checkoutSubtotalMatchesTheSumOfCartPrices() {
    loginAsStandardUser();
    InventoryPage inventoryPage = new InventoryPage(page);
    inventoryPage.addToCart("Sauce Labs Backpack");
    inventoryPage.addToCart("Sauce Labs Bike Light");
    inventoryPage.addToCart("Sauce Labs Fleece Jacket");
    inventoryPage.openCart();

    CartPage cartPage = new CartPage(page);
    List<BigDecimal> itemPrices = cartPage.getItemPrices();
    BigDecimal expectedSubtotal = itemPrices.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    cartPage.checkout();

    CheckoutPage checkoutPage = new CheckoutPage(page);
    checkoutPage.enterDetails("Ada", "Lovelace", "N1 1AA");
    checkoutPage.continueToOverview();

    assertEquals(expectedSubtotal, checkoutPage.getSubtotal());
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

  private static Stream<CheckoutDetails> checkoutRows() {
    return TestData.checkoutDetails().stream();
  }
}
