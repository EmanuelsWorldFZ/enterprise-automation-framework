package com.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class CartPage {
  private final Page page;

  public CartPage(Page page) {
    this.page = page;
  }

  public int getItemCount() {
    page.getByText("Your Cart", new Page.GetByTextOptions().setExact(true)).waitFor();
    return page.locator(".cart_item").count();
  }

  public int getCartCount() {
    var badge = page.getByTestId("shopping-cart-badge");
    return badge.count() == 0 ? 0 : Integer.parseInt(badge.innerText());
  }

  public void removeItem(String productName) {
    page.locator(".cart_item")
        .filter(new Locator.FilterOptions().setHasText(productName))
        .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Remove"))
        .click();
  }

  public void checkout() {
    page.getByTestId("checkout").click();
  }
}
