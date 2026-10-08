package com.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class InventoryPage {
  private final Page page;

  public InventoryPage(Page page) {
    this.page = page;
  }

  public boolean isDisplayed() {
    Locator heading = page.getByText("Products", new Page.GetByTextOptions().setExact(true));
    heading.waitFor();
    return heading.isVisible();
  }

  public void addToCart(String productName) {
    product(productName)
        .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Add to cart"))
        .click();
  }

  public int getCartCount() {
    Locator badge = page.getByTestId("shopping-cart-badge");
    return badge.count() == 0 ? 0 : Integer.parseInt(badge.innerText());
  }

  public void openCart() {
    page.getByTestId("shopping-cart-link").click();
    page.getByText("Your Cart", new Page.GetByTextOptions().setExact(true)).waitFor();
  }

  public void logout() {
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Open Menu")).click();
    page.getByTestId("logout-sidebar-link").click();
    page.getByTestId("login-button").waitFor();
  }

  private Locator product(String productName) {
    return page.locator(".inventory_item")
        .filter(new Locator.FilterOptions().setHasText(productName));
  }
}
