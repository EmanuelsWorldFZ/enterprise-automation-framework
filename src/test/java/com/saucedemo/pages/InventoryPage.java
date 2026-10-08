package com.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class InventoryPage extends BasePage {
  public InventoryPage(Page page) {
    super(page);
  }

  public boolean isDisplayed() {
    Locator heading = exactText("Products");
    heading.waitFor();
    return heading.isVisible();
  }

  public String getProductPrice(String productName) {
    return product(productName).locator(".inventory_item_price").innerText();
  }

  public String getProductTitle(String productName) {
    return product(productName).locator(".inventory_item_name").innerText();
  }

  public void addToCart(String productName) {
    product(productName)
        .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Add to cart"))
        .click();
  }

  public int getCartCount() {
    Locator badge = testId("shopping-cart-badge");
    return badge.count() == 0 ? 0 : Integer.parseInt(badge.innerText());
  }

  public void openCart() {
    testId("shopping-cart-link").click();
    exactText("Your Cart").waitFor();
  }

  public void logout() {
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Open Menu")).click();
    testId("logout-sidebar-link").click();
    testId("login-button").waitFor();
  }

  private Locator product(String productName) {
    return page.locator(".inventory_item")
        .filter(new Locator.FilterOptions().setHasText(productName));
  }
}
