package com.saucedemo.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.InventoryPage;
import org.junit.jupiter.api.Test;

class CartTests extends BaseTest {
  private static final String BACKPACK = "Sauce Labs Backpack";
  private static final String BIKE_LIGHT = "Sauce Labs Bike Light";

  @Test
  void productCanBeAddedAndRemovedFromCart() {
    loginAsStandardUser();
    InventoryPage inventoryPage = new InventoryPage(page);
    inventoryPage.addToCart(BACKPACK);
    assertEquals(1, inventoryPage.getCartCount());

    inventoryPage.openCart();
    CartPage cartPage = new CartPage(page);
    assertEquals(1, cartPage.getItemCount());
    cartPage.removeItem(BACKPACK);

    assertEquals(0, cartPage.getItemCount());
    assertEquals(0, cartPage.getCartCount());
  }

  @Test
  void multipleProductsAppearInCart() {
    loginAsStandardUser();
    InventoryPage inventoryPage = new InventoryPage(page);
    inventoryPage.addToCart(BACKPACK);
    inventoryPage.addToCart(BIKE_LIGHT);
    assertEquals(2, inventoryPage.getCartCount());

    inventoryPage.openCart();

    assertEquals(2, new CartPage(page).getItemCount());
  }
}
